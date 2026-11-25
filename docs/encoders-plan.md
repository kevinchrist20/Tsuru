# Encoders & Decoders Plan

Status: Phase 3 complete — optional plugin-based extras deferred
Last updated: 2026-11-25
Scope: Tauri desktop app (Tauri 2 + Vue 3)

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
| Base64 text | `base64` | `encode_base64`, `decode_base64` | `Base64View.vue` + `CodecPanel` | Working, with URL-safe/padding toggles |
| Base64 image | `base64Image` | Browser File API + image sniffing | `Base64ImageView.vue` | Working, local files only |
| URL | `url` | `encode_url`, `decode_url` | `UrlView.vue` | Working, component + full-URL modes |
| HTML entities | `html` | `encode_html`, `decode_html` | `HtmlView.vue` | Working |
| Hex | `hex` | `encode_hex`, `decode_hex` | `HexView.vue` | Working, uppercase toggle |
| Binary | `binary` | `encode_binary`, `decode_binary` | `BinaryView.vue` | Working |
| Base32 | `base32` | `encode_base32`, `decode_base32` | `Base32View.vue` | Working, padding toggle |
| Unicode escapes | `unicode` | `encode_unicode`, `decode_unicode` | `UnicodeView.vue` | Working, surrogate pairs supported |
| Punycode / IDN | `punycode` | `encode_punycode`, `decode_punycode` | `PunycodeView.vue` | Working, per-label conversion |
| Quoted-Printable | `quotedPrintable` | `encode_quoted_printable`, `decode_quoted_printable` | `QuotedPrintableView.vue` | Working, RFC 2045 soft breaks |
| Slugs | `slug` | `generate_slug` | `SlugView.vue` | Working |
| UUID | `uuid` | `generate_uuid`, `generate_uuids` | `UuidView.vue` | Working, 1/5/10 generator |
| JWT | `jwt` | `decode_jwt` | `JwtView.vue` | Working, decode-only with warning |

Current gaps:

- Base64 Image does not support URL image fetching, native file dialogs, or saving
  decoded images to disk (deferred — requires Tauri plugins).
- The dashboard does not yet group unfinished tools with badges (all listed tools are
  implemented).

Resolved in Phases 0–3:

- `/tools/:id` resolves through `ToolView` + `toolViews.ts`; unfinished tools show
  `ComingSoonView` instead of the Base64 screen.
- All codec views share `CodecPanel` + `useCodec`, with options slots for per-tool
  toggles (URL mode, uppercase, padding, wrapping).
- Slug and UUID belong to `ToolCategory.Generators`; JWT belongs to
  `ToolCategory.Cryptography`.
- Every codec has Rust unit tests (93), the frontend has Vitest coverage (29 tests),
  and `JwtParts` is exported to TypeScript via ts-rs
  (`src/types/bindings/JwtParts.ts`).

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

### Phase 1 — Migration parity (complete)

#### 1. Base64 text

- [x] Keep standard Base64 alphabet and padding as the defaults.
- [x] Strip ASCII whitespace before decoding so wrapped input works.
- [x] Add user-toggleable URL-safe alphabet and padding options.
- [x] Preserve UTF-8 errors as readable messages.
- [x] Rust tests: empty input, ASCII, emoji/Unicode, invalid padding, invalid
      characters, whitespace-wrapped input, large input, URL-safe and no-padding cases.

#### 2. Slugs

- [x] Build `SlugView.vue` using the existing `generate_slug` command.
- [x] Improve slug rules:
      - transliterate/ignore accents predictably (`deunicode`)
      - lowercase
      - replace whitespace with one hyphen
      - collapse repeated hyphens
      - trim leading/trailing hyphens
      - drop unsupported punctuation
- [x] Move logic into `services/generators.rs` with unit tests.
- [x] Add copy/clear and a live output preview.

#### 3. UUID

- [x] Build `UuidView.vue` using the existing `generate_uuid` command.
- [x] Generate on demand, not on every keystroke.
- [x] Add a count selector (1, 5, 10) and copy-all.
- [x] Validate v4 format in tests (version nibble and variant bits).
- [x] Keep generation in Rust so all UUIDs come from one implementation.

#### 4. Base64 image

- [x] Build `Base64ImageView.vue` with Encode and Decode tabs.
- [x] Encode:
      - local image picker (`<input type="file" accept="image/png,image/jpeg">`)
      - accept JPG, PNG, JPEG (match DevUtils)
      - enforce an 8 MB limit
      - show image preview and `data:<mime>;base64,...` output
- [x] Decode:
      - accept a full data URL or raw Base64
      - detect MIME type from magic bytes
      - show an image preview
      - show a clear error for malformed or non-image input
- [x] Use browser APIs (`FileReader`, `Blob`, `URL.createObjectURL`) for the MVP.
- [ ] Add native file dialog / save-to-file later with Tauri plugins.

### Phase 2 — Additional text codecs (complete)

Each codec follows the same shape: pure Rust service + thin command + service wrapper
+ view built on `CodecPanel`.

