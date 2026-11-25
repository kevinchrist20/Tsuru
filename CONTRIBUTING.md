# Contributing to Tsuru

Thanks for contributing to Tsuru. This guide keeps contributions simple and consistent.

## Workflow

1. Open or find an issue describing the bug/feature.
2. Create a focused branch from `tauri`.
3. Make small, reviewable commits.
4. Open a pull request with context and validation notes.

## Branch Naming

Use one of these patterns:

- `feat/<short-description>`
- `fix/<short-description>`
- `chore/<short-description>`
- `docs/<short-description>`

Examples:

- `feat/json-formatter-tool`
- `fix/base64-whitespace-decoding`
- `docs/installation-notes`

## Commit Messages

Keep commit messages clear and scoped. A simple prefix style is enough:

- `feat: add hash generator tool`
- `fix: tolerate whitespace in base64 decoding`
- `docs: update planned tools list`

## Local Validation

Before opening a PR, run:

```bash
pnpm build
pnpm test
pnpm lint
```

If your change touches Rust/Tauri backend code, also run:

```bash
cargo test --manifest-path src-tauri/Cargo.toml
```

Note: `cargo test` also regenerates the TypeScript command bindings in
`src/types/tauri_commands.ts` and `src/types/bindings/`. Commit the updated files when
command signatures change.

## Pull Request Checklist

- [ ] The change is focused and tied to an issue (or clearly explained).
- [ ] `pnpm build`, `pnpm test` and `pnpm lint` pass locally.
- [ ] `cargo test --manifest-path src-tauri/Cargo.toml` passes for backend changes.
- [ ] New commands or services follow the thin-command/service-layer structure and
      include unit tests.
- [ ] UI changes include screenshots or short notes.
- [ ] Relevant docs are updated (README, `docs/encoders-plan.md`, `REQUIREMENTS.md`).

## Release-Related Docs

For user-facing changes or release-impacting fixes, update as needed:

- `CHANGELOG.md` for release history.
- `release_notes.md` for the next release body used by CI.

## PR Description Template

Use this format in your PR body:

```md
## Summary
- What changed and why

## Validation
- Commands run and key results

## Notes
- Risks, follow-ups, or rollout details
```

## Code of Conduct

Be respectful, constructive, and collaborative in all discussions and reviews.
