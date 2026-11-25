<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { getToolByRoute } from '../../models/Tool'
import ComingSoonView from './ComingSoonView.vue'
import ToolNotFoundView from './ToolNotFoundView.vue'
import { toolViews } from './toolViews'

const route = useRoute()
const tool = computed(() => getToolByRoute(route.params.id as string))
const view = computed(() => (tool.value ? toolViews[tool.value.route] : undefined))
</script>

<template>
  <component :is="view" v-if="view && tool" :key="tool.route" :tool="tool" />
  <ComingSoonView v-else-if="tool" :tool="tool" />
  <ToolNotFoundView v-else />
</template>
