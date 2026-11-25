import type { CodecLabels, CodecMode } from '../../types/codec'
import { mount } from '@vue/test-utils'
import { describe, expect, it } from 'vitest'
import CodecPanel from './CodecPanel.vue'

const labels: CodecLabels = {
  encode: { tab: 'Encode', input: 'Input', output: 'Output Base64' },
  decode: { tab: 'Decode', input: 'Input Base64', output: 'Output String' },
}

function mountPanel(mode: CodecMode, overrides: Partial<Record<string, unknown>> = {}) {
  return mount(CodecPanel, {
    props: {
      mode,
      input: '',
      output: '',
      labels,
      ...overrides,
    },
  })
}

describe('codecPanel', () => {
  it('renders the active tab labels', () => {
    const wrapper = mountPanel('encode')
    expect(wrapper.text()).toContain('Input')
    expect(wrapper.text()).toContain('Output Base64')
    expect(wrapper.text()).toContain('Encode')
    expect(wrapper.text()).toContain('Decode')
  })

  it('emits update:mode when a tab is clicked', async () => {
    const wrapper = mountPanel('encode')
    await wrapper.findAll('[role="tab"]')[1]!.trigger('click')
    expect(wrapper.emitted('update:mode')?.[0]).toEqual(['decode'])
  })

  it('emits update:input when the textarea changes', async () => {
    const wrapper = mountPanel('encode')
    const textarea = wrapper.find('textarea')
    await textarea.setValue('hello')
    expect(wrapper.emitted('update:input')?.[0]).toEqual(['hello'])
  })

  it('shows the character count', () => {
    const wrapper = mountPanel('encode', { input: 'abc' })
    expect(wrapper.text()).toContain('3 chars')
  })

  it('renders the error alert', () => {
    const wrapper = mountPanel('decode', { error: 'Invalid Base64 input' })
    expect(wrapper.text()).toContain('Invalid Base64 input')
  })

  it('hides the error alert when there is no error', () => {
    const wrapper = mountPanel('encode')
    expect(wrapper.find('[role="alert"]').exists()).toBe(false)
  })
})
