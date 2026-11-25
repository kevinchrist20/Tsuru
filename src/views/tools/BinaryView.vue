<script setup lang="ts">
import type { Tool } from '../../models/Tool'
import type { CodecLabels } from '../../types/codec'
import BaseView from '../../components/BaseView.vue'
import CodecPanel from '../../components/codec/CodecPanel.vue'
import { useCodec } from '../../composables/useCodec'
import BinaryService from '../../services/encoders/binary.service'

defineProps<{ tool: Tool }>()

const binaryLabels: CodecLabels = {
  encode: {
    tab: 'Encode',
    input: 'Input',
    output: 'Output binary',
  },
  decode: {
    tab: 'Decode',
    input: 'Input binary',
    output: 'Output',
  },
}

const { mode, input, output, error, loading, setMode, clear } = useCodec({
  encode: value => BinaryService.encode(value),
  decode: value => BinaryService.decode(value),
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
      :labels="binaryLabels"
      input-mono
      output-mono
      @update:mode="setMode"
    />
  </BaseView>
</template>
