<script setup lang="ts">
import { Tool } from '../models/Tool'

const { tool } = defineProps<{ tool: Tool }>()

const emit = defineEmits<(e: 'click') => void>()
</script>

<template>
  <div class="bg-dark-background hover:bg-dark-shade transition-colors duration-300 rounded-md shadow-md cursor-pointer"
       style="width: 350px; height: 200px;"
       @click="emit('click')">
    <div class="p-default h-full flex flex-col">
      <div class="h-default"></div>
      <h6 class="text-left">{{ tool.name }}</h6>
      <div class="h-small"></div>
      <p class="body2 text-medium-grey text-left">{{ tool.sub }}</p>
    </div>
  </div>
</template>
