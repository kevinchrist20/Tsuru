<script setup lang="ts">
const { modelValue, placeholder } = defineProps<{ modelValue: string, placeholder?: string }>()

const emit = defineEmits<(e: 'update:modelValue', value: string) => void>()
</script>

<template>
  <div class="relative">
    <svg
      xmlns="http://www.w3.org/2000/svg"
      class="pointer-events-none absolute left-default top-1/2 h-5 w-5 -translate-y-1/2 text-medium-grey"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      stroke-width="2"
      stroke-linecap="round"
      stroke-linejoin="round"
      aria-hidden="true"
    >
      <circle cx="11" cy="11" r="8" />
      <path d="m21 21-4.3-4.3" />
    </svg>

    <input
      type="text"
      :value="modelValue"
      class="w-full rounded-lg border border-hairline bg-dark-background py-3 pl-10 pr-10 text-text-color subtitle2 outline-none transition-colors duration-200 placeholder:text-medium-grey/60 focus:border-telegram-blue/50 focus:ring-2 focus:ring-telegram-blue/20"
      :placeholder="placeholder"
      aria-label="Search tools"
      @input="emit('update:modelValue', ($event.target as HTMLInputElement)?.value)"
    >

    <button
      v-if="modelValue"
      type="button"
      class="icon-btn absolute right-1.5 top-1/2 h-8 w-8 -translate-y-1/2"
      aria-label="Clear search"
      @click="emit('update:modelValue', '')"
    >
      <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M18 6 6 18" />
        <path d="m6 6 12 12" />
      </svg>
    </button>
  </div>
</template>
