import type { ComputedRef, Ref, WritableComputedRef } from 'vue'
import type { CodecMode } from '../types/codec'
import { computed, reactive, ref, watch } from 'vue'
import { extractErrorMessage } from '../utils/error'

export interface UseCodecOptions {
  encode: (input: string) => Promise<string>
  decode: (input: string) => Promise<string>
  debounceMs?: number
  initialMode?: CodecMode
}

interface CodecState {
  input: string
  output: string
  error: string
}

interface UseCodecReturn {
  mode: Ref<CodecMode>
  input: WritableComputedRef<string>
  output: WritableComputedRef<string>
  error: ComputedRef<string>
  loading: Ref<boolean>
  setMode: (mode: CodecMode) => void
  clear: () => void
  refresh: () => void
}

export function useCodec(options: UseCodecOptions): UseCodecReturn {
  const debounceMs = options.debounceMs ?? 150
  const mode = ref<CodecMode>(options.initialMode ?? 'encode')
  const states = reactive<Record<CodecMode, CodecState>>({
    encode: { input: '', output: '', error: '' },
    decode: { input: '', output: '', error: '' },
  })
  const loading = ref(false)

  let timer: ReturnType<typeof setTimeout> | undefined
  let requestId = 0

  const input = computed({
    get: () => states[mode.value].input,
    set: (value: string) => {
      states[mode.value].input = value
    },
  })

  const output = computed({
    get: () => states[mode.value].output,
    set: (value: string) => {
      states[mode.value].output = value
    },
  })

  const error = computed(() => states[mode.value].error)

  function cancelPending() {
    if (timer) {
      clearTimeout(timer)
      timer = undefined
    }
    requestId += 1
    loading.value = false
  }

  async function run(targetMode: CodecMode, value: string) {
    const id = ++requestId
    loading.value = true
    states[targetMode].error = ''

    try {
      const result = targetMode === 'encode'
        ? await options.encode(value)
        : await options.decode(value)

      if (id !== requestId || targetMode !== mode.value) {
        return
      }

      states[targetMode].output = result
    }
    catch (err) {
      if (id !== requestId || targetMode !== mode.value) {
        return
      }

      states[targetMode].output = ''
      states[targetMode].error = extractErrorMessage(
        err,
        `Could not ${targetMode} the input.`,
      )
    }
    finally {
      if (id === requestId) {
        loading.value = false
      }
    }
  }

  function schedule(targetMode: CodecMode, value: string) {
    if (timer) {
      clearTimeout(timer)
      timer = undefined
    }

    // Invalidate any in-flight request before starting a new debounce window.
    requestId += 1
    loading.value = false

    if (value.length === 0) {
      states[targetMode].output = ''
      states[targetMode].error = ''
      return
    }

    timer = setTimeout(() => {
      timer = undefined
      void run(targetMode, value)
    }, debounceMs)
  }

  watch(
    () => states.encode.input,
    value => schedule('encode', value),
  )

  watch(
    () => states.decode.input,
    value => schedule('decode', value),
  )

  function setMode(nextMode: CodecMode) {
    if (nextMode === mode.value) {
      return
    }

    cancelPending()
    mode.value = nextMode
  }

  function clear() {
    cancelPending()
    states[mode.value] = { input: '', output: '', error: '' }
  }

  function refresh() {
    schedule(mode.value, states[mode.value].input)
  }

  return {
    mode,
    input,
    output,
    error,
    loading,
    setMode,
    clear,
    refresh,
  }
}
