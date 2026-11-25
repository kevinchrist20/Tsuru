<script setup lang="ts">
import type { CodecLabels } from '../../components/codec/types'
import type { Tool } from '../../models/Tool'
import BaseView from '../../components/BaseView.vue'
import CodecPanel from '../../components/codec/CodecPanel.vue'
import { useCodec } from '../../composables/useCodec'
import Base64Service from '../../services/encoders/base64.service'

defineProps<{ tool: Tool }>()

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

const { mode, input, output, error, loading, setMode, clear } = useCodec({
  encode: Base64Service.encode,
  decode: Base64Service.decode,
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
      :labels="base64Labels"
      @update:mode="setMode"
    />
  </BaseView>
</template>
