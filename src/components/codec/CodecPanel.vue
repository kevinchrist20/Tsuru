<script setup lang="ts">
import type { CodecMode } from '../../composables/useCodec'
import type { CodecLabels } from './types'
import { computed } from 'vue'

const props = defineProps<{
  mode: CodecMode
  input: string
  output: string
  labels: CodecLabels
  error?: string
  loading?: boolean
}>()

const emit = defineEmits<{
  'update:mode': [mode: CodecMode]
  'update:input': [value: string]
}>()

const tabs = computed(() => [
  { mode: 'encode' as const, label: props.labels.encode.tab },
  { mode: 'decode' as const, label: props.labels.decode.tab },
])

const activeLabels = computed(() => props.labels[props.mode])
const charCount = computed(() => props.input.length.toLocaleString())
</script>

<template>
  <div>
    <div class="flex mb-default" role="tablist" aria-label="Codec mode">
      <button
        v-for="tab in tabs"
        :key="tab.mode"
        type="button"
        role="tab"
        class="px-4 py-2 flex-1 rounded-t-md transition-colors"
        :class="mode === tab.mode
          ? 'bg-dark-background text-telegram-blue font-bold'
          : 'text-light-grey hover:bg-dark-background/50'"
        :aria-selected="mode === tab.mode"
        @click="emit('update:mode', tab.mode)"
      >
        {{ tab.label }}
      </button>
    </div>

    <slot name="options" />

    <div class="mb-default">
      <div class="flex items-center justify-between mb-small">
        <label class="block text-text-color body2" :for="`codec-input-${mode}`">
          {{ activeLabels.input }}
        </label>
        <span class="caption text-medium-grey">
          {{ charCount }} chars
        </span>
      </div>
      <textarea
        :id="`codec-input-${mode}`"
        :value="input"
        class="w-full p-default bg-dark-background text-text-color rounded-md border-none outline-none focus:ring-1 focus:ring-telegram-blue resize-y"
        rows="6"
        spellcheck="false"
        autocomplete="off"
        @input="emit('update:input', ($event.target as HTMLTextAreaElement).value)"
      />
    </div>

    <div
      v-if="error"
      class="mb-default p-default bg-red-900/50 text-red-300 rounded-md body2"
      role="alert"
    >
      {{ error }}
    </div>

    <div class="mb-default">
      <div class="flex items-center justify-between mb-small">
        <label class="block text-text-color body2" :for="`codec-output-${mode}`">
          {{ activeLabels.output }}
        </label>
        <span v-if="loading" class="caption text-medium-grey">
          Processing…
        </span>
      </div>
      <textarea
        :id="`codec-output-${mode}`"
        :value="output"
        class="w-full p-default bg-dark-background text-text-color rounded-md border-none outline-none focus:ring-1 focus:ring-telegram-blue resize-y"
        rows="6"
        spellcheck="false"
        readonly
      />
    </div>
  </div>
</template>
