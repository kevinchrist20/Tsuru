<script setup lang="ts">
import type { Tool } from '../models/Tool'
import { toolIcons } from '../models/toolIcons'

const { tool } = defineProps<{ tool: Tool }>()

const emit = defineEmits<(e: 'click') => void>()
</script>

<template>
  <button
    type="button"
    class="tool-card"
    @click="emit('click')"
  >
    <div class="flex items-start gap-small">
      <span class="icon-tile" aria-hidden="true">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="h-5 w-5"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <path v-for="d in toolIcons[tool.route]" :key="d" :d="d" />
        </svg>
      </span>

      <div class="min-w-0 flex-1">
        <p class="truncate text-sm font-semibold text-text-color">
          {{ tool.name }}
        </p>
        <p class="mt-1 line-clamp-2 body2 text-medium-grey">
          {{ tool.sub }}
        </p>
      </div>
    </div>

    <div class="mt-default flex items-center justify-between gap-small">
      <span class="chip truncate">
        {{ tool.category }}
      </span>

      <svg
        xmlns="http://www.w3.org/2000/svg"
        class="h-4 w-4 shrink-0 text-medium-grey transition-all duration-200 group-hover:translate-x-0.5 group-hover:text-telegram-blue"
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        stroke-width="2"
        stroke-linecap="round"
        stroke-linejoin="round"
        aria-hidden="true"
      >
        <path d="m9 6 6 6-6 6" />
      </svg>
    </div>
  </button>
</template>
