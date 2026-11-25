# Tsuru 0.0.1

## Highlights

- First public release of Tsuru, an offline-first developer toolbox built with Tauri 2
  and Vue 3.
- Ten encoders and decoders ship out of the box: Base64 (with URL-safe alphabet, padding
  and line-wrapping options), Base64 Image with drag & drop and live preview, URL, HTML
  entities, Hex, Binary, Base32, Unicode escapes, Punycode/IDN and Quoted-Printable.
- Two generators: SEO-friendly Slugs with accent transliteration and batch UUID v4
  generation (1, 5 or 10 at a time).
- A JWT Decoder that pretty-prints the header and payload of any token, with a clear
  warning that signatures are never verified.
- One consistent interface across every tool: encode/decode tabs, debounced live output,
  character counts, inline error messages, copy/clear actions and an info modal with
  usage notes.
- Fully offline: no network calls, no accounts, no telemetry. Everything runs locally on
  your machine.

## Platforms

- macOS builds for Apple Silicon (aarch64) and Intel (x86_64).
- Windows installers (MSI/NSIS).
- Linux packages (deb, rpm, AppImage).

## Notes

- This is the first release, so expect rough edges. Bug reports and feature requests are
  very welcome on the issue tracker.
- The full roadmap lives in `REQUIREMENTS.md` and `docs/encoders-plan.md`; upcoming work
  includes a JSON formatter/validator, a JSON Crack-style visualizer, YAML support and
  quicktype-style model generation for Dart.
