import { invoke } from '../../types/tauri_commands'

const Base64Service = {
  encode(input: string): Promise<string> {
    return invoke('encode_base64', { input })
  },

  decode(input: string): Promise<string> {
    return invoke('decode_base64', { input })
  },
}

export default Base64Service
