<script setup lang="ts">
import type { Tool } from '../../models/Tool'
import type { CodecLabels } from '../../types/codec'
import BaseView from '../../components/BaseView.vue'
import CodecPanel from '../../components/codec/CodecPanel.vue'
import { useCodec } from '../../composables/useCodec'
import QuotedPrintableService from '../../services/encoders/quoted-printable.service'

defineProps<{ tool: Tool }>()

const quotedPrintableLabels: CodecLabels = {
  encode: {
    tab: 'Encode',
    input: 'Input',
    output: 'Output quoted-printable',
  },
  decode: {
    tab: 'Decode',
    input: 'Input quoted-printable',
    output: 'Output',
  },
}

const { mode, input, output, error, loading, setMode, clear } = useCodec({
  encode: value => QuotedPrintableService.encode(value),
  decode: value => QuotedPrintableService.decode(value),
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
      :labels="quotedPrintableLabels"
      input-mono
      output-mono
      @update:mode="setMode"
    />
  </BaseView>
</template>
