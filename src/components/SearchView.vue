<script setup lang="ts">
const { modelValue, placeholder } = defineProps<{ modelValue: string, placeholder?: string }>()

const emit = defineEmits<(e: 'update:modelValue', value: string) => void>()
</script>

<template>
  <div class="w-full bg-dark-background rounded-md shadow-sm">
    <div class="relative w-full">
      <input
        type="text" :value="modelValue"
        class="w-full p-default rounded-md bg-dark-background text-text-color subtitle2 border-none outline-none focus:ring-1 focus:ring-telegram-blue"
        :placeholder="placeholder"
        @input="emit('update:modelValue', ($event.target as HTMLInputElement)?.value)"
      >
      <span class="absolute right-default top-1/2 transform -translate-y-1/2">
        <svg
          xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-medium-grey" fill="none" viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path
            stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
            d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
          />
        </svg>
      </span>
    </div>
  </div>
</template>

<style scoped>
input::placeholder {
  color: var(--color-medium-grey);
  font-size: var(--text-subtitle2);
}
</style>
