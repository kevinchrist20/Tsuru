<template>
  <div 
    class="bg-dark-background rounded-micro p-default shadow-md cursor-pointer w-[350px] h-[200px]"
    @click="$emit('click')"
  >
    <div class="p-default h-full flex flex-col">
      <div class="h-default"></div>
      <h3 class="text-lg font-semibold text-left">{{ tool.name }}</h3>
      <div class="h-small"></div>
      <p class="text-sm text-medium-grey text-left">{{ tool.sub }}</p>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import { Tool } from '@/models/Tool'

export default defineComponent({
  props: {
    tool: {
      type: Object as PropType<Tool>,
      required: true
    }
  },
  emits: ['click']
})
</script>
