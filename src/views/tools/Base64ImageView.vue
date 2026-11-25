<script setup lang="ts">
import type { Tool } from '../../models/Tool'
import type { ParsedImage } from '../../services/encoders/base64-image.service'
import type { CodecMode } from '../../types/codec'
import { computed, ref, watch } from 'vue'
import BaseView from '../../components/BaseView.vue'
import ModeTabs from '../../components/codec/ModeTabs.vue'
import {
  formatBytes,
  IMAGE_ACCEPT,
  isAcceptedImage,
  MAX_IMAGE_BYTES,
  parseImageInput,
  readImageAsDataUrl,
} from '../../services/encoders/base64-image.service'
import { extractErrorMessage } from '../../utils/error'

defineProps<{ tool: Tool }>()

const mode = ref<CodecMode>('encode')
const fileInput = ref<HTMLInputElement | null>(null)
const dragActive = ref(false)
const fileName = ref('')
const fileSize = ref(0)
const encodeMime = ref('')
const encodeOutput = ref('')
const encodeError = ref('')
const encoding = ref(false)
const decodeInput = ref('')
const decodeOutput = ref('')
const decodeInfo = ref<ParsedImage | null>(null)
const decodeError = ref('')
const decoding = ref(false)

let encodeRequestId = 0
let decodeTimer: ReturnType<typeof setTimeout> | undefined
let decodeRequestId = 0

const activeInput = computed(() => mode.value === 'encode'
  ? fileName.value
  : decodeInput.value)
const activeOutput = computed(() => mode.value === 'encode'
  ? encodeOutput.value
  : decodeOutput.value)
const fileSizeLabel = computed(() => fileSize.value > 0
  ? formatBytes(fileSize.value)
  : '')

function resetFileInput() {
  if (fileInput.value) {
    fileInput.value.value = ''
  }
}

function handleFile(file: File) {
  encodeRequestId += 1
  const id = encodeRequestId

  encodeError.value = ''
  encodeOutput.value = ''
  encodeMime.value = ''
  fileName.value = file.name
  fileSize.value = file.size

  if (!isAcceptedImage(file)) {
    encodeError.value = 'Choose a JPG or PNG image.'
    encoding.value = false
    return
  }

  if (file.size > MAX_IMAGE_BYTES) {
    encodeError.value = 'Image must be 8 MB or smaller.'
    encoding.value = false
    return
  }

  encoding.value = true

  void readImageAsDataUrl(file)
    .then((rawDataUrl) => {
      if (id !== encodeRequestId) {
        return
      }

      const parsed = parseImageInput(rawDataUrl)
      encodeOutput.value = parsed.dataUrl
      encodeMime.value = parsed.mime
    })
    .catch((err) => {
      if (id !== encodeRequestId) {
        return
      }
      encodeError.value = extractErrorMessage(err, 'Could not encode this image.')
    })
    .finally(() => {
      if (id === encodeRequestId) {
        encoding.value = false
      }
    })
}

function onFileChange(event: Event) {
  const input = event.target as HTMLInputElement
  const file = input.files?.[0]

  if (!file) {
    return
  }

  input.value = ''
  handleFile(file)
}

function onDrop(event: DragEvent) {
  dragActive.value = false
  const file = event.dataTransfer?.files?.[0]

  if (file) {
    handleFile(file)
  }
}

function scheduleDecode(value: string) {
  if (decodeTimer) {
    clearTimeout(decodeTimer)
    decodeTimer = undefined
  }

  decodeRequestId += 1
  decoding.value = false

  if (!value.trim()) {
    decodeOutput.value = ''
    decodeInfo.value = null
    decodeError.value = ''
    return
  }

  const id = decodeRequestId
  decoding.value = true

  decodeTimer = setTimeout(() => {
    decodeTimer = undefined

    try {
      const parsed = parseImageInput(value)

      if (id !== decodeRequestId) {
        return
      }

      decodeOutput.value = parsed.dataUrl
      decodeInfo.value = parsed
      decodeError.value = ''
    }
    catch (err) {
      if (id !== decodeRequestId) {
        return
      }

      decodeOutput.value = ''
      decodeInfo.value = null
      decodeError.value = extractErrorMessage(err, 'Could not decode this image.')
    }
    finally {
      if (id === decodeRequestId) {
        decoding.value = false
      }
    }
  }, 250)
}

watch(decodeInput, scheduleDecode)

function onEncodePreviewError() {
  encodeError.value = 'The encoded image could not be rendered.'
}

function onDecodePreviewError() {
  decodeRequestId += 1
  decoding.value = false
  decodeOutput.value = ''
  decodeInfo.value = null
  decodeError.value = 'The decoded data is not a renderable image.'
}

function clearActive() {
  if (mode.value === 'encode') {
    encodeRequestId += 1
    encoding.value = false
    encodeOutput.value = ''
    encodeMime.value = ''
    encodeError.value = ''
    fileName.value = ''
    fileSize.value = 0
    resetFileInput()
    return
  }

  if (decodeTimer) {
    clearTimeout(decodeTimer)
    decodeTimer = undefined
  }

  decodeRequestId += 1
  decoding.value = false
  decodeInput.value = ''
  decodeOutput.value = ''
  decodeInfo.value = null
  decodeError.value = ''
}
</script>

