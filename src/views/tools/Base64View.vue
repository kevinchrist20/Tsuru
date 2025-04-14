<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import BaseView from '../../components/BaseView.vue'
import { getToolByRoute } from '../../models/Tool'

const route = useRoute()
const toolId = ref(route.params.id as string)
const tool = ref(getToolByRoute(toolId.value))

// State
const inputText = ref('')
const outputText = ref('')

// Process input whenever it changes
watch(inputText, (newInput) => {
  try {
    // Example: Base64 encode
    outputText.value = btoa(newInput)
  } catch (e) {
    outputText.value = 'Invalid input'
  }
})

// Handle copy event if needed
const handleCopy = (text: string) => {
  console.log('Text copied:', text)
}

// Handle clear event if needed
const handleClear = () => {
  console.log('Fields cleared')
  // Additional cleanup if necessary
}
</script>

<template>
  <BaseView 
    v-if="tool" 
    :tool="tool"
    v-model:inputText="inputText"
    v-model:outputText="outputText"
    @copy="handleCopy"
    @clear="handleClear"
  >
    <!-- Tool-specific UI here if needed -->
  </BaseView>
</template>
