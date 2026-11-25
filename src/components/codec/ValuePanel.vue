<script setup lang="ts">
import { computed, useId } from 'vue'

const props = defineProps<{
  input: string
  output: string
  inputLabel: string
  outputLabel: string
  error?: string
  loading?: boolean
  inputPlaceholder?: string
  inputRows?: number
  outputRows?: number
  inputMono?: boolean
  outputMono?: boolean
}>()

const emit = defineEmits<{
  'update:input': [value: string]
}>()

const inputId = useId()
const outputId = useId()
const charCount = computed(() => props.input.length.toLocaleString())
</script>

<template>
  <div>
    <div class="mb-default">
      <div class="mb-small flex items-center justify-between">
        <label class="block body2 text-text-color" :for="inputId">
          {{ inputLabel }}
        </label>
        <span class="caption tabular-nums text-medium-grey">
          {{ charCount }} chars
        </span>
      </div>
      <textarea
        :id="inputId"
        :value="input"
        class="field w-full resize-y"
        :class="inputMono ? 'font-mono' : ''"
        :rows="inputRows ?? 6"
        :placeholder="inputPlaceholder"
        spellcheck="false"
        autocomplete="off"
        @input="emit('update:input', ($event.target as HTMLTextAreaElement).value)"
      />
    </div>

    <div
      v-if="error"
      class="alert-danger"
      role="alert"
    >
      <svg xmlns="http://www.w3.org/2000/svg" class="mt-0.5 h-4 w-4 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
        <path d="M10.29 3.86 1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z" />
        <path d="M12 9v4" />
        <path d="M12 17h.01" />
      </svg>
      <span>{{ error }}</span>
    </div>

    <div class="mb-default">
      <div class="mb-small flex items-center justify-between">
        <label class="block body2 text-text-color" :for="outputId">
          {{ outputLabel }}
        </label>
        <span v-if="loading" class="caption flex items-center gap-1 text-medium-grey">
          <span class="spinner" />
          Processing…
        </span>
      </div>
      <textarea
        :id="outputId"
        :value="output"
        class="field w-full resize-y"
        :class="outputMono ? 'font-mono' : ''"
        :rows="outputRows ?? 6"
        spellcheck="false"
        readonly
      />
    </div>
  </div>
</template>
