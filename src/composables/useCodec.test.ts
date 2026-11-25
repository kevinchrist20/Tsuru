import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import { useCodec } from './useCodec'

describe('useCodec', () => {
  beforeEach(() => {
    vi.useFakeTimers()
  })

  afterEach(() => {
    vi.useRealTimers()
  })

  it('encodes on input after the debounce window', async () => {
    const codec = useCodec({
      encode: async value => `E(${value})`,
      decode: async value => `D(${value})`,
      debounceMs: 50,
    })

    codec.input.value = 'hello'
    expect(codec.output.value).toBe('')

    await vi.advanceTimersByTimeAsync(50)
    expect(codec.output.value).toBe('E(hello)')
  })

  it('clears output and error when input becomes empty', async () => {
    const codec = useCodec({
      encode: async value => `E(${value})`,
      decode: async () => {
        throw new Error('bad input')
      },
      debounceMs: 50,
    })

    codec.setMode('decode')
    codec.input.value = 'x'
    await vi.advanceTimersByTimeAsync(50)
    expect(codec.error.value).toBe('bad input')

    codec.input.value = ''
    await vi.advanceTimersByTimeAsync(50)
    expect(codec.output.value).toBe('')
    expect(codec.error.value).toBe('')
  })

  it('keeps per-mode state when switching modes', async () => {
    const codec = useCodec({
      encode: async value => `E(${value})`,
      decode: async value => `D(${value})`,
      debounceMs: 10,
    })

    codec.input.value = 'hello'
    await vi.advanceTimersByTimeAsync(10)

    codec.setMode('decode')
    expect(codec.input.value).toBe('')

    codec.input.value = 'world'
    await vi.advanceTimersByTimeAsync(10)
    expect(codec.output.value).toBe('D(world)')

    codec.setMode('encode')
    expect(codec.input.value).toBe('hello')
    expect(codec.output.value).toBe('E(hello)')
  })

  it('surfaces errors and keeps the output empty', async () => {
    const codec = useCodec({
      encode: async () => {
        throw new Error('encode failed')
      },
      decode: async value => value,
      debounceMs: 10,
    })

    codec.input.value = 'x'
    await vi.advanceTimersByTimeAsync(10)

    expect(codec.error.value).toBe('encode failed')
    expect(codec.output.value).toBe('')
    expect(codec.loading.value).toBe(false)
  })

  it('clear resets the active mode', async () => {
    const codec = useCodec({
      encode: async value => `E(${value})`,
      decode: async value => `D(${value})`,
      debounceMs: 10,
    })

    codec.input.value = 'hello'
    await vi.advanceTimersByTimeAsync(10)

    codec.clear()
    expect(codec.input.value).toBe('')
    expect(codec.output.value).toBe('')
    expect(codec.error.value).toBe('')
  })

  it('refresh re-runs the active input with new options', async () => {
    let suffix = 'A'
    const codec = useCodec({
      encode: async value => `${value}-${suffix}`,
      decode: async value => value,
      debounceMs: 10,
    })

    codec.input.value = 'hello'
    await vi.advanceTimersByTimeAsync(10)
    expect(codec.output.value).toBe('hello-A')

    suffix = 'B'
    codec.refresh()
    await vi.advanceTimersByTimeAsync(10)
    expect(codec.output.value).toBe('hello-B')
  })
})
