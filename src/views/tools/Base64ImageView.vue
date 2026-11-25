<script setup lang="ts">
import type { Tool } from '../../models/Tool'
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
const fileName = ref('')
const fileSize = ref(0)
const encodeOutput = ref('')
const encodeError = ref('')
const encoding = ref(false)
const decodeInput = ref('')
const decodeOutput = ref('')
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

async function onFileChange(event: Event) {
  const input = event.target as HTMLInputElement
  const file = input.files?.[0]

  if (!file) {
    return
  }

  encodeRequestId += 1
  const id = encodeRequestId
  encodeError.value = ''
  encodeOutput.value = ''
  fileName.value = file.name
  fileSize.value = file.size

  if (!isAcceptedImage(file)) {
    encodeError.value = 'Choose a JPG or PNG image.'
    input.value = ''
    encoding.value = false
    return
  }

  if (file.size > MAX_IMAGE_BYTES) {
    encodeError.value = 'Image must be 8 MB or smaller.'
    input.value = ''
    encoding.value = false
    return
  }

  encoding.value = true

  try {
    const rawDataUrl = await readImageAsDataUrl(file)
    if (id !== encodeRequestId) {
      return
    }
    encodeOutput.value = parseImageInput(rawDataUrl).dataUrl
  }
  catch (err) {
    if (id !== encodeRequestId) {
      return
    }
    encodeError.value = extractErrorMessage(err, 'Could not encode this image.')
  }
  finally {
    if (id === encodeRequestId) {
      encoding.value = false
    }
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
      decodeError.value = ''
    }
    catch (err) {
      if (id !== decodeRequestId) {
        return
      }
      decodeOutput.value = ''
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

function clearActive() {
  if (mode.value === 'encode') {
    encodeRequestId += 1
    encoding.value = false
    encodeOutput.value = ''
    encodeError.value = ''
    fileName.value = ''
    fileSize.value = 0
    if (fileInput.value) {
      fileInput.value.value = ''
    }
    return
  }

  if (decodeTimer) {
    clearTimeout(decodeTimer)
    decodeTimer = undefined
  }
  decodeRequestId += 1
  decodeInput.value = ''
  decodeOutput.value = ''
  decodeError.value = ''
  decoding.value = false
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
      <div class="bg-dark-background rounded-md p-default mb-default">
        <label for="base64-image-file" class="block mb-small text-text-color body2">
          Image file
        </label>
        <input
          id="base64-image-file"
          ref="fileInput"
          type="file"
          :accept="IMAGE_ACCEPT"
          class="block w-full text-medium-grey body2 file:mr-default file:py-2 file:px-4 file:rounded-md file:border-0 file:bg-telegram-blue file:text-white file:font-medium hover:file:bg-telegram-blue/80 file:cursor-pointer"
          @change="onFileChange"
        >
        <p v-if="fileName" class="caption text-medium-grey mt-small">
          {{ fileName }}<span v-if="fileSizeLabel"> · {{ fileSizeLabel }}</span>
        </p>
        <p class="caption text-medium-grey mt-small">
          JPG or PNG, up to 8 MB.
        </p>
      </div>

      <div v-if="encoding" class="mb-default text-medium-grey body2">
        Encoding image…
      </div>

      <div
        v-if="encodeError"
        class="mb-default p-default bg-red-900/50 text-red-300 rounded-md body2"
        role="alert"
      >
        {{ encodeError }}
      </div>

      <div v-if="encodeOutput" class="mb-default">
        <p class="block mb-small text-text-color body2">
          Preview
        </p>
        <img
          :src="encodeOutput"
          alt="Encoded image preview"
          class="max-h-64 max-w-full rounded-md border border-medium-grey/30 object-contain"
        >
      </div>

      <div class="mb-default">
        <label for="base64-image-output" class="block mb-small text-text-color body2">
          Output Base64
        </label>
        <textarea
          id="base64-image-output"
          :value="encodeOutput"
          class="w-full p-default bg-dark-background text-text-color rounded-md border-none outline-none focus:ring-1 focus:ring-telegram-blue resize-y font-mono"
          rows="8"
          spellcheck="false"
          readonly
        />
      </div>
    </div>

    <div v-else>
      <div class="mb-default">
        <label for="base64-image-input" class="block mb-small text-text-color body2">
          Input Base64 or data URL
        </label>
        <textarea
          id="base64-image-input"
          v-model="decodeInput"
          class="w-full p-default bg-dark-background text-text-color rounded-md border-none outline-none focus:ring-1 focus:ring-telegram-blue resize-y font-mono"
          rows="8"
          placeholder="Paste a data:image/...;base64,... URL or raw Base64"
          spellcheck="false"
          autocomplete="off"
        />
      </div>

      <div v-if="decoding" class="mb-default text-medium-grey body2">
        Decoding image…
      </div>

      <div
        v-if="decodeError"
        class="mb-default p-default bg-red-900/50 text-red-300 rounded-md body2"
        role="alert"
      >
        {{ decodeError }}
      </div>

      <div class="mb-default">
        <p class="block mb-small text-text-color body2">
          Preview
        </p>
        <img
          v-if="decodeOutput"
          :src="decodeOutput"
          alt="Decoded image preview"
          class="max-h-64 max-w-full rounded-md border border-medium-grey/30 object-contain"
        >
        <div
          v-else
          class="bg-dark-background rounded-md p-large text-center text-medium-grey body2"
        >
          Image preview will appear here.
        </div>
      </div>
    </div>
  </BaseView>
</template>
