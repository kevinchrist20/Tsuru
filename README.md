# Tsuru

[![Release](https://img.shields.io/github/v/release/kevinchrist20/Tsuru)](https://github.com/kevinchrist20/Tsuru/releases)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
![Built with Tauri](https://img.shields.io/badge/Built%20with-Tauri-blue)

Tsuru is a desktop app that provides a collection of developer tools designed to assist your workflow. From simple utilities
like encoders and decoders, generators, JSON formatters, validation tools, cryptography and security, converters and more.
Tsuru covers various categories in real-world programming — fully offline, no telemetry, no accounts.

Built with **Tauri 2**, **Vue 3**, **TypeScript** and **Tailwind CSS 4**.

## Features

- **13 ready-to-use tools** across Encoders & Decoders, Generators and Cryptography & Security.
- **Easy to Use**: Every tool follows the same layout — encode/decode tabs, live output, copy/clear.
- **Desktop Application**: Tsuru is a desktop app, providing offline access and convenience for developers.
- **Typed IPC**: Tauri commands are exposed through auto-generated TypeScript bindings, and structured payloads are exported
  with [ts-rs](https://github.com/Aleph-Alpha/ts-rs).
- **Tested**: 90+ Rust unit tests for the codecs, plus a Vitest suite for the shared UI logic.
- **Auto-updates**: updates are signature-verified and offered through a consent dialog —
  download when you choose, then confirm to restart and upgrade.

## Available tools

### Encoders & Decoders

| Tool | Description |
| --- | --- |
| Base64 Encoder/Decoder | Text ↔ Base64 with URL-safe alphabet, padding and RFC 2045 line-wrapping options. |
| Base64 Image Encoder/Decoder | Local JPG/PNG (drag & drop, up to 8 MB) → `data:` URL, and Base64/data URL → image preview. |
| URL Encoder/Decoder | Percent-encoding in component (`encodeURIComponent`) or full-URL (`encodeURI`) modes. |
| HTML Entity Encoder/Decoder | Escape `& < > " '` and decode named and numeric character references. |
| Hex Encoder/Decoder | Text ↔ UTF-8 bytes as hex, with an uppercase output toggle. |
| Binary Encoder/Decoder | Text ↔ 8-bit binary groups (spaced or compact). |
| Base32 Encoder/Decoder | RFC 4648 Base32 with an optional padding toggle. |
| Unicode Escape/Unescape | `\uXXXX` escapes including surrogate pairs, plus `\xXX` and short escapes. |
| Punycode / IDN Converter | Unicode domains ↔ `xn--` punycode, label by label. |
| Quoted-Printable Encoder/Decoder | RFC 2045 MIME encoding with 76-character soft breaks. |

### Generators

| Tool | Description |
| --- | --- |
| Slugs | SEO-friendly URL slugs with accent transliteration (`deunicode`). |
| UUID | Version 4 UUIDs, one at a time or in batches of 1 / 5 / 10. |

### Cryptography & Security

| Tool | Description |
| --- | --- |
| JWT Decoder | Decode a token's header and payload with pretty-printed JSON. Decode-only — signatures are never verified. |

## Planned tools

The roadmap below covers the tools we intend to add next, inspired by
[appdevtools.com](https://appdevtools.com/), [quicktype.io/dart](https://quicktype.io/dart/),
[jsoncrack.com](https://jsoncrack.com/) and Yaml Crack.

### Data & converters

- **JSON Formatter / Validator / Minifier** — beautify and validate JSON with syntax error reporting.
- **JSON Crack–style visualizer** — explore JSON as an interactive graph, expand/collapse nodes and search.
- **Yaml Crack–style visualizer** — the same graph explorer experience for YAML, plus YAML ↔ JSON conversion.
- **JSON → Dart model generator** — quicktype-style class generation from JSON samples for Flutter projects
  (with more targets like TypeScript/Kotlin to follow).
- **JSON ↔ XML** and **CSV ↔ JSON** conversion.
- **Query String Parser** and **URL Parser** — inspect and edit parts of a URL.
- **Number Base Converter** — binary, octal, decimal, hex and arbitrary bases.

### Text utilities

- **Clean Paste** — strip formatting, smart quotes and invisible characters from clipboard text.
- **Case Converter** — camelCase, snake_case, kebab-case, PascalCase, CONSTANT_CASE, Title Case.
- **Text Diff Checker** — side-by-side comparison of two text blocks.
- **Markdown Previewer** — live-rendered markdown with copy-as-HTML.
- **Lorem Ipsum Generator** — paragraphs, sentences or words on demand.

### Validation

- **Regex Tester** — pattern testing with match highlighting and capture groups.
- **Common validators** — email, URL, IPv4/IPv6, credit card (Luhn) and JSON Schema checks.

### Cryptography & Security

- **Hash Generator** — MD5, SHA-1, SHA-256, SHA-512 for text and files.
- **HMAC Generator** — keyed hashes for the same algorithms.
- **Password Generator** — configurable length and character sets with strength hints.

### Everyday helpers

- **Unix Timestamp Converter** — epoch ↔ human-readable dates in local time and UTC.
- **Cron Expression Parser** — human-readable explanations for cron schedules.
- **Color Converter** — HEX ↔ RGB ↔ HSL with a picker.
- **QR Code Generator** — offline QR codes for text, URLs and Wi-Fi credentials.
- **ASCII / Unicode Table** — quick character reference with search.

Have an idea for another tool? Open an issue — the tool registry, shared codec UI and service layers make adding new tools
straightforward.

## Getting started

### Installation

Download the latest build for your platform from the
[Releases](https://github.com/kevinchrist20/Tsuru/releases) page.

> Current builds may be unsigned or not notarized yet. Only install artifacts from the
> official releases page.

#### Verify Downloads (Recommended)

Before installing, verify the checksum of the downloaded file against the
`SHA256SUMS.txt` file attached to the release.

macOS / Linux:

```bash
shasum -a 256 Tsuru*.dmg
# or
sha256sum Tsuru*
```

Windows (PowerShell):

```powershell
Get-FileHash .\Tsuru* -Algorithm SHA256
```

#### macOS (Apple Silicon + Intel)

Choose the correct build:

- Apple Silicon (M1/M2/M3): `aarch64-apple-darwin`
- Intel: `x86_64-apple-darwin`

Install:

1. Download the `.dmg` for your architecture.
2. Open it and drag `Tsuru.app` into `Applications`.
3. Try launching once from `Applications`.

If macOS blocks it because it is not signed/notarized yet:

1. Open `System Settings` → `Privacy & Security`.
2. In the security section, click `Open Anyway` for Tsuru.
3. Confirm by clicking `Open`.

Terminal alternative (remove quarantine flag):

```bash
sudo xattr -dr com.apple.quarantine /Applications/Tsuru.app
open -a /Applications/Tsuru.app
```

#### Windows

1. Download the `.msi` or `.exe` installer.
2. Launch the installer.

If Windows SmartScreen appears:

1. Click `More info` → `Run anyway`.

If the downloaded file is blocked, you can unblock it first:

```powershell
Unblock-File .\Tsuru*.msi
# or
Unblock-File .\Tsuru*.exe
```

#### Linux

Choose the artifact for your distro:

- `.AppImage` (portable, most distros)
- `.deb` (Debian/Ubuntu)
- `.rpm` (Fedora/RHEL/openSUSE)

AppImage:

```bash
chmod +x Tsuru*.AppImage
./Tsuru*.AppImage
```

Debian/Ubuntu:

```bash
sudo apt install ./Tsuru*.deb
```

Fedora/RHEL/openSUSE:

```bash
sudo zypper install ./Tsuru*.rpm
```

If your desktop asks for confirmation because the binary is from the internet, allow
execution and continue.

### Development

Prerequisites: [Node.js](https://nodejs.org/) 20+, [pnpm](https://pnpm.io/installation) and
[Rust](https://www.rust-lang.org/tools/install).

```bash
# Clone the repository
git clone <repository-url>
cd Tsuru

# Install dependencies
pnpm install

# Start the app in development mode
pnpm launch

# Build a debug binary without bundling
pnpm tauri build --debug --no-bundle
```

### Useful commands

| Command | Description |
| --- | --- |
| `pnpm launch` | Run the Tauri app in development mode. |
| `pnpm dev` | Start the Vite dev server only (browser preview without Tauri). |
| `pnpm build` | Type-check with `vue-tsc` and build the frontend. |
| `pnpm test` | Run the Vitest suite. |
| `pnpm lint` | Lint and auto-fix with ESLint. |
| `cargo test` | Run the Rust codec and binding tests (from `src-tauri/`). |

### Releasing

Releases are cut with the [Release workflow](.github/workflows/release.yml):

1. Run the workflow manually (`workflow_dispatch`) with the version to release
   (e.g. `0.0.2`).
2. It bumps `package.json`, `src-tauri/tauri.conf.json` and `src-tauri/Cargo.toml`,
   opens a version-bump PR against `tauri`, tags `v<version>` and creates the GitHub
   release from `release_notes.md`.
3. Build jobs then produce macOS (Apple Silicon + Intel), Windows and Linux artifacts
   and attach them to the release, including signed updater bundles and a `latest.json`
   manifest consumed by the in-app updater.

**Required repository secrets** for signing the updater artifacts (generate a keypair
with `pnpm tauri signer generate`):

- `TAURI_SIGNING_PRIVATE_KEY` — contents of the private key file.
- `TAURI_SIGNING_PRIVATE_KEY_PASSWORD` — the key password (may stay empty for a
  passwordless key).

## Contributing

We welcome contributions from the community to improve and expand Tsuru. If you'd like to contribute,
please see our [Contribution Guidelines](CONTRIBUTING.md) for more information.

## License

Tsuru is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Recommended IDE Setup

- [VS Code](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) + [Tauri](https://marketplace.visualstudio.com/items?itemName=tauri-apps.tauri-vscode) + [rust-analyzer](https://marketplace.visualstudio.com/items?itemName=rust-lang.rust-analyzer)

## Type Support For `.vue` Imports in TS

Since TypeScript cannot handle type information for `.vue` imports, they are shimmed to be a generic Vue component type by default. In most cases this is fine if you don't really care about component prop types outside of templates. However, if you wish to get actual prop types in `.vue` imports (for example to get props validation when using manual `h(...)` calls), you can enable Volar's Take Over mode by following these steps:

1. Run `Extensions: Show Built-in Extensions` from VS Code's command palette, look for `TypeScript and JavaScript Language Features`, then right click and select `Disable (Workspace)`. By default, Take Over mode will enable itself if the default TypeScript extension is disabled.
2. Reload the VS Code window by running `Developer: Reload Window` from the command palette.

You can learn more about Take Over mode [here](https://github.com/johnsoncodehk/volar/discussions/471).
