<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<template>
  <div class="min-h-screen bg-dark-shade flex items-center justify-center p-default">
    <div class="bg-dark-background rounded-md p-large text-center max-w-md w-full">
      <h5 class="text-text-color mb-small">
        Tool not found
      </h5>
      <p class="body2 text-medium-grey mb-default">
        The tool you are looking for does not exist.
      </p>
      <button
        class="bg-telegram-blue hover:bg-telegram-blue/80 text-white py-2 px-4 rounded-md transition-colors duration-300 button"
        @click="router.push('/')"
      >
        Back to tools
      </button>
    </div>
  </div>
</template>
