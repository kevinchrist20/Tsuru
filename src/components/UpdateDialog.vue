<script setup lang="ts">
import { computed } from 'vue'
import { useUpdater } from '../composables/useUpdater'

const {
  status,
  errorMessage,
  currentVersion,
  newVersion,
  releaseNotes,
  progressPercent,
  isVisible,
  startDownload,
  installNow,
  dismiss,
} = useUpdater()

const title = computed(() => {
  switch (status.value) {
    case 'available':
      return 'Update available'
    case 'downloading':
      return 'Downloading update'
    case 'downloaded':
      return 'Update ready to install'
    case 'installing':
      return 'Updating…'
    case 'error':
      return 'Update failed'
    default:
      return 'Tsuru'
  }
})

const ariaLabel = computed(() => `Tsuru update: ${title.value}`)
</script>

<template>
  <Transition name="update-dialog">
    <div
      v-if="isVisible"
      class="fixed bottom-4 left-4 z-40 w-88 max-w-[calc(100vw-2rem)]"
      role="dialog"
      :aria-label="ariaLabel"
    >
      <div class="card p-default">
        <div class="mb-small flex items-start justify-between gap-small">
          <p class="body2 font-semibold text-text-color">
            {{ title }}
          </p>
          <button
            v-if="status !== 'installing'"
            type="button"
            class="icon-btn -mt-1 -mr-1"
            aria-label="Dismiss update"
            @click="dismiss"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M18 6 6 18" />
              <path d="m6 6 12 12" />
            </svg>
          </button>
        </div>

        <p v-if="newVersion" class="caption mb-default text-medium-grey">
          {{ currentVersion }} → {{ newVersion }}
        </p>

        <div
          v-if="status === 'available' && releaseNotes"
          class="mb-default max-h-24 overflow-auto rounded-md border border-hairline bg-dark-deep p-small text-caption text-light-grey whitespace-pre-line"
        >
          {{ releaseNotes }}
        </div>

        <div v-if="status === 'downloading'">
          <div class="mb-small h-1 w-full overflow-hidden rounded-full bg-white/10">
            <div
              class="h-full rounded-full bg-telegram-blue transition-all duration-200"
              :style="{ width: `${progressPercent}%` }"
            />
          </div>
          <p class="caption text-medium-grey">
            {{ progressPercent }}%
          </p>
        </div>

        <div v-if="status === 'installing'" class="flex items-center gap-small body2 text-medium-grey">
          <span class="spinner" />
          Installing the update and restarting…
        </div>

        <div v-if="status === 'error'" class="mb-default body2 text-red-300">
          {{ errorMessage }}
        </div>

        <div class="flex items-center gap-small">
          <button
            v-if="status === 'available' || status === 'error'"
            type="button"
            class="btn btn-primary flex-1"
            @click="startDownload"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
              <path d="M7 10l5 5 5-5" />
              <path d="M12 15V3" />
            </svg>
            {{ status === 'error' ? 'Try again' : 'Download update' }}
          </button>

          <button
            v-if="status === 'downloaded'"
            type="button"
            class="btn btn-primary flex-1"
            @click="installNow"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M21 12a9 9 0 1 1-2.64-6.36" />
              <path d="M21 3v5h-5" />
            </svg>
            Restart & update
          </button>

          <button
            v-if="status !== 'installing'"
            type="button"
            class="btn btn-ghost"
            @click="dismiss"
          >
            Later
          </button>
        </div>
      </div>
    </div>
  </Transition>
</template>

<style scoped>
.update-dialog-enter-active,
.update-dialog-leave-active {
  transition: opacity 0.2s ease, transform 0.2s ease;
}

.update-dialog-enter-from,
.update-dialog-leave-to {
  opacity: 0;
  transform: translateY(12px);
}
</style>
