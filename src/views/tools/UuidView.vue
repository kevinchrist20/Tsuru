<script setup lang="ts">
import type { Tool } from '../../models/Tool'
import { ref } from 'vue'
import BaseView from '../../components/BaseView.vue'
import UuidService from '../../services/generators/uuid.service'
import { extractErrorMessage } from '../../utils/error'

defineProps<{ tool: Tool }>()

const count = ref(1)
const output = ref('')
const error = ref('')
const loading = ref(false)

let requestId = 0

async function generate() {
  const id = ++requestId
  loading.value = true
  error.value = ''

  try {
    const values = await UuidService.generateMany(count.value)

    if (id !== requestId) {
      return
    }

    output.value = values.join('\n')
  }
  catch (err) {
    if (id !== requestId) {
      return
    }

    output.value = ''
    error.value = extractErrorMessage(err, 'Could not generate UUIDs.')
  }
  finally {
    if (id === requestId) {
      loading.value = false
    }
  }
}

function reset() {
  requestId += 1
  output.value = ''
  error.value = ''
  loading.value = false
}
</script>

<template>
  <BaseView
    v-model:output-text="output"
    :tool="tool"
    input-text=""
    @clear="reset"
  >
    <div class="card p-default">
      <div class="flex flex-wrap items-end justify-between gap-default">
        <div>
          <label for="uuid-count" class="mb-small block body2 text-medium-grey">
            How many?
          </label>

          <div class="relative">
            <select
              id="uuid-count"
              v-model.number="count"
              class="select w-32"
              :disabled="loading"
            >
              <option :value="1">
                1
              </option>
              <option :value="5">
                5
              </option>
              <option :value="10">
                10
              </option>
            </select>

            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="pointer-events-none absolute right-3 top-1/2 h-4 w-4 -translate-y-1/2 text-medium-grey"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
              aria-hidden="true"
            >
              <path d="m6 9 6 6 6-6" />
            </svg>
          </div>
        </div>

        <button type="button" class="btn btn-primary" :disabled="loading" @click="generate">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-4 w-4"
            :class="loading ? 'animate-spin' : ''"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            aria-hidden="true"
          >
            <path d="M21 12a9 9 0 1 1-2.64-6.36" />
            <path d="M21 3v5h-5" />
          </svg>
          {{ loading ? 'Generating…' : 'Generate' }}
        </button>
      </div>
    </div>

    <div
      v-if="error"
      class="alert-danger mt-default"
      role="alert"
    >
      <svg xmlns="http://www.w3.org/2000/svg" class="mt-0.5 h-4 w-4 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
        <path d="M10.29 3.86 1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z" />
        <path d="M12 9v4" />
        <path d="M12 17h.01" />
      </svg>
      <span>{{ error }}</span>
    </div>

    <div class="mt-default">
      <div class="mb-small flex items-center justify-between">
        <label for="uuid-output" class="block body2 text-text-color">
          UUIDs
        </label>
        <span v-if="output" class="chip">
          {{ output.split('\n').length }} generated
        </span>
      </div>

      <div
        v-if="!output && !loading"
        class="rounded-md border border-dashed border-medium-grey/30 bg-dark-background/50 p-large text-center body2 text-medium-grey"
      >
        Press <span class="font-medium text-text-color">Generate</span> to create UUIDs
      </div>

      <textarea
        v-else
        id="uuid-output"
        :value="output"
        class="field w-full resize-y font-mono"
        rows="6"
        spellcheck="false"
        readonly
      />

      <p class="caption mt-small text-medium-grey">
        Version 4 · random
      </p>
    </div>
  </BaseView>
</template>
