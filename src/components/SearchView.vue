<script setup lang="ts">
const { modelValue, placeholder } = defineProps<{ modelValue: string, placeholder?: string }>()

const emit = defineEmits<(e: 'update:modelValue', value: string) => void>();
</script>

<template>
  <div class="w-full">
    <input type="text" :value="modelValue"
      @input="emit('update:modelValue', ($event.target as HTMLInputElement)?.value)"
      class="w-full border border-gray-300 rounded-md p-2 bg-dark-background text-white" :placeholder="placeholder" />
    <span class="absolute right-3 top-1/2 transform -translate-y-1/2">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24"
        stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
          d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
      </svg>
    </span>
  </div>
</template>
