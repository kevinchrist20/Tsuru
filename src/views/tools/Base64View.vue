<script setup lang="ts">
import type { Tool } from '../../models/Tool'
import type { CodecLabels } from '../../types/codec'
import { ref, watch } from 'vue'
import BaseView from '../../components/BaseView.vue'
import CodecPanel from '../../components/codec/CodecPanel.vue'
import { useCodec } from '../../composables/useCodec'
import Base64Service from '../../services/encoders/base64.service'

defineProps<{ tool: Tool }>()

const urlSafe = ref(false)
const padding = ref(true)

const base64Labels: CodecLabels = {
  encode: {
    tab: 'Encode',
    input: 'Input',
    output: 'Output Base64',
  },
  decode: {
    tab: 'Decode',
    input: 'Input Base64',
    output: 'Output String',
  },
}

const { mode, input, output, error, loading, setMode, clear, refresh } = useCodec({
  encode: value => Base64Service.encode(value, {
    urlSafe: urlSafe.value,
    padding: padding.value,
  }),
  decode: value => Base64Service.decode(value, {
    urlSafe: urlSafe.value,
    padding: padding.value,
  }),
})

watch([urlSafe, padding], () => refresh())
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
      :mode="mode"
      :output="output"
      :error="error"
      :loading="loading"
      :labels="base64Labels"
      @update:mode="setMode"
    >
      <template #options>
        <div class="flex flex-wrap items-center gap-large mb-default body2">
          <label class="flex items-center gap-small text-text-color cursor-pointer">
            <input
              v-model="urlSafe"
              type="checkbox"
              class="accent-telegram-blue"
            >
            URL-safe alphabet
          </label>
          <label class="flex items-center gap-small text-text-color cursor-pointer">
            <input
              v-model="padding"
              type="checkbox"
              class="accent-telegram-blue"
            >
            Padding
          </label>
        </div>
      </template>
    </CodecPanel>
  </BaseView>
</template>
