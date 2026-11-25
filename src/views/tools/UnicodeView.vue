<script setup lang="ts">
import type { Tool } from '../../models/Tool'
import type { CodecLabels } from '../../types/codec'
import BaseView from '../../components/BaseView.vue'
import CodecPanel from '../../components/codec/CodecPanel.vue'
import { useCodec } from '../../composables/useCodec'
import UnicodeService from '../../services/encoders/unicode.service'

defineProps<{ tool: Tool }>()

const unicodeLabels: CodecLabels = {
  encode: {
    tab: 'Encode',
    input: 'Input',
    output: 'Output escapes',
  },
  decode: {
    tab: 'Decode',
    input: 'Input escapes',
    output: 'Output',
  },
}

const { mode, input, output, error, loading, setMode, clear } = useCodec({
  encode: value => UnicodeService.encode(value),
  decode: value => UnicodeService.decode(value),
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
      :labels="unicodeLabels"
      input-mono
      output-mono
      @update:mode="setMode"
    />
  </BaseView>
</template>
