# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and this
project adheres to [Semantic Versioning](https://semver.org/).

## [0.0.1] - 2026-11-25

### Added

- Initial Tsuru MVP on Tauri 2 + Vue 3 with a shared codec UI (`CodecPanel`, `ModeTabs`,
  `ValuePanel`, `useCodec`) and a view registry that routes every tool.
- **Encoders & Decoders**: Base64 (URL-safe alphabet, padding, RFC 2045 line wrapping),
  Base64 Image (local JPG/PNG, drag & drop, 8 MB limit), URL (component and full-URL
  modes), HTML entities, Hex, Binary, Base32, Unicode escapes (surrogate pairs),
  Punycode/IDN and Quoted-Printable (RFC 2045 soft breaks).
- **Generators**: Slugs (accent transliteration) and UUID (batches of 1/5/10).
- **Cryptography & Security**: JWT Decoder (decode-only, with an explicit
  "not verified" warning).
- Dashboard with tool search, category grouping and a coming-soon fallback for
  unregistered routes.
- Typed Tauri IPC: auto-generated command bindings plus ts-rs export of structured
  payloads (`JwtParts`).
- Test suites: 93 Rust unit tests across all codecs and 29 Vitest tests for the shared
  UI logic.
