import { invoke } from '../../types/tauri_commands'

const HtmlService = {
  encode(input: string): Promise<string> {
    return invoke('encode_html', { input })
  },

  decode(input: string): Promise<string> {
    return invoke('decode_html', { input })
  },
}

export default HtmlService
