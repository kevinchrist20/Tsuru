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
  <div class="mb-default flex rounded-lg border border-hairline bg-dark-background p-1" role="tablist" aria-label="Codec mode">
    <button
      v-for="tab in tabs"
      :key="tab.mode"
      type="button"
      role="tab"
      class="flex-1 rounded-md px-4 py-1.5 text-sm font-medium transition-colors duration-200"
      :class="modelValue === tab.mode
        ? 'bg-telegram-blue text-white'
        : 'text-medium-grey hover:bg-white/5 hover:text-text-color'"
      :aria-selected="modelValue === tab.mode"
      @click="emit('update:modelValue', tab.mode)"
    >
      {{ tab.mode === 'encode' ? encodeLabel : decodeLabel }}
    </button>
  </div>
</template>
