import { describe, expect, it } from 'vitest'
import {
  detectImageMime,
  formatBytes,
  parseImageInput,
} from './base64-image.service'

const SAMPLE_PNG
  = 'iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg=='

describe('detectImageMime', () => {
  it('detects png from magic bytes', () => {
    const bytes = Uint8Array.from(atob(SAMPLE_PNG), c => c.charCodeAt(0))
    expect(detectImageMime(bytes)).toBe('image/png')
  })

  it('detects jpeg', () => {
    expect(detectImageMime(new Uint8Array([0xFF, 0xD8, 0xFF, 0xE0]))).toBe('image/jpeg')
  })

  it('detects gif', () => {
    expect(detectImageMime(new Uint8Array([0x47, 0x49, 0x46, 0x38, 0x39, 0x61]))).toBe('image/gif')
  })

  it('detects webp', () => {
    const bytes = new Uint8Array(12)
    bytes.set([0x52, 0x49, 0x46, 0x46], 0)
    bytes.set([0x57, 0x45, 0x42, 0x50], 8)
    expect(detectImageMime(bytes)).toBe('image/webp')
  })

  it('returns null for unknown bytes', () => {
    expect(detectImageMime(new Uint8Array([1, 2, 3, 4]))).toBeNull()
  })
})

describe('formatBytes', () => {
  it('formats bytes, kb and mb', () => {
    expect(formatBytes(512)).toBe('512 B')
    expect(formatBytes(2048)).toBe('2.0 KB')
    expect(formatBytes(3 * 1024 * 1024)).toBe('3.0 MB')
  })
})

describe('parseImageInput', () => {
  it('parses raw base64 and sniffs the mime type', () => {
    const parsed = parseImageInput(SAMPLE_PNG)
    expect(parsed.mime).toBe('image/png')
    expect(parsed.dataUrl).toBe(`data:image/png;base64,${SAMPLE_PNG}`)
    expect(parsed.size).toBeGreaterThan(0)
  })

  it('parses data urls and normalizes whitespace', () => {
    const padded = `${SAMPLE_PNG.slice(0, 20)} ${SAMPLE_PNG.slice(20)}`
    const parsed = parseImageInput(`data:image/png;base64,${padded}`)
    expect(parsed.base64).toBe(SAMPLE_PNG)
    expect(parsed.mime).toBe('image/png')
  })

  it('rejects invalid base64', () => {
    expect(() => parseImageInput('not base64 !!!')).toThrow('Invalid Base64 data.')
  })

  it('rejects non-image payloads', () => {
    const text = btoa('hello world')
    expect(() => parseImageInput(text)).toThrow('Could not detect a supported image type')
  })

  it('rejects non-image data urls', () => {
    expect(() => parseImageInput(`data:text/plain;base64,${btoa('x')}`)).toThrow(
      'Only image data URLs are supported.',
    )
  })

  it('rejects empty input', () => {
    expect(() => parseImageInput('   ')).toThrow('Paste a Base64 string or image data URL.')
  })
})
