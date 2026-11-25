<script setup lang="ts">
import type { Tool } from '../../models/Tool'
import type { CodecLabels } from '../../types/codec'
import { ref, watch } from 'vue'
import BaseView from '../../components/BaseView.vue'
import CodecPanel from '../../components/codec/CodecPanel.vue'
import { useCodec } from '../../composables/useCodec'
import HexService from '../../services/encoders/hex.service'

defineProps<{ tool: Tool }>()

const uppercase = ref(false)

const hexLabels: CodecLabels = {
  encode: {
    tab: 'Encode',
    input: 'Input',
    output: 'Output hex',
  },
  decode: {
    tab: 'Decode',
    input: 'Input hex',
    output: 'Output',
  },
}

const { mode, input, output, error, loading, setMode, clear, refresh } = useCodec({
  encode: value => HexService.encode(value, uppercase.value),
  decode: value => HexService.decode(value),
})

watch(uppercase, () => refresh())
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
      :labels="hexLabels"
      input-mono
      output-mono
      @update:mode="setMode"
    >
      <template #options>
        <div class="mb-default flex flex-wrap items-center gap-small">
          <label class="cursor-pointer">
            <input v-model="uppercase" type="checkbox" class="peer sr-only">
            <span class="chip cursor-pointer select-none px-3 py-1 text-xs transition-colors duration-200 peer-checked:border-telegram-blue/40 peer-checked:bg-telegram-blue/15 peer-checked:text-telegram-blue peer-focus-visible:ring-2 peer-focus-visible:ring-telegram-blue/50">
              Uppercase
            </span>
          </label>
        </div>
      </template>
    </CodecPanel>
  </BaseView>
</template>
