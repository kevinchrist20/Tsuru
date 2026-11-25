import { invoke } from '../../types/tauri_commands'

export type UrlEncodeMode = 'component' | 'url'

const UrlService = {
  encode(input: string, mode: UrlEncodeMode): Promise<string> {
    return invoke('encode_url', { input, mode })
  },

  decode(input: string): Promise<string> {
    return invoke('decode_url', { input })
  },
}

export default UrlService
