import { invoke } from '../../types/tauri_commands'

const HexService = {
  encode(input: string, uppercase = false): Promise<string> {
    return invoke('encode_hex', { input, uppercase })
  },

  decode(input: string): Promise<string> {
    return invoke('decode_hex', { input })
  },
}

export default HexService
