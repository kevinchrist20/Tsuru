<script setup lang="ts">
import type { Tool } from '../../models/Tool'
import type { UrlEncodeMode } from '../../services/encoders/url.service'
import type { CodecLabels } from '../../types/codec'
import { ref, watch } from 'vue'
import BaseView from '../../components/BaseView.vue'
import CodecPanel from '../../components/codec/CodecPanel.vue'
import { useCodec } from '../../composables/useCodec'
import UrlService from '../../services/encoders/url.service'

defineProps<{ tool: Tool }>()

const mode = ref<UrlEncodeMode>('component')

const urlLabels: CodecLabels = {
  encode: {
    tab: 'Encode',
    input: 'Input',
    output: 'Output URL-encoded',
  },
  decode: {
    tab: 'Decode',
    input: 'Input URL-encoded',
    output: 'Output',
  },
}

const {
  mode: codecMode,
  input,
  output,
  error,
  loading,
  setMode,
  clear,
  refresh,
} = useCodec({
  encode: value => UrlService.encode(value, mode.value),
  decode: value => UrlService.decode(value),
})

watch(mode, () => refresh())
</script>

<template>
  <BaseView
    v-model:input-text="input"
    v-model:output-text="output"
    :tool="tool"
    @clear="clear"
  >
    <CodecPanel
      v-model:input="input"
      :mode="codecMode"
      :output="output"
      :error="error"
      :loading="loading"
      :labels="urlLabels"
      input-mono
      output-mono
      @update:mode="setMode"
    >
      <template #options>
        <div v-if="codecMode === 'encode'" class="mb-default flex flex-wrap items-center gap-default">
          <label for="url-mode" class="body2 text-medium-grey">
            Mode
          </label>

          <div class="relative">
            <select id="url-mode" v-model="mode" class="select w-36">
              <option value="component">
                Component
              </option>
              <option value="url">
                Full URL
              </option>
            </select>

            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="pointer-events-none absolute right-3 top-1/2 h-4 w-4 -translate-y-1/2 text-medium-grey"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
              aria-hidden="true"
            >
              <path d="m6 9 6 6 6-6" />
            </svg>
          </div>

          <p class="caption text-medium-grey">
            Component matches encodeURIComponent · Full URL matches encodeURI
          </p>
        </div>
      </template>
    </CodecPanel>
  </BaseView>
</template>
