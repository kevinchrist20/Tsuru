<script setup lang="ts">
import type { CodecLabels, CodecMode } from '../../types/codec'
import { computed } from 'vue'
import ModeTabs from './ModeTabs.vue'
import ValuePanel from './ValuePanel.vue'

const props = defineProps<{
  mode: CodecMode
  input: string
  output: string
  labels: CodecLabels
  error?: string
  loading?: boolean
  inputMono?: boolean
  outputMono?: boolean
}>()

const emit = defineEmits<{
  'update:mode': [mode: CodecMode]
  'update:input': [value: string]
}>()

const activeLabels = computed(() => props.labels[props.mode])
</script>

<template>
  <div>
    <ModeTabs
      :model-value="mode"
      :encode-label="labels.encode.tab"
      :decode-label="labels.decode.tab"
      @update:model-value="emit('update:mode', $event)"
    />

    <slot name="options" />

    <ValuePanel
      :input="input"
      :output="output"
      :input-label="activeLabels.input"
      :output-label="activeLabels.output"
      :error="error"
      :loading="loading"
      :input-mono="inputMono"
      :output-mono="outputMono"
      @update:input="emit('update:input', $event)"
    />
  </div>
</template>
