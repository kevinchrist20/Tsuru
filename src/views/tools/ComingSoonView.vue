<script setup lang="ts">
import type { Tool } from '../../models/Tool'
import { useRouter } from 'vue-router'
import BaseView from '../../components/BaseView.vue'

defineProps<{ tool: Tool }>()

const router = useRouter()
</script>

<template>
  <BaseView :tool="tool" input-text="" output-text="">
    <div class="bg-dark-background rounded-md p-large text-center">
      <h5 class="text-text-color mb-small">
        Coming soon
      </h5>
      <p class="body2 text-medium-grey max-w-lg mx-auto">
        {{ tool.sub }}
      </p>
      <p class="caption text-medium-grey mt-default">
        This tool is part of the Encoders &amp; Generators migration and is not available yet.
      </p>
    </div>

    <template #actions>
      <div class="mt-large">
        <button
          class="bg-telegram-blue hover:bg-telegram-blue/80 text-white py-2 px-4 rounded-md transition-colors duration-300 button"
          @click="router.push('/')"
        >
          Back to tools
        </button>
      </div>
    </template>
  </BaseView>
</template>
