# Encoders & Decoders Plan

Status: Phase 0 complete
Last updated: 2026-11-25
Scope: Tsuru desktop app (Tauri 2 + Vue 3)

## 1. Goal

Complete the **Encoders & Decoders** category in Tsuru, starting with parity for the
original DevUtils tools and then adding the common encoders developers expect.

The plan has three outcomes:

1. Every tool card in the category opens its own working view.
2. Encode/decode logic is shared, typed, and tested instead of copied per tool.
3. New codecs can be added without touching routing or duplicating UI state.

## 2. Current state

| Tool | Route | Backend | Frontend | Status |
|---|---|---|---|---|
| Base64 text | `base64` | `encode_base64`, `decode_base64` | `Base64View.vue` + `CodecPanel` | Working |
| Base64 image | `base64Image` | None | Coming-soon fallback | Metadata only |
| Slugs | `slug` | `generate_slug` | Coming-soon fallback | Command exists, no view |
| UUID | `uuid` | `generate_uuid` | Coming-soon fallback | Command exists, no view |

Current gaps:

- There is no test coverage for Base64 or the generators.
- Slug logic is naive: it does not transliterate accented characters, trim leading or
  trailing hyphens, or collapse every repeated hyphen.
- Base64 Image, Slugs, and UUID are still waiting on their dedicated views.
- There is no frontend test runner yet (Vitest is deferred until after parity).

Resolved in Phase 0:

- `/tools/:id` now resolves through `ToolView` + `toolViews.ts`; unfinished tools show
  `ComingSoonView` instead of the Base64 screen.
- `Base64View` uses the shared `CodecPanel` + `useCodec` state model.
- The Base64 service lives under `src/services/encoders/`.
- Slug and UUID now belong to `ToolCategory.Generators`.

## 3. Decisions

| Decision | Choice |
|---|---|
| Category taxonomy | **Add `ToolCategory.Generators` and move Slug/UUID there** |
| Where codec logic lives | Rust services for text codecs; browser File API for image preview |
| Shared UI | `CodecPanel` component + `useCodec` composable |
| Routing | Single `/tools/:id` route with a view registry |
| Error contract | Keep `Result<T, String>` for MVP; revisit typed errors later |
| Base64 image scope | **Local files only for now; no URL fetching** |
| Base64 options | **URL-safe alphabet and padding toggles, opt-in per user** |
| Frontend tests | **Add Vitest after migration parity** |
| JWT location | **Cryptography & Security category** |

## 4. Tool scope by phase

### Phase 0 — Foundation (complete)

- [x] Add `src/views/tools/ToolView.vue` that resolves the tool from
      `route.params.id` and renders the matching view from `toolViews.ts`.
- [x] Replace the blanket `component: Base64View` route with the registry view.
- [x] Add `src/components/codec/CodecPanel.vue` with:
      encode/decode tabs, input label, read-only output, error alert, character count,
      loading state, and an options slot for tool-specific controls.
- [x] Add `src/composables/useCodec.ts` to own per-mode input/output/error state and
      debounce input changes (~150 ms).
- [x] Add `src/utils/error.ts` with `extractErrorMessage(error, fallback)`.
- [x] Keep unfinished tools safe: missing registry entries fall back to
      `ComingSoonView.vue`, and unknown routes show `ToolNotFoundView.vue`.
- [x] Refactor `Base64View.vue` onto `CodecPanel` + `useCodec` with no behaviour
      regressions.
- [x] Move `base64.service.ts` under `src/services/encoders/`.
- [x] Add `ToolCategory.Generators` and move Slug/UUID into it.
- [x] Defer Vitest until after migration parity (decision recorded above).

Note: copy/clear still live in `BaseView`, which now works directly against the
codec's writable input/output state. The optional `Tool.status` field was replaced by
the view registry fallback to avoid duplicating readiness information.

### Phase 1 — Migration parity

#### 1. Base64 text

- [ ] Keep standard Base64 alphabet and padding as the defaults.
- [ ] Strip ASCII whitespace before decoding so wrapped input works.
- [ ] Add user-toggleable URL-safe alphabet and padding options.
- [ ] Preserve UTF-8 errors as readable messages.
- [ ] Rust tests: empty input, ASCII, emoji/Unicode, invalid padding, invalid
      characters, whitespace-wrapped input, large input, URL-safe and no-padding cases.

