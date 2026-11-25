<script setup lang="ts">
import type { Tool } from '../models/Tool'
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import SearchView from '../components/SearchView.vue'
import ToolCard from '../components/ToolCard.vue'
import { getTools, ToolCategory } from '../models/Tool'

const searchValue = ref('')
const router = useRouter()

const categoryOrder: ToolCategory[] = [
  ToolCategory.EncodeDecode,
  ToolCategory.Generators,
  ToolCategory.Converters,
  ToolCategory.Cryptography,
]

interface ToolGroup {
  category: ToolCategory
  tools: { tool: Tool, index: number }[]
}

const groups = computed<ToolGroup[]>(() => {
  const query = searchValue.value.trim().toLowerCase()
  const filtered = getTools().filter((tool) => {
    if (!query) {
      return true
    }
    return [
      tool.name,
      tool.sub,
      tool.description,
      tool.category,
    ]
      .some(value => value.toLowerCase().includes(query))
  })

  let index = 0

  return categoryOrder
    .map(category => ({
      category,
      tools: filtered
        .filter(tool => tool.category === category)
        .map(tool => ({ tool, index: index++ })),
    }))
    .filter(group => group.tools.length > 0)
})

function navigateToTool(tool: Tool) {
  router.push(`/tools/${tool.route}`)
}

function clearSearch() {
  searchValue.value = ''
}
</script>

<template>
  <div class="min-h-screen px-default pb-large pt-large">
    <div class="mx-auto w-full max-w-5xl">
      <header class="flex flex-col gap-large">
        <div class="flex items-center gap-default">
          <div
            class="grid h-11 w-11 shrink-0 place-items-center rounded-lg border border-telegram-blue/25 bg-telegram-blue/10 text-xl text-telegram-blue"
            aria-hidden="true"
          >
            鶴
          </div>

          <div class="min-w-0">
            <h1 class="text-2xl font-bold text-text-color">
              Tsuru
            </h1>
            <p class="caption text-medium-grey">
              A toolbox of everyday developer utilities
            </p>
          </div>
        </div>

        <SearchView v-model="searchValue" placeholder="Search tools…" />
      </header>

      <section v-for="group in groups" :key="group.category" class="mt-large">
        <div class="mb-small flex items-center gap-small">
          <h5 class="text-sm font-semibold text-text-color">
            {{ group.category }}
          </h5>
          <span class="chip">
            {{ group.tools.length }}
          </span>
          <div class="rule" />
        </div>

        <div class="grid grid-cols-1 gap-default md:grid-cols-2 lg:grid-cols-3">
          <ToolCard
            v-for="item in group.tools"
            :key="item.tool.route"
            :tool="item.tool"
            :style="{ '--i': item.index }"
            @click="navigateToTool(item.tool)"
          />
        </div>
      </section>

      <div v-if="groups.length === 0" class="card mx-auto mt-large max-w-md p-large text-center">
        <div class="icon-tile mx-auto mb-default h-12 w-12">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="11" cy="11" r="8" />
            <path d="m21 21-4.3-4.3" />
          </svg>
        </div>
        <p class="text-sm font-semibold text-text-color">
          No tools found
        </p>
        <p class="body2 mt-1 text-medium-grey">
          Nothing matches “{{ searchValue }}”.
        </p>
        <button type="button" class="btn btn-ghost mt-default" @click="clearSearch">
          Clear search
        </button>
      </div>

      <div class="h-large" />
    </div>
  </div>
</template>
