import { describe, expect, it } from 'vitest'
import { extractErrorMessage } from './error'

describe('extractErrorMessage', () => {
  it('passes string errors through', () => {
    expect(extractErrorMessage('boom')).toBe('boom')
  })

  it('reads the message property from objects', () => {
    expect(extractErrorMessage(new Error('kaboom'))).toBe('kaboom')
    expect(extractErrorMessage({ message: 'custom' })).toBe('custom')
  })

  it('ignores empty or non-string messages', () => {
    expect(extractErrorMessage({ message: '   ' }, 'fallback')).toBe('fallback')
    expect(extractErrorMessage({ message: 42 }, 'fallback')).toBe('fallback')
  })

  it('falls back for anything else', () => {
    expect(extractErrorMessage(null, 'fallback')).toBe('fallback')
    expect(extractErrorMessage(undefined)).toBe('Something went wrong.')
  })
})
