<script setup lang="ts">
import type { Tool } from '../../models/Tool'
import type { CodecLabels } from '../../types/codec'
import BaseView from '../../components/BaseView.vue'
import CodecPanel from '../../components/codec/CodecPanel.vue'
import { useCodec } from '../../composables/useCodec'
import PunycodeService from '../../services/encoders/punycode.service'

defineProps<{ tool: Tool }>()

const punycodeLabels: CodecLabels = {
  encode: {
    tab: 'Encode',
    input: 'Domain (Unicode)',
    output: 'Punycode (xn--)',
  },
  decode: {
    tab: 'Decode',
    input: 'Domain (punycode)',
    output: 'Unicode',
  },
}

const { mode, input, output, error, loading, setMode, clear } = useCodec({
  encode: value => PunycodeService.encode(value),
  decode: value => PunycodeService.decode(value),
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
      :labels="punycodeLabels"
      input-mono
      output-mono
      @update:mode="setMode"
    />
  </BaseView>
</template>
