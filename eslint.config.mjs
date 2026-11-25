import config from '@antfu/eslint-config'

export default config({
  ignores: [
    'dist/**',
    'pnpm-workspace.yaml',
    'src-tauri/**',
    'src/types/bindings/**',
    'src/types/tauri_commands.ts',
  ],
})
