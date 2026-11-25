# Requirements

Tsuru's tool roadmap, synced with the README. ✅ marks tools that are already shipped.

All tools follow the project principles: fully offline, no telemetry, no accounts, and
crypto-adjacent tools stay decode-only unless explicitly noted.

## Encoders & Decoders

- [x] **Base64 Encoder/Decoder** — URL-safe alphabet, padding and RFC 2045 line-wrapping options
- [x] **Base64 Image Encoder/Decoder** — local JPG/PNG, drag & drop, 8 MB limit
- [x] **URL Encoder/Decoder** — component (`encodeURIComponent`) and full-URL (`encodeURI`) modes
- [x] **HTML Entity Encoder/Decoder** — named and numeric character references
- [x] **Hex Encoder/Decoder** — UTF-8 bytes ↔ hex, uppercase toggle
- [x] **Binary Encoder/Decoder** — spaced or compact 8-bit groups
- [x] **Base32 Encoder/Decoder** — RFC 4648, optional padding
- [x] **Unicode Escape/Unescape** — `\uXXXX` incl. surrogate pairs, `\xXX`, short escapes
- [x] **Punycode / IDN Converter** — Unicode domains ↔ `xn--`, label by label
- [x] **Quoted-Printable Encoder/Decoder** — RFC 2045 with 76-character soft breaks
- [ ] **JSON Formatter / Validator / Minifier** — beautify, validate and minify with error reporting
- [ ] **JSON Crack–style visualizer** — interactive graph explorer (https://jsoncrack.com)
- [ ] **Yaml Crack–style YAML explorer** — graph explorer + YAML ↔ JSON
- [ ] **JSON → Dart model generator** — quicktype-style class generation (https://quicktype.io/dart),
      with TypeScript/Kotlin targets to follow
- [ ] **JSON ↔ XML** and **CSV ↔ JSON** conversion
- [ ] **Query String Parser** and **URL Parser**
- [ ] **Number Base Converter** — binary, octal, decimal, hex and arbitrary bases

## Generators

- [x] **Slugs** — SEO-friendly slugs with accent transliteration
- [x] **UUID** — v4, batches of 1 / 5 / 10

## Cryptography & Security

- [x] **JWT Decoder** — decode-only, signatures are never verified
- [ ] **Hash Generator** — MD5, SHA-1, SHA-256, SHA-512 for text and files
- [ ] **HMAC Generator** — keyed hashes for the same algorithms
- [ ] **Password Generator** — configurable length and character sets with strength hints

## Text

- [ ] **Clean Paste** — strip formatting, smart quotes and invisible characters
- [ ] **Case Converter** — camelCase, snake_case, kebab-case, PascalCase, CONSTANT_CASE, Title Case
- [ ] **Text Diff Checker** — side-by-side comparison
- [ ] **Markdown Previewer** — live preview with copy-as-HTML
- [ ] **Lorem Ipsum Generator** — paragraphs, sentences or words

## Validation

- [ ] **Regex Tester** — match highlighting and capture groups
- [ ] **Common validators** — email, URL, IPv4/IPv6, credit card (Luhn), JSON Schema

## Everyday helpers

- [ ] **Unix Timestamp Converter** — epoch ↔ dates in local time and UTC
- [ ] **Cron Expression Parser** — human-readable schedule explanations
- [ ] **Color Converter** — HEX ↔ RGB ↔ HSL with a picker
- [ ] **QR Code Generator** — offline codes for text, URLs and Wi-Fi credentials
- [ ] **ASCII / Unicode Table** — quick character reference with search
