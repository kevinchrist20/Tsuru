<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import BaseView from '../../components/BaseView.vue'
import { getToolByRoute } from '../../models/Tool'

import Base64Service from '../../services/base64.service'

const route = useRoute()
const toolId = ref(route.params.id as string)
const tool = ref(getToolByRoute(toolId.value))

const inputText = ref('')
const outputText = ref('')
const selectedTab = ref(0) // 0 for encode, 1 for decode
const encodeInput = ref('')
const encodeOutput = ref('')
const decodeInput = ref('')
const decodeOutput = ref('')
const decodingError = ref('')

// Watch for tab changes to update the main input/output
watch(selectedTab, (newTab) => {
  if (newTab === 0) {
    // Encode tab
    inputText.value = encodeInput.value
    outputText.value = encodeOutput.value
  }
  else {
    // Decode tab
    inputText.value = decodeInput.value
    outputText.value = decodeOutput.value
  }
})

// Handle input changes based on active tab
async function onInputChange(event: Event) {
  const value = (event.target as HTMLTextAreaElement).value
  inputText.value = value

  if (selectedTab.value === 0) {
    // Encode tab
    encodeInput.value = value
    try {
      encodeOutput.value = await Base64Service.encode(value)
      outputText.value = encodeOutput.value
    }
    catch {
      encodeOutput.value = 'Error encoding input'
      outputText.value = encodeOutput.value
    }
  }
  else {
    // Decode tab
    decodeInput.value = value
    try {
      decodeOutput.value = await Base64Service.decode(value)
      outputText.value = decodeOutput.value
      decodingError.value = ''
    }
    catch {
      decodingError.value = 'Invalid Base64 input'
      decodeOutput.value = ''
      outputText.value = ''
    }
  }
}

// Handle output changes (may be needed for two-way binding)
function onOutputChange(event: Event) {
  const value = (event.target as HTMLTextAreaElement).value
  outputText.value = value
}

// Handle copy event
function handleCopy(text: string) {
  navigator.clipboard.writeText(text)
}

// Handle clear event
function handleClear() {
  if (selectedTab.value === 0) {
    // Clear encode fields
    encodeInput.value = ''
    encodeOutput.value = ''
  }
  else {
    // Clear decode fields
    decodeInput.value = ''
    decodeOutput.value = ''
    decodingError.value = ''
  }

  inputText.value = ''
  outputText.value = ''
}

// Change tabs
function changeTab(tabIndex: number) {
  selectedTab.value = tabIndex
  if (tabIndex === 0) {
    inputText.value = encodeInput.value
    outputText.value = encodeOutput.value
  }
  else {
    inputText.value = decodeInput.value
    outputText.value = decodeOutput.value
  }
}
</script>

<template>
  <BaseView
    v-if="tool"
    v-model:input-text="inputText"
    v-model:output-text="outputText"
    :tool="tool"
    @copy="handleCopy"
    @clear="handleClear"
  >
    <!-- Tab navigation -->
    <div class="flex mb-default">
      <button
        class="px-4 py-2 flex-1"
        :class="selectedTab === 0 ? 'bg-dark-background text-telegram-blue font-bold' : 'text-light-grey'"
        @click="changeTab(0)"
      >
        Encode
      </button>
      <button
        class="px-4 py-2 flex-1"
        :class="selectedTab === 1 ? 'bg-dark-background text-telegram-blue font-bold' : 'text-light-grey'"
        @click="changeTab(1)"
      >
        Decode
      </button>
    </div>

    <!-- Input and Output fields -->
    <div>
      <div class="mb-default">
        <label class="block mb-small text-text-color body2">
          {{ selectedTab === 0 ? 'Input' : 'Input Base64' }}
        </label>
        <textarea
          :value="inputText"
          class="w-full p-default bg-dark-background text-text-color rounded-md border-none outline-none focus:ring-1 focus:ring-telegram-blue resize-y"
          rows="4"
          @input="onInputChange"
        />
      </div>

      <!-- Error message for decoding -->
      <div v-if="selectedTab === 1 && decodingError" class="mb-default p-default bg-red-900/50 text-red-300 rounded-md">
        {{ decodingError }}
      </div>

      <div class="mb-default">
        <label class="block mb-small text-text-color body2">
          {{ selectedTab === 0 ? 'Output Base64' : 'Output String' }}
        </label>
        <textarea
          :value="outputText"
          class="w-full p-default bg-dark-background text-text-color rounded-md border-none outline-none focus:ring-1 focus:ring-telegram-blue resize-y"
          rows="4"
          readonly
          @input="onOutputChange"
        />
      </div>
    </div>
  </BaseView>
</template>
