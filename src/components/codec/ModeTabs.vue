<script setup lang="ts">
import type { CodecMode } from '../../types/codec'

withDefaults(defineProps<{
  modelValue: CodecMode
  encodeLabel?: string
  decodeLabel?: string
}>(), {
  encodeLabel: 'Encode',
  decodeLabel: 'Decode',
})

const emit = defineEmits<{
  'update:modelValue': [mode: CodecMode]
}>()

const tabs = [
  { mode: 'encode' as const },
  { mode: 'decode' as const },
]
</script>

<template>
  <div class="flex mb-default" role="tablist" aria-label="Codec mode">
    <button
      v-for="tab in tabs"
      :key="tab.mode"
      type="button"
      role="tab"
      class="px-4 py-2 flex-1 rounded-t-md transition-colors"
      :class="modelValue === tab.mode
        ? 'bg-dark-background text-telegram-blue font-bold'
        : 'text-light-grey hover:bg-dark-background/50'"
      :aria-selected="modelValue === tab.mode"
      @click="emit('update:modelValue', tab.mode)"
    >
      {{ tab.mode === 'encode' ? encodeLabel : decodeLabel }}
    </button>
  </div>
</template>
