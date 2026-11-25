import { invoke } from '../../types/tauri_commands'

const UnicodeService = {
  encode(input: string): Promise<string> {
    return invoke('encode_unicode', { input })
  },

  decode(input: string): Promise<string> {
    return invoke('decode_unicode', { input })
  },
}

export default UnicodeService
