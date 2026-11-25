<script setup lang="ts">
import type { Tool } from '../models/Tool'
import { onBeforeUnmount, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { copyToClipboard } from '../utils/clipboard'

const props = defineProps<{
  tool: Tool
  inputText: string
  outputText: string
}>()

const emit = defineEmits([
  'back',
  'update:inputText',
  'update:outputText',
  'copy',
  'clear',
])

const showInfoModal = ref(false)
const showToast = ref(false)
const toastMessage = ref('')
let toastTimer: ReturnType<typeof setTimeout> | undefined

const router = useRouter()

function handleBack() {
  emit('back')
  router.back()
}

function showToastMessage(message: string) {
  toastMessage.value = message
  showToast.value = true

  if (toastTimer) {
    clearTimeout(toastTimer)
  }

  toastTimer = setTimeout(() => {
    showToast.value = false
  }, 2400)
}

async function handleCopy() {
  const success = await copyToClipboard(props.outputText)

  if (success) {
    showToastMessage('Copied to clipboard')
    emit('copy', props.outputText)
  }
  else {
    showToastMessage('Failed to copy to clipboard')
  }
}

function handleClear() {
  emit('clear')
  emit('update:inputText', '')
  emit('update:outputText', '')
  showToastMessage('Cleared input and output')
}

function onEscapeKey(event: KeyboardEvent) {
  if (event.key === 'Escape') {
    showInfoModal.value = false
  }
}

watch(showInfoModal, (open) => {
  if (open) {
    window.addEventListener('keydown', onEscapeKey)
  }
  else {
    window.removeEventListener('keydown', onEscapeKey)
  }
})

onBeforeUnmount(() => {
  window.removeEventListener('keydown', onEscapeKey)
  if (toastTimer) {
    clearTimeout(toastTimer)
  }
})
</script>

<template>
  <div class="flex min-h-screen flex-col">
    <header class="sticky top-0 z-20 border-b border-hairline bg-dark-background/85 backdrop-blur-md">
      <div class="mx-auto flex w-full max-w-3xl items-center gap-small px-default py-small">
        <button type="button" class="icon-btn" aria-label="Go back" @click="handleBack">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="m15 18-6-6 6-6" />
          </svg>
        </button>

        <div class="min-w-0 flex-1">
          <p class="truncate text-sm font-semibold text-text-color">
            {{ tool.name }}
          </p>
          <p class="caption text-medium-grey">
            {{ tool.category }}
          </p>
        </div>

        <button type="button" class="icon-btn" aria-label="About this tool" @click="showInfoModal = true">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="10" />
            <path d="M12 16v-4" />
            <path d="M12 8h.01" />
          </svg>
        </button>
      </div>
    </header>

    <main class="flex-grow px-default pb-large pt-large">
      <div class="mx-auto w-full max-w-3xl">
        <slot />
      </div>
    </main>

    <footer class="sticky bottom-0 z-10 border-t border-hairline bg-dark-shade/85 backdrop-blur-md">
      <div class="mx-auto flex w-full max-w-3xl items-center gap-small px-default py-small">
        <slot name="actions">
          <button type="button" class="btn btn-primary" :disabled="!outputText" @click="handleCopy">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <rect x="9" y="9" width="11" height="11" rx="2" />
              <path d="M5 15V5a2 2 0 0 1 2-2h10" />
            </svg>
            Copy
          </button>

          <button type="button" class="btn btn-ghost" @click="handleClear">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M18 6 6 18" />
              <path d="m6 6 12 12" />
            </svg>
            Clear
          </button>

          <slot name="additional-actions" />
        </slot>
      </div>
    </footer>

    <div class="pointer-events-none fixed inset-x-0 bottom-20 z-30 flex justify-center px-default">
      <Transition name="toast">
        <div
          v-if="showToast"
          class="pointer-events-auto flex items-center gap-small rounded-full border border-telegram-blue/30 bg-telegram-blue/15 px-4 py-2 text-sm text-text-color shadow-pop backdrop-blur-sm"
          role="status"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-telegram-blue" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="m5 12 5 5L20 7" />
          </svg>
          {{ toastMessage }}
        </div>
      </Transition>
    </div>

    <Transition name="modal">
      <div
        v-if="showInfoModal"
        class="fixed inset-0 z-50 flex items-center justify-center bg-black/60 p-default backdrop-blur-sm"
        @click.self="showInfoModal = false"
      >
        <div class="modal-panel card w-full max-w-xl overflow-hidden" role="dialog" aria-modal="true" :aria-label="`About ${tool.name}`">
          <div class="flex items-start justify-between gap-default border-b border-hairline p-default">
            <div class="min-w-0">
              <h5 class="truncate text-text-color">
                {{ tool.name }}
              </h5>
              <p class="caption mt-0.5 text-medium-grey">
                {{ tool.category }}
              </p>
            </div>

            <button type="button" class="icon-btn" aria-label="Close" @click="showInfoModal = false">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M18 6 6 18" />
                <path d="m6 6 12 12" />
              </svg>
            </button>
          </div>

          <div class="markdown-content max-h-[60vh] overflow-auto p-default body2 text-light-grey">
            <div v-html="tool.description" />
          </div>

          <div class="flex justify-end border-t border-hairline p-default pt-small">
            <button type="button" class="btn btn-ghost" @click="showInfoModal = false">
              Close
            </button>
          </div>
        </div>
      </div>
    </Transition>
  </div>
</template>

<style scoped>
.markdown-content :deep(p) {
  margin-bottom: var(--spacing-default);
}

.markdown-content :deep(p:last-child) {
  margin-bottom: 0;
}

.markdown-content :deep(a) {
  color: var(--color-telegram-blue);
  text-decoration: none;
}

.markdown-content :deep(a:hover) {
  text-decoration: underline;
}

.markdown-content :deep(b) {
  color: var(--color-text-color);
}

.markdown-content :deep(code) {
  font-family: var(--font-mono);
  font-size: 0.85em;
  padding: 0.15em 0.4em;
  border-radius: 6px;
  border: 1px solid var(--color-hairline);
  background-color: #0f1216;
}

.markdown-content :deep(pre) {
  font-family: var(--font-mono);
  font-size: 0.85em;
  line-height: 1.6;
  padding: var(--spacing-default);
  border-radius: var(--radius-md);
  border: 1px solid var(--color-hairline);
  background-color: #0f1216;
  overflow-x: auto;
  margin: var(--spacing-default) 0;
}

.markdown-content :deep(ul),
.markdown-content :deep(ol) {
  padding-left: var(--spacing-large);
  margin-bottom: var(--spacing-default);
}
</style>
