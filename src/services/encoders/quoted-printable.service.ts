import { invoke } from '../../types/tauri_commands'

const QuotedPrintableService = {
  encode(input: string): Promise<string> {
    return invoke('encode_quoted_printable', { input })
  },

  decode(input: string): Promise<string> {
    return invoke('decode_quoted_printable', { input })
  },
}

export default QuotedPrintableService