#### 2. Slugs

- [ ] Build `SlugView.vue` using the existing `generate_slug` command.
- [ ] Improve slug rules:
      - transliterate/ignore accents predictably
      - lowercase
      - replace whitespace with one hyphen
      - collapse repeated hyphens
      - trim leading/trailing hyphens
      - drop unsupported punctuation
- [ ] Move logic into `services/generators.rs` with unit tests.
- [ ] Add copy/clear and a live output preview.

#### 3. UUID

- [ ] Build `UuidView.vue` using the existing `generate_uuid` command.
- [ ] Generate on demand, not on every keystroke.
- [ ] Add a count selector (1, 5, 10) and copy-all.
- [ ] Validate v4 format in tests (version nibble and variant bits).
- [ ] Keep generation in Rust so all UUIDs come from one implementation.

#### 4. Base64 image

- [ ] Build `Base64ImageView.vue` with Encode and Decode tabs.
- [ ] Encode:
      - local image picker (`<input type="file" accept="image/*">`)
      - accept JPG, PNG, JPEG (match DevUtils)
      - enforce an 8 MB limit
      - show image preview and `data:<mime>;base64,...` output
- [ ] Decode:
      - accept a full data URL or raw Base64
      - detect MIME type where possible
      - show an image preview
      - show a clear error for malformed or non-image input
- [ ] Use browser APIs (`FileReader`, `Blob`, `URL.createObjectURL`) for the MVP.
- [ ] Add native file dialog / save-to-file later with Tauri plugins.

### Phase 2 — Additional text codecs

Each codec follows the same shape: pure Rust service + thin command + service wrapper
+ view built on `CodecPanel`.

| Tool | Rust dependency | Notes |
|---|---|---|
| URL encode/decode | `percent-encoding` | Component vs full-URL mode; do not auto-open decoded URLs |
| HTML entities | `html-escape` or equivalent | Decode into a textarea/pre, never `v-html` |
| Hex | `data-encoding` or custom | UTF-8 bytes to hex; accept upper/lower case |
| Binary | custom | UTF-8 bytes to 8-bit groups; enforce valid lengths |
| Base32 | `data-encoding` | RFC 4648; optional padding |
| JWT decoder | `base64` + `serde_json` | Lives under **Cryptography & Security**; header/payload only, display "not verified" warning |

### Phase 3 — Polish

- [ ] Base64 line wrapping option (URL-safe + padding toggles land in Phase 1).
- [ ] Unicode escape/unescape.
- [ ] Punycode / IDN.
- [ ] Quoted-Printable.
- [ ] Drag-and-drop image input.
- [ ] Save decoded image to disk.
- [ ] Optional native file dialog and HTTP image fetch.

## 5. Architecture

### Rust layout

```
src-tauri/src/
  commands/
    encoders.rs          # thin #[tauri::command] wrappers
    generators.rs
  services/
    encoders/
      mod.rs
      base64.rs
      url.rs
      html.rs
      hex.rs
      binary.rs
      base32.rs
      jwt.rs
    generators.rs        # slug + uuid
  models/                # only when a command needs a struct
```

Rules:

- Commands do argument marshalling only.
- Services contain deterministic, unit-testable functions.
- Prefer `encode(input) -> Result<String, String>` and
  `decode(input) -> Result<String, String>` signatures for text codecs.
- Use `#[derive(TS)]` and `TS_RS_EXPORT_DIR` when a tool needs a structured payload
  (JWT parts, image metadata, options).
- Do not introduce a trait/abstraction until at least two codecs need options.

### Frontend layout

```
src/
  components/codec/
    CodecPanel.vue
  composables/
    useCodec.ts
  services/encoders/
    base64.service.ts
    base64-image.service.ts
    slug.service.ts
    uuid.service.ts
    url.service.ts
    html.service.ts
    hex.service.ts
    binary.service.ts
    base32.service.ts
    jwt.service.ts
  views/tools/
    ToolView.vue
    Base64View.vue
    Base64ImageView.vue
    SlugView.vue
    UuidView.vue
```

### Data flow

