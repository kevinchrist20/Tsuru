import { invoke } from '../../types/tauri_commands'

const SlugService = {
  generate(input: string): Promise<string> {
    return invoke('generate_slug', { input })
  },
}

export default SlugService
