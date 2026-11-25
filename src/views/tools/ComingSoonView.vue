<script setup lang="ts">
import type { Tool } from '../../models/Tool'
import { useRouter } from 'vue-router'
import BaseView from '../../components/BaseView.vue'

defineProps<{ tool: Tool }>()

const router = useRouter()
</script>

<template>
  <BaseView :tool="tool" input-text="" output-text="">
    <div class="grid min-h-[45vh] place-items-center">
      <div class="card w-full max-w-md p-large text-center">
        <div class="icon-tile mx-auto mb-default h-12 w-12">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="10" />
            <path d="M12 6v6l4 2" />
          </svg>
        </div>

        <h5 class="text-text-color">
          Coming soon
        </h5>
        <p class="body2 mt-1 text-medium-grey">
          {{ tool.sub }}
        </p>
        <p class="caption mt-default text-medium-grey">
          This tool is part of the migration roadmap.
        </p>
      </div>
    </div>

    <template #actions>
      <button type="button" class="btn btn-primary" @click="router.push('/')">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="m15 18-6-6 6-6" />
        </svg>
        Back to tools
      </button>
    </template>
  </BaseView>
</template>
