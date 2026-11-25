import type { JwtParts } from '../../types/bindings'
import { invoke } from '../../types/tauri_commands'

const JwtService = {
  decode(token: string): Promise<JwtParts> {
    return invoke('decode_jwt', { token })
  },
}

export default JwtService
