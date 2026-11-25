import { invoke } from '../../types/tauri_commands'

const PunycodeService = {
  encode(input: string): Promise<string> {
    return invoke('encode_punycode', { input })
  },

  decode(input: string): Promise<string> {
    return invoke('decode_punycode', { input })
  },
}

export default PunycodeService