```
Tool card -> /tools/:id -> ToolView registry -> tool view
                                      |
                                      v
                            CodecPanel + useCodec
                                      |
                                      v
                             service -> invoke -> Rust command -> service
```

### Routing registry

```ts
const toolViews: Record<ToolRoute, Component> = {
  [ToolRoute.Base64]: Base64View,
  [ToolRoute.Base64Image]: Base64ImageView,
  [ToolRoute.Slugs]: SlugView,
  [ToolRoute.UUID]: UuidView,
}
```

The registry is the only place that needs to change when a tool is added.

## 6. UX rules

Every codec view should share:

- Header with tool name, back navigation, and info modal.
- Encode/Decode tabs where both directions apply.
- Input textarea and read-only output.
- Copy, clear, and optional swap.
- Inline error alert near the input.
- Character count and, for large outputs, a truncation/limit notice.
- Debounced live encoding, with a visible loading state for async work.
- Keyboard-friendly focus order and accessible labels.

Tool-specific UX:

- **Base64 image**: drop zone, thumbnail preview, file size, MIME type.
- **Slug**: live preview, copy, example input.
- **UUID**: generate button, count selector, list output, copy-all.
- **JWT**: three panels (header, payload, signature) and a prominent
  "decoded only — not verified" notice.

## 7. Error and security rules

- Never render decoded HTML with `v-html`.
- Never auto-open decoded URLs.
- Enforce input/file size limits before encoding or decoding.
- Validate JWT structure but do not claim signature verification.
- Keep all encoding/decoding local. No network calls in the MVP.
- Return user-facing messages, not raw panic/debug output.
- Log unexpected errors to the console, but keep UI messages actionable.

## 8. Testing

### Rust

- One unit-test module per codec.
- Known-vector tests (RFC 4648 for Base64/Base32, standard hex vectors).
- Round-trip tests: `decode(encode(input)) == input`.
- Invalid-input tests assert errors, not panics.
- Generator tests for slug normalization and UUID v4 format.
- Binding generator test continues to run via `cargo test`.

### Frontend

- Add Vitest + Vue Test Utils **after migration parity** (decision recorded above).
- Test `useCodec` state transitions: mode switch, debounce, error, clear, copy.
- Test `CodecPanel` renders labels, output, and errors.
- Test `ToolView` registry resolves every `ToolRoute`.
- Manual QA checklist per tool before marking ready.

### Validation commands

```bash
pnpm lint
pnpm build
cargo test --manifest-path src-tauri/Cargo.toml
pnpm tauri build --debug --no-bundle
```

## 9. Ordered task list

1. [x] Foundation: `ToolView` registry, route fix, `CodecPanel`, `useCodec`, error utility.
2. [x] Refactor Base64 onto the shared stack. Rust tests remain part of Phase 1.
3. [ ] Slug view + improved service + tests.
4. [ ] UUID view + tests.
5. [ ] Base64 image view + file/size/preview handling.
6. [ ] URL encode/decode.
7. [ ] HTML entities.
8. [ ] Hex.
9. [ ] Binary.
10. [ ] Base32.
11. [ ] JWT decoder under Cryptography & Security.
12. [ ] Optional: Vitest setup after parity, native dialogs, URL image fetch, extra codecs.

## 10. Acceptance criteria

- Every ready tool card navigates to its own correct view.
- Unfinished tools show a clear "Coming soon" screen instead of another tool.
- All codecs round-trip valid input and return readable errors for invalid input.
- Copy, clear, and tab switching work consistently across tools.
- Base64 image enforces the 8 MB limit and previews local JPG/PNG/JPEG files.
- Slug output is deterministic and handles accents, punctuation, and extra spaces.
- UUID output is valid v4 and regenerates on demand.
- No `v-html` rendering of decoded content.
- `pnpm lint`, `pnpm build`, `cargo test`, and a Tauri debug build all pass.

## 11. Remaining decisions

1. Base64 defaults: standard alphabet and padding enabled; both toggles opt-out.
2. Slug transliteration: use a crate or a small deterministic ASCII map?
3. UUID UI: counts of 1/5/10 and newline-separated copy-all — confirm.
4. Should dashboard cards for unfinished tools show a "Coming soon" badge?
5. Base64 image MIME detection: infer from data URL prefix, file extension, or sniffing?
