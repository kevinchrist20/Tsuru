<script setup lang="ts">
import type { Tool } from '../../models/Tool'
import type { CodecLabels } from '../../types/codec'
import { ref, watch } from 'vue'
import BaseView from '../../components/BaseView.vue'
import CodecPanel from '../../components/codec/CodecPanel.vue'
import { useCodec } from '../../composables/useCodec'
import Base32Service from '../../services/encoders/base32.service'

defineProps<{ tool: Tool }>()

const padding = ref(true)

const base32Labels: CodecLabels = {
  encode: {
    tab: 'Encode',
    input: 'Input',
    output: 'Output Base32',
  },
  decode: {
    tab: 'Decode',
    input: 'Input Base32',
    output: 'Output',
  },
}

const { mode, input, output, error, loading, setMode, clear, refresh } = useCodec({
  encode: value => Base32Service.encode(value, padding.value),
  decode: value => Base32Service.decode(value),
})

watch(padding, () => refresh())
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
      :labels="base32Labels"
      input-mono
      output-mono
      @update:mode="setMode"
    >
      <template #options>
        <div class="mb-default flex flex-wrap items-center gap-small">
          <label class="cursor-pointer">
            <input v-model="padding" type="checkbox" class="peer sr-only">
            <span class="chip cursor-pointer select-none px-3 py-1 text-xs transition-colors duration-200 peer-checked:border-telegram-blue/40 peer-checked:bg-telegram-blue/15 peer-checked:text-telegram-blue peer-focus-visible:ring-2 peer-focus-visible:ring-telegram-blue/50">
              Padding
            </span>
          </label>
        </div>
      </template>
    </CodecPanel>
  </BaseView>
</template>
