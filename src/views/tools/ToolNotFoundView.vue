<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<template>
  <div class="grid min-h-screen place-items-center p-default">
    <div class="card w-full max-w-md p-large text-center">
      <div class="icon-tile mx-auto mb-default h-12 w-12">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <circle cx="11" cy="11" r="8" />
          <path d="m21 21-4.3-4.3" />
        </svg>
      </div>

      <h5 class="text-text-color">
        Tool not found
      </h5>
      <p class="body2 mt-1 text-medium-grey">
        The tool you are looking for does not exist.
      </p>

      <button type="button" class="btn btn-primary mt-default" @click="router.push('/')">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="m15 18-6-6 6-6" />
        </svg>
        Back to tools
      </button>
    </div>
  </div>
</template>
