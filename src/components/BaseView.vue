<script setup lang="ts">
import { ref, defineProps, defineEmits } from 'vue'
import { useRouter } from 'vue-router'
import { Tool } from '../models/Tool'
import { copyToClipboard } from '../utils/clipboard'

// Props for the component
const props = defineProps<{
  tool: Tool,
  inputText: string,
  outputText: string
}>()

// Emits
const emit = defineEmits(['back', 'update:inputText', 'update:outputText', 'copy', 'clear'])

// State management
const showInfoModal = ref(false)
const showToast = ref(false)
const toastMessage = ref('')

const router = useRouter()

// Method to handle back navigation
const handleBack = () => {
  emit('back')
  router.back()
}

// Toast handling
const showToastMessage = (message: string) => {
  toastMessage.value = message
  showToast.value = true
  setTimeout(() => {
    showToast.value = false
  }, 3000) // Hide after 3 seconds
}

// Action handlers
const handleCopy = async () => {
  const success = await copyToClipboard(props.outputText)
  if (success) {
    showToastMessage('Copied to clipboard')
    emit('copy', props.outputText)
  } else {
    showToastMessage('Failed to copy to clipboard')
  }
}

const handleClear = () => {
  emit('clear')
  emit('update:inputText', '')
  emit('update:outputText', '')
  showToastMessage('Cleared input and output')
}

// Input change handlers
const onInputChange = (e: Event) => {
  const target = e.target as HTMLTextAreaElement
  emit('update:inputText', target.value)
}

const onOutputChange = (e: Event) => {
  const target = e.target as HTMLTextAreaElement
  emit('update:outputText', target.value)
}
</script>

<template>
  <div class="flex flex-col min-h-screen bg-dark-shade">
    <!-- Top App Bar -->
    <header class="bg-dark-background shadow-md p-default flex items-center justify-between">
      <!-- Back button -->
      <button @click="handleBack" class="text-text-color p-micro">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
          <path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" />
        </svg>
      </button>
      
      <!-- Title -->
      <h6 class="text-text-color truncate">{{ tool.name }}</h6>
      
      <!-- Info button -->
      <button @click="showInfoModal = true" class="text-text-color p-micro">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
          <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2h.01a1 1 0 100-2H9z" clip-rule="evenodd" />
        </svg>
      </button>
    </header>
    
    <!-- Main Content Area -->
    <main class="flex-grow p-default">
      <!-- Custom content from parent -->
      <slot></slot>
      
      <!-- Default Input/Output Area -->
      <slot name="actions">
        <div class="mt-large">
          <div class="mb-default">
            <label class="block mb-small text-text-color body2">Input</label>
            <textarea 
              :value="inputText" 
              @input="onInputChange"
              class="w-full p-default bg-dark-background text-text-color rounded-md border-none outline-none focus:ring-1 focus:ring-telegram-blue resize-y"
              rows="4"
            ></textarea>
          </div>
          
          <div class="mb-default">
            <label class="block mb-small text-text-color body2">Output</label>
            <textarea 
              :value="outputText" 
              @input="onOutputChange"
              class="w-full p-default bg-dark-background text-text-color rounded-md border-none outline-none focus:ring-1 focus:ring-telegram-blue resize-y"
              rows="4"
              readonly
            ></textarea>
          </div>
          
          <div class="flex space-x-small">
            <button 
              @click="handleCopy" 
              class="bg-telegram-blue hover:bg-opacity-80 text-white py-2 px-4 rounded-md transition-colors duration-300 button"
            >
              Copy
            </button>
            <button 
              @click="handleClear" 
              class="bg-dark-background hover:bg-dark-shade border border-medium-grey text-text-color py-2 px-4 rounded-md transition-colors duration-300 button"
            >
              Clear
            </button>
            <!-- Additional tool-specific actions -->
            <slot name="additional-actions"></slot>
          </div>
        </div>
      </slot>
    </main>
    
    <!-- Bottom Bar (if needed) -->
    <footer v-if="$slots['bottom-bar']" class="bg-dark-background shadow-lg p-default">
      <slot name="bottom-bar"></slot>
    </footer>
    
    <!-- Toast Notification -->
    <transition
      enter-active-class="transition duration-300 ease-out"
      enter-from-class="transform -translate-y-2 opacity-0"
      enter-to-class="transform translate-y-0 opacity-100"
      leave-active-class="transition duration-200 ease-in"
      leave-from-class="transform translate-y-0 opacity-100"
      leave-to-class="transform -translate-y-2 opacity-0"
    >
      <div 
        v-if="showToast" 
        class="fixed top-large inset-x-0 mx-auto w-max max-w-sm bg-telegram-blue text-text-color py-2 px-4 rounded-md shadow-lg body2"
      >
        {{ toastMessage }}
      </div>
    </transition>
    
    <!-- Info Modal -->
    <div v-if="showInfoModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-default">
      <div class="bg-dark-background rounded-md shadow-lg max-w-lg w-full max-h-[80vh] overflow-auto">
        <div class="p-default border-b border-gray-700">
          <h6>{{ tool.name }}</h6>
        </div>
        
        <div class="p-default body2 markdown-content">
          <!-- Tool description -->
          <div v-html="tool.description"></div>
        </div>
        
        <div class="p-default border-t border-gray-700 flex justify-end">
          <button 
            @click="showInfoModal = false" 
            class="bg-telegram-blue hover:bg-opacity-80 text-white py-1 px-3 rounded-md transition-colors duration-300 button"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.markdown-content :deep(pre) {
  background-color: #272822;
  color: #f8f8f2;
  padding: theme('spacing.default');
  border-radius: theme('borderRadius.md');
  overflow-x: auto;
  margin: theme('spacing.default') 0;
}

.markdown-content :deep(a) {
  color: theme('colors.telegram-blue');
  text-decoration: none;
}

.markdown-content :deep(code) {
  background-color: #272822;
  color: #f8f8f2;
  padding: 0.2em 0.4em;
  border-radius: theme('borderRadius.md');
}

.markdown-content :deep(p) {
  margin-bottom: theme('spacing.default');
}

.markdown-content :deep(ul),
.markdown-content :deep(ol) {
  padding-left: theme('spacing.large');
  margin-bottom: theme('spacing.default');
}
</style>
