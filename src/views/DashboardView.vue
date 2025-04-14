<template>
  <div class="p-default min-h-screen">
    <div class="max-w-6xl mx-auto">
      <SearchView v-model="searchValue" placeholder="Search tools..." class="relative" />
      
      <div class="h-large"></div>
      
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-default">
        <ToolCard 
          v-for="tool in tools" 
          :key="tool.route" 
          :tool="tool" 
          @click="navigateToTool(tool)"
        />
      </div>
      
      <div class="h-large"></div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useRouter } from 'vue-router'
import SearchView from '../components/SearchView.vue'
import ToolCard from '../components/ToolCard.vue'
import { Tool, getTools } from '../models/Tool'

export default defineComponent({
  components: {
    SearchView,
    ToolCard
  },
  setup() {
    const searchValue = ref('')
    const tools = ref<Tool[]>(getTools())
    const router = useRouter()
    
    const navigateToTool = (tool: Tool) => {
      router.push(`/tool/${tool.route}`)
    }
    
    return {
      searchValue,
      tools,
      navigateToTool
    }
  }
})
</script>
