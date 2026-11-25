<script setup lang="ts">
import type { Tool } from '../../models/Tool'
import { ref, watch } from 'vue'
import BaseView from '../../components/BaseView.vue'
import ValuePanel from '../../components/codec/ValuePanel.vue'
import SlugService from '../../services/generators/slug.service'
import { extractErrorMessage } from '../../utils/error'

defineProps<{ tool: Tool }>()

const input = ref('')
const output = ref('')
const error = ref('')
const loading = ref(false)

let timer: ReturnType<typeof setTimeout> | undefined
let requestId = 0

async function run(value: string) {
  const id = ++requestId
  loading.value = true
  error.value = ''

  try {
    const result = await SlugService.generate(value)
    if (id !== requestId) {
      return
    }
    output.value = result
  }
  catch (err) {
    if (id !== requestId) {
      return
    }
    output.value = ''
    error.value = extractErrorMessage(err, 'Could not generate a slug.')
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

  if (value.length === 0) {
    output.value = ''
    error.value = ''
    return
  }

  timer = setTimeout(() => {
    timer = undefined
    void run(value)
  }, 150)
}

watch(input, schedule)

function reset() {
  if (timer) {
    clearTimeout(timer)
    timer = undefined
  }
  requestId += 1
  input.value = ''
  output.value = ''
  error.value = ''
  loading.value = false
}
</script>

<template>
  <BaseView
    v-model:input-text="input"
    v-model:output-text="output"
    :tool="tool"
    @clear="reset"
  >
    <ValuePanel
      v-model:input="input"
      :output="output"
      input-label="Text"
      output-label="Slug"
      input-placeholder="Type or paste text to slugify…"
      :input-rows="5"
      :output-rows="5"
      :error="error"
      :loading="loading"
    />
  </BaseView>
</template>
