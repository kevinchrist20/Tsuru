import { invoke } from '../../types/tauri_commands'

const Base32Service = {
  encode(input: string, padding = true): Promise<string> {
    return invoke('encode_base32', { input, padding })
  },

  decode(input: string): Promise<string> {
    return invoke('decode_base32', { input })
  },
}

export default Base32Service
