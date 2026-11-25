import { invoke } from '../../types/tauri_commands'

const UuidService = {
  generate(): Promise<string> {
    return invoke('generate_uuid')
  },

  generateMany(count: number): Promise<string[]> {
    return invoke('generate_uuids', { count })
  },
}

export default UuidService
