export const MAX_IMAGE_BYTES = 8 * 1024 * 1024
export const IMAGE_ACCEPT = 'image/png,image/jpeg'

export type SupportedImageMime = 'image/png' | 'image/jpeg' | 'image/gif' | 'image/webp'

export interface ParsedImage {
  dataUrl: string
  mime: SupportedImageMime
  base64: string
  size: number
}

export function isAcceptedImage(file: File): boolean {
  return file.type === 'image/png'
    || file.type === 'image/jpeg'
    || /\.(?:jpe?g|png)$/i.test(file.name)
}

export function readImageAsDataUrl(file: File): Promise<string> {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()

    reader.onload = () => {
      if (typeof reader.result === 'string') {
        resolve(reader.result)
      }
      else {
        reject(new Error('Could not read the selected image.'))
      }
    }

    reader.onerror = () => reject(new Error('Could not read the selected image.'))
    reader.readAsDataURL(file)
  })
}

export function detectImageMime(bytes: Uint8Array): SupportedImageMime | null {
  if (
    bytes.length >= 8
    && bytes[0] === 0x89
    && bytes[1] === 0x50
    && bytes[2] === 0x4E
    && bytes[3] === 0x47
    && bytes[4] === 0x0D
    && bytes[5] === 0x0A
    && bytes[6] === 0x1A
    && bytes[7] === 0x0A
  ) {
    return 'image/png'
  }

  if (bytes.length >= 3 && bytes[0] === 0xFF && bytes[1] === 0xD8 && bytes[2] === 0xFF) {
    return 'image/jpeg'
  }

  if (
    bytes.length >= 6
    && bytes[0] === 0x47
    && bytes[1] === 0x49
    && bytes[2] === 0x46
    && bytes[3] === 0x38
  ) {
    return 'image/gif'
  }

  if (
    bytes.length >= 12
    && bytes[0] === 0x52
    && bytes[1] === 0x49
    && bytes[2] === 0x46
    && bytes[3] === 0x46
    && bytes[8] === 0x57
    && bytes[9] === 0x45
    && bytes[10] === 0x42
    && bytes[11] === 0x50
  ) {
    return 'image/webp'
  }

  return null
}

export function formatBytes(bytes: number): string {
  if (bytes < 1024) {
    return `${bytes} B`
  }

  if (bytes < 1024 * 1024) {
    return `${(bytes / 1024).toFixed(1)} KB`
  }

  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`
}

function decodeBase64ToBytes(base64: string): Uint8Array {
  try {
    const binary = atob(base64)
    return Uint8Array.from(binary, character => character.charCodeAt(0))
  }
  catch {
    throw new Error('Invalid Base64 data.')
  }
}

export function parseImageInput(input: string): ParsedImage {
  const trimmed = input.trim()

  if (!trimmed) {
    throw new Error('Paste a Base64 string or image data URL.')
  }

  let base64 = trimmed

  if (trimmed.startsWith('data:')) {
    const commaIndex = trimmed.indexOf(',')

    if (commaIndex === -1) {
      throw new Error('Invalid image data URL.')
    }

    const header = trimmed.slice(5, commaIndex)

    if (!header.startsWith('image/')) {
      throw new Error('Only image data URLs are supported.')
    }

    if (!header.includes(';base64')) {
      throw new Error('Only base64-encoded image data URLs are supported.')
    }

    base64 = trimmed.slice(commaIndex + 1)
  }

  const normalized = base64.replace(/\s/g, '')

  if (!normalized) {
    throw new Error('Paste a Base64 string or image data URL.')
  }

  const bytes = decodeBase64ToBytes(normalized)
  const mime = detectImageMime(bytes)

  if (!mime) {
    throw new Error('Could not detect a supported image type (PNG, JPEG, GIF, WebP).')
  }

  return {
    dataUrl: `data:${mime};base64,${normalized}`,
    mime,
    base64: normalized,
    size: bytes.length,
  }
}
