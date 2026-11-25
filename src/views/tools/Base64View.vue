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
const wrap = ref(false)

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
    wrap: wrap.value,
  }),
  decode: value => Base64Service.decode(value, {
    urlSafe: urlSafe.value,
    padding: padding.value,
  }),
})

watch([urlSafe, padding, wrap], () => refresh())
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
      input-mono
      output-mono
      @update:mode="setMode"
    >
      <template #options>
        <div class="mb-default flex flex-wrap items-center gap-small">
          <label class="cursor-pointer">
            <input v-model="urlSafe" type="checkbox" class="peer sr-only">
            <span class="chip cursor-pointer select-none px-3 py-1 text-xs transition-colors duration-200 peer-checked:border-telegram-blue/40 peer-checked:bg-telegram-blue/15 peer-checked:text-telegram-blue peer-focus-visible:ring-2 peer-focus-visible:ring-telegram-blue/50">
              URL-safe alphabet
            </span>
          </label>

          <label v-if="mode === 'encode'" class="cursor-pointer">
            <input v-model="padding" type="checkbox" class="peer sr-only">
            <span class="chip cursor-pointer select-none px-3 py-1 text-xs transition-colors duration-200 peer-checked:border-telegram-blue/40 peer-checked:bg-telegram-blue/15 peer-checked:text-telegram-blue peer-focus-visible:ring-2 peer-focus-visible:ring-telegram-blue/50">
              Padding
            </span>
          </label>

          <label v-if="mode === 'encode'" class="cursor-pointer">
            <input v-model="wrap" type="checkbox" class="peer sr-only">
            <span class="chip cursor-pointer select-none px-3 py-1 text-xs transition-colors duration-200 peer-checked:border-telegram-blue/40 peer-checked:bg-telegram-blue/15 peer-checked:text-telegram-blue peer-focus-visible:ring-2 peer-focus-visible:ring-telegram-blue/50">
              Wrap lines (76)
            </span>
          </label>
        </div>
      </template>
    </CodecPanel>
  </BaseView>
</template>
