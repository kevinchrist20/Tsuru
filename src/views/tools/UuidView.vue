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
    <div class="bg-dark-background rounded-md p-default mb-default">
      <div class="flex flex-wrap items-end gap-default">
        <div>
          <label for="uuid-count" class="block mb-small text-text-color body2">
            How many?
          </label>
          <select
            id="uuid-count"
            v-model.number="count"
            class="bg-dark-background text-text-color border border-medium-grey rounded-md p-small outline-none focus:ring-1 focus:ring-telegram-blue body2"
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
        </div>

        <button
          type="button"
          class="bg-telegram-blue hover:bg-telegram-blue/80 text-white py-2 px-4 rounded-md transition-colors duration-300 button disabled:opacity-50 disabled:cursor-not-allowed"
          :disabled="loading"
          @click="generate"
        >
          {{ loading ? 'Generating…' : 'Generate' }}
        </button>
      </div>
    </div>

    <div
      v-if="error"
      class="mb-default p-default bg-red-900/50 text-red-300 rounded-md body2"
      role="alert"
    >
      {{ error }}
    </div>

    <div class="mb-default">
      <label for="uuid-output" class="block mb-small text-text-color body2">
        UUIDs
      </label>
      <textarea
        id="uuid-output"
        :value="output"
        class="w-full p-default bg-dark-background text-text-color rounded-md border-none outline-none focus:ring-1 focus:ring-telegram-blue resize-y font-mono"
        rows="8"
        spellcheck="false"
        readonly
      />
      <p class="caption text-medium-grey mt-small">
        Generated UUIDs use version 4 (random).
      </p>
    </div>
  </BaseView>
</template>
