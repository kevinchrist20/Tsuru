<script setup lang="ts">
import type { Tool } from '../../models/Tool'
import type { CodecLabels } from '../../types/codec'
import BaseView from '../../components/BaseView.vue'
import CodecPanel from '../../components/codec/CodecPanel.vue'
import { useCodec } from '../../composables/useCodec'
import HtmlService from '../../services/encoders/html.service'

defineProps<{ tool: Tool }>()

const htmlLabels: CodecLabels = {
  encode: {
    tab: 'Encode',
    input: 'Input',
    output: 'Output HTML entities',
  },
  decode: {
    tab: 'Decode',
    input: 'Input HTML entities',
    output: 'Output',
  },
}

const { mode, input, output, error, loading, setMode, clear } = useCodec({
  encode: value => HtmlService.encode(value),
  decode: value => HtmlService.decode(value),
})
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
      :labels="htmlLabels"
      input-mono
      output-mono
      @update:mode="setMode"
    />
  </BaseView>
</template>