<template>
  <BaseView
    :tool="tool"
    :input-text="activeInput"
    :output-text="activeOutput"
    @clear="clearActive"
  >
    <ModeTabs v-model="mode" />

    <div v-if="mode === 'encode'">
      <label
        for="base64-image-file"
        class="flex cursor-pointer flex-col items-center justify-center gap-small rounded-lg border border-dashed p-large text-center outline-none transition-colors duration-200 has-[:focus-visible]:border-telegram-blue/60"
        :class="dragActive
          ? 'border-telegram-blue/60 bg-telegram-blue/5'
          : 'border-medium-grey/30 bg-dark-background/50 hover:border-telegram-blue/40 hover:bg-white/[0.03]'"
        @dragenter.prevent="dragActive = true"
        @dragover.prevent="dragActive = true"
        @dragleave="dragActive = false"
        @drop.prevent="onDrop"
      >
        <span class="icon-tile h-11 w-11" aria-hidden="true">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <rect x="3" y="3" width="18" height="18" rx="2" />
            <circle cx="8.5" cy="8.5" r="1.5" />
            <path d="m21 15-5-5L5 21" />
          </svg>
        </span>

        <span class="body2 text-text-color">
          Choose an image or drop it here
        </span>
        <span class="caption text-medium-grey">
          JPG or PNG · up to 8 MB
        </span>

        <input
          id="base64-image-file"
          ref="fileInput"
          type="file"
          class="sr-only"
          :accept="IMAGE_ACCEPT"
          @change="onFileChange"
        >
      </label>

      <div v-if="fileName" class="mt-default flex flex-wrap items-center gap-small">
        <span class="chip max-w-full truncate">
          {{ fileName }}
        </span>
        <span v-if="fileSizeLabel" class="chip">
          {{ fileSizeLabel }}
        </span>
        <span v-if="encodeMime" class="chip">
          {{ encodeMime }}
        </span>
      </div>

      <div v-if="encoding" class="mt-default flex items-center gap-small body2 text-medium-grey">
        <span class="spinner" />
        Encoding image…
      </div>

      <div
        v-if="encodeError"
        class="alert-danger mt-default"
        role="alert"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="mt-0.5 h-4 w-4 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
          <path d="M10.29 3.86 1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z" />
          <path d="M12 9v4" />
          <path d="M12 17h.01" />
        </svg>
        <span>{{ encodeError }}</span>
      </div>

      <div v-if="encodeOutput" class="card mt-default p-default">
        <p class="mb-small body2 text-medium-grey">
          Preview
        </p>
        <img
          :src="encodeOutput"
          alt="Encoded image preview"
          class="mx-auto max-h-64 max-w-full rounded-md object-contain"
          @error="onEncodePreviewError"
        >
      </div>

      <div class="mt-default">
        <label for="base64-image-output" class="mb-small block body2 text-text-color">
          Output Base64
        </label>
        <textarea
          id="base64-image-output"
          :value="encodeOutput"
          class="field w-full resize-y font-mono"
          rows="7"
          spellcheck="false"
          readonly
        />
      </div>
    </div>

    <div v-else>
      <div class="mt-default">
        <label for="base64-image-input" class="mb-small block body2 text-text-color">
          Input Base64 or data URL
        </label>
        <textarea
          id="base64-image-input"
          v-model="decodeInput"
          class="field w-full resize-y font-mono"
          rows="7"
          placeholder="Paste a data:image/...;base64,... URL or raw Base64"
          spellcheck="false"
          autocomplete="off"
        />
      </div>

      <div v-if="decoding" class="mt-default flex items-center gap-small body2 text-medium-grey">
        <span class="spinner" />
        Decoding image…
      </div>

      <div
        v-if="decodeError"
        class="alert-danger mt-default"
        role="alert"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="mt-0.5 h-4 w-4 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
          <path d="M10.29 3.86 1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z" />
          <path d="M12 9v4" />
          <path d="M12 17h.01" />
        </svg>
        <span>{{ decodeError }}</span>
      </div>

      <div class="card mt-default p-default">
        <p class="mb-small body2 text-medium-grey">
          Preview
        </p>

        <img
          v-if="decodeOutput"
          :src="decodeOutput"
          alt="Decoded image preview"
          class="mx-auto max-h-64 max-w-full rounded-md object-contain"
          @error="onDecodePreviewError"
        >
        <div
          v-else
          class="rounded-md border border-dashed border-medium-grey/30 p-large text-center body2 text-medium-grey"
        >
          Image preview will appear here
        </div>

        <div v-if="decodeInfo" class="mt-small flex flex-wrap gap-small">
          <span class="chip">
            {{ decodeInfo.mime }}
          </span>
          <span class="chip">
            {{ formatBytes(decodeInfo.size) }} decoded
          </span>
        </div>
      </div>
    </div>
  </BaseView>
</template>
