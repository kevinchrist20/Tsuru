<script setup lang="ts">
import { computed, useId } from 'vue'

const props = defineProps<{
  input: string
  output: string
  inputLabel: string
  outputLabel: string
  error?: string
  loading?: boolean
  inputPlaceholder?: string
  inputRows?: number
  outputRows?: number
}>()

const emit = defineEmits<{
  'update:input': [value: string]
}>()

const inputId = useId()
const outputId = useId()
const charCount = computed(() => props.input.length.toLocaleString())
</script>

<template>
  <div>
    <div class="mb-default">
      <div class="flex items-center justify-between mb-small">
        <label class="block text-text-color body2" :for="inputId">
          {{ inputLabel }}
        </label>
        <span class="caption text-medium-grey">
          {{ charCount }} chars
        </span>
      </div>
      <textarea
        :id="inputId"
        :value="input"
        class="w-full p-default bg-dark-background text-text-color rounded-md border-none outline-none focus:ring-1 focus:ring-telegram-blue resize-y"
        :rows="inputRows ?? 6"
        :placeholder="inputPlaceholder"
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
        <label class="block text-text-color body2" :for="outputId">
          {{ outputLabel }}
        </label>
        <span v-if="loading" class="caption text-medium-grey">
          Processing…
        </span>
      </div>
      <textarea
        :id="outputId"
        :value="output"
        class="w-full p-default bg-dark-background text-text-color rounded-md border-none outline-none focus:ring-1 focus:ring-telegram-blue resize-y"
        :rows="outputRows ?? 6"
        spellcheck="false"
        readonly
      />
    </div>
  </div>
</template>
