import { invoke } from '../../types/tauri_commands'

export interface Base64Options {
  urlSafe: boolean
  padding: boolean
}

const DEFAULT_OPTIONS: Base64Options = {
  urlSafe: false,
  padding: true,
}

const Base64Service = {
  encode(input: string, options: Base64Options = DEFAULT_OPTIONS): Promise<string> {
    return invoke('encode_base64', {
      input,
      urlSafe: options.urlSafe,
      padding: options.padding,
    })
  },

  decode(input: string, options: Base64Options = DEFAULT_OPTIONS): Promise<string> {
    return invoke('decode_base64', {
      input,
      urlSafe: options.urlSafe,
      padding: options.padding,
    })
  },
}

export default Base64Service
