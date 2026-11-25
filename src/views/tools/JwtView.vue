<script setup lang="ts">
import type { Tool } from '../../models/Tool'
import type { JwtParts } from '../../types/bindings'
import { computed, ref, watch } from 'vue'
import BaseView from '../../components/BaseView.vue'
import JwtService from '../../services/security/jwt.service'
import { extractErrorMessage } from '../../utils/error'

defineProps<{ tool: Tool }>()

const input = ref('')
const parts = ref<JwtParts | null>(null)
const error = ref('')
const loading = ref(false)

let timer: ReturnType<typeof setTimeout> | undefined
let requestId = 0

const payload = computed(() => parts.value?.payload ?? '')

async function run(value: string) {
  const id = ++requestId
  loading.value = true
  error.value = ''

  try {
    const decoded = await JwtService.decode(value)

    if (id !== requestId) {
      return
    }

    parts.value = decoded
  }
  catch (err) {
    if (id !== requestId) {
      return
    }

    parts.value = null
    error.value = extractErrorMessage(err, 'Could not decode this token.')
  }
  finally {
    if (id === requestId) {
      loading.value = false
    }
  }
}

function schedule(value: string) {
  if (timer) {
    clearTimeout(timer)
    timer = undefined
  }

  requestId += 1
  loading.value = false

  if (value.trim().length === 0) {
    parts.value = null
    error.value = ''
    return
  }

  timer = setTimeout(() => {
    timer = undefined
    void run(value)
  }, 200)
}

watch(input, schedule)

function reset() {
  if (timer) {
    clearTimeout(timer)
    timer = undefined
  }

  requestId += 1
  input.value = ''
  parts.value = null
  error.value = ''
  loading.value = false
}
</script>

<template>
  <BaseView
    v-model:input-text="input"
    v-model:output-text="payload"
    :tool="tool"
    @clear="reset"
  >
    <div class="mb-default">
      <label for="jwt-input" class="mb-small block body2 text-text-color">
        Token
      </label>
      <textarea
        id="jwt-input"
        v-model="input"
        class="field w-full resize-y font-mono"
        rows="5"
        placeholder="Paste a JWT (header.payload.signature)"
        spellcheck="false"
        autocomplete="off"
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

    <div v-if="loading" class="mb-default flex items-center gap-small body2 text-medium-grey">
      <span class="spinner" />
      Decoding token…
    </div>

    <div
      v-if="parts"
      class="alert-warning"
      role="note"
    >
      <svg xmlns="http://www.w3.org/2000/svg" class="mt-0.5 h-4 w-4 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
        <path d="M10.29 3.86 1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z" />
        <path d="M12 9v4" />
        <path d="M12 17h.01" />
      </svg>
      <span class="body2">
        Decoded only — the signature is <span class="font-semibold">not verified</span>, so the payload is not proof that the token is valid.
      </span>
    </div>

    <div v-if="parts" class="flex flex-col gap-default">
      <div class="card p-default">
        <div class="mb-small flex items-center justify-between gap-small">
          <p class="body2 text-medium-grey">
            Header
          </p>
          <span class="chip">
            {{ parts.algorithm }}
          </span>
        </div>
        <pre class="max-h-64 overflow-auto rounded-md border border-hairline bg-dark-deep p-default body2 font-mono text-light-grey">{{ parts.header }}</pre>
      </div>

      <div class="card p-default">
        <div class="mb-small flex items-center justify-between gap-small">
          <p class="body2 text-medium-grey">
            Payload
          </p>
          <span class="caption text-medium-grey">
            The Copy button copies this JSON
          </span>
        </div>
        <pre class="max-h-64 overflow-auto rounded-md border border-hairline bg-dark-deep p-default body2 font-mono text-light-grey">{{ parts.payload }}</pre>
      </div>

      <div class="card p-default">
        <div class="mb-small flex items-center justify-between gap-small">
          <p class="body2 text-medium-grey">
            Signature
          </p>
        </div>
        <pre class="max-h-64 overflow-auto rounded-md border border-hairline bg-dark-deep p-default body2 font-mono text-light-grey">{{ parts.signature }}</pre>
      </div>
    </div>

    <div
      v-else-if="!error && !input.trim()"
      class="rounded-md border border-dashed border-medium-grey/30 bg-dark-background/50 p-large text-center body2 text-medium-grey"
    >
      Paste a JWT above to inspect its header and payload
    </div>
  </BaseView>
</template>
