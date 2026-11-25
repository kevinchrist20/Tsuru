import { invoke } from '../../types/tauri_commands'

const BinaryService = {
  encode(input: string): Promise<string> {
    return invoke('encode_binary', { input })
  },

  decode(input: string): Promise<string> {
    return invoke('decode_binary', { input })
  },
}

export default BinaryService