| Tool | Rust dependency | Notes |
|---|---|---|
| URL encode/decode | `percent-encoding` | Component vs full-URL mode; decoded URLs are never auto-opened |
| HTML entities | `html-escape` | Encoder covers `& < > " '`; decoder supports named + numeric entities, unknown entities pass through |
| Hex | custom | UTF-8 bytes to hex; uppercase toggle; accepts `0x`, whitespace, mixed case |
| Binary | custom | Space-separated 8-bit groups or compact bit strings; strict validation |
| Base32 | `data-encoding` | RFC 4648 vectors covered; padding toggle; lowercase tolerated |
| JWT decoder | `base64` + `serde_json` + `ts-rs` | Lives under **Cryptography & Security**; `JwtParts` TS binding exported |

Completed:

- [x] URL encode/decode with component/full-URL mode select.
- [x] HTML entity encode/decode.
- [x] Hex encode/decode with uppercase toggle.
- [x] Binary encode/decode.
- [x] Base32 encode/decode with padding toggle.
- [x] JWT decoder with three panels, algorithm chip, and "not verified" warning.

### Phase 3 — Polish (complete)

- [x] Base64 line wrapping option (RFC 2045 76-character soft breaks).
- [x] Unicode escape/unescape (surrogate pairs, `\xXX` and short escapes).
- [x] Punycode / IDN converter (per-label `xn--` conversion, not full IDNA validation).
- [x] Quoted-Printable encoder/decoder (RFC 2045 with soft breaks and trailing
      whitespace handling).
- [x] Drag-and-drop image input (done during the UI pass).
- [x] Vitest + Vue Test Utils with `pnpm test` (29 frontend tests).
- [ ] Save decoded image to disk (needs `tauri-plugin-fs` / `tauri-plugin-dialog`).
- [ ] Optional native file dialog and HTTP image fetch (needs new Tauri plugins and
      capability changes).

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
    ModeTabs.vue
    ValuePanel.vue
  composables/
    useCodec.ts
  types/
    codec.ts
  services/encoders/
    base64.service.ts
    base64-image.service.ts
    url.service.ts
    html.service.ts
    hex.service.ts
    binary.service.ts
    base32.service.ts
    unicode.service.ts
    punycode.service.ts
    quoted-printable.service.ts
  services/generators/
    slug.service.ts
    uuid.service.ts
  services/security/
    jwt.service.ts
  types/bindings/
    JwtParts.ts (generated by ts-rs)
  views/tools/
    ToolView.vue
    Base64View.vue
    Base64ImageView.vue
    UrlView.vue
    HtmlView.vue
    HexView.vue
    BinaryView.vue
    Base32View.vue
    SlugView.vue
    UuidView.vue
    JwtView.vue
    ComingSoonView.vue
    ToolNotFoundView.vue
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

- [x] Vitest + Vue Test Utils are wired up (`pnpm test`, jsdom environment).
- [x] `useCodec` state transitions covered: mode switch, debounce, error, clear, copy, refresh.
- [x] `CodecPanel` renders labels, output, and errors.
- [x] `ToolView` registry resolves every `ToolRoute`.
- [ ] Manual QA checklist per tool before marking ready.

### Validation commands

```bash
pnpm lint
pnpm build
cargo test --manifest-path src-tauri/Cargo.toml
pnpm tauri build --debug --no-bundle
```

## 9. Ordered task list

1. [x] Foundation: `ToolView` registry, route fix, `CodecPanel`, `useCodec`, error utility.
2. [x] Refactor Base64 onto the shared stack; add Rust tests and URL-safe/padding toggles.
3. [x] Slug view + improved service + tests.
4. [x] UUID view + tests.
5. [x] Base64 image view + file/size/preview handling.
6. [x] URL encode/decode.
7. [x] HTML entities.
8. [x] Hex.
9. [x] Binary.
10. [x] Base32.
11. [x] JWT decoder under Cryptography & Security.
12. [x] Vitest setup after parity (`pnpm test`, 29 tests).
13. [ ] Optional: native dialogs, save decoded images, URL image fetch (needs Tauri
      plugins), extra codecs.

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

1. Base64 defaults are standard alphabet, padding on, wrapping off; the wrap toggle
   outputs RFC 2045 soft breaks at 76 characters.
2. Slug transliteration uses `deunicode`; confirm whether Japanese/Chinese output should
   keep romanization or fall back to pinyin/romaji settings.
3. UUID UI ships with counts 1/5/10 and newline-separated copy-all.
4. All current tools are implemented, so `ComingSoonView` only appears for future
   unregistered routes; no badge is needed yet.
5. Base64 Image encode accepts JPG/PNG only; decode sniffs PNG, JPEG, GIF, and WebP.
   Decide whether encode should support GIF/WebP too.
6. Native file dialogs, saving decoded images, and URL image fetching require Tauri
   plugins (`dialog`, `fs`, `http`) — deferred until the plugin/capability work starts.
