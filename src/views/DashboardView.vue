<script setup lang="ts">
import type { Tool } from '../models/Tool'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import SearchView from '../components/SearchView.vue'
import ToolCard from '../components/ToolCard.vue'
import { getTools } from '../models/Tool'

const searchValue = ref('')
const tools = ref<Tool[]>(getTools())
const router = useRouter()

function navigateToTool(tool: Tool) {
  router.push(`/tools/${tool.route}`)
}
</script>

<template>
  <div class="p-default min-h-screen">
    <div class="max-w-6xl mx-auto">
      <SearchView v-model="searchValue" placeholder="Search tools..." class="relative" />

      <div class="h-large" />

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-default">
        <ToolCard
          v-for="tool in tools"
          :key="tool.route"
          :tool="tool"
          @click="navigateToTool(tool)"
        />
      </div>

      <div class="h-large" />
    </div>
  </div>
</template>
