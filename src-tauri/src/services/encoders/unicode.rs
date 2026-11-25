/// Encodes text as `\uXXXX` escapes. Printable ASCII is kept as-is (except backslash);
/// every other character — including newlines — becomes a 4-digit escape, with surrogate
/// pairs for characters outside the BMP.
pub fn encode(input: &str) -> String {
    let mut out = String::with_capacity(input.len());

    for character in input.chars() {
        let code = character as u32;

        if character == '\\' {
            out.push_str("\\\\");
        } else if (0x20..=0x7E).contains(&code) {
            out.push(character);
        } else {
            push_escaped(&mut out, code);
        }
    }

    out
}

/// Decodes `\uXXXX` (including surrogate pairs), `\xXX` and common short escapes.
pub fn decode(input: &str) -> Result<String, String> {
    let characters = input.chars().collect::<Vec<char>>();
    let mut out = String::with_capacity(input.len());
    let mut index = 0;

    while index < characters.len() {
        let character = characters[index];

        if character != '\\' {
            out.push(character);
            index += 1;
            continue;
        }

        if index + 1 >= characters.len() {
            return Err("Trailing backslash at end of input.".to_string());
        }

        let escape = characters[index + 1];

        match escape {
            'u' | 'U' => {
                let (code, consumed) = decode_hex_escape(&characters, index, 4)?;
                index += consumed;

                if (0xD800..=0xDBFF).contains(&code) {
                    let (low, low_consumed) = read_surrogate(&characters, index)?;
                    if !(0xDC00..=0xDFFF).contains(&low) {
                        return Err("Lone high surrogate is not a valid character.".to_string());
                    }
                    let combined = 0x10000 + ((code - 0xD800) << 10) + (low - 0xDC00);
                    push_code_point(&mut out, combined)?;
                    index += low_consumed;
                } else if (0xDC00..=0xDFFF).contains(&code) {
                    return Err("Unexpected low surrogate without its high surrogate.".to_string());
                } else {
                    push_code_point(&mut out, code)?;
                }
            }
            'x' | 'X' => {
                let (code, consumed) = decode_hex_escape(&characters, index, 2)?;
                index += consumed;
                push_code_point(&mut out, code)?;
            }
            'n' => {
                out.push('\n');
                index += 2;
            }
            'r' => {
                out.push('\r');
                index += 2;
            }
            't' => {
                out.push('\t');
                index += 2;
            }
            'b' => {
                out.push('\u{0008}');
                index += 2;
            }
            'f' => {
                out.push('\u{000C}');
                index += 2;
            }
            'v' => {
                out.push('\u{000B}');
                index += 2;
            }
            '0' => {
                out.push('\u{0000}');
                index += 2;
            }
            '\'' | '"' | '\\' => {
                out.push(escape);
                index += 2;
            }
            _ => {
                return Err(format!("Unknown escape sequence: \\{escape}"));
            }
        }
    }

    Ok(out)
}

fn decode_hex_escape(
    characters: &[char],
    index: usize,
    digits: usize,
) -> Result<(u32, usize), String> {
    let start = index + 2;
    let end = start + digits;

    if end > characters.len() {
        return Err(format!("Incomplete \\u escape at position {}.", index));
    }

    let hex = characters[start..end].iter().collect::<String>();

    if !hex.chars().all(|character| character.is_ascii_hexdigit()) {
        return Err(format!("Invalid hex digits in \\u escape: \\u{hex}"));
    }

    let value = u32::from_str_radix(&hex, 16).expect("digits are valid hex");
    Ok((value, 2 + digits))
}

fn read_surrogate(characters: &[char], index: usize) -> Result<(u32, usize), String> {
    if index + 2 > characters.len()
        || characters[index] != '\\'
        || (characters[index + 1] != 'u' && characters[index + 1] != 'U')
    {
        return Err("Lone high surrogate is not a valid character.".to_string());
    }

    decode_hex_escape(characters, index, 4)
}

fn push_code_point(out: &mut String, code: u32) -> Result<(), String> {
    let character =
        char::from_u32(code).ok_or_else(|| format!("Invalid Unicode code point: U+{code:04X}"))?;
    out.push(character);
    Ok(())
}

fn push_escaped(out: &mut String, code: u32) {
    if code > 0xFFFF {
        let value = code - 0x10000;
        let high = 0xD800 + (value >> 10);
        let low = 0xDC00 + (value & 0x3FF);
        out.push_str(&format!("\\u{high:04x}\\u{low:04x}"));
    } else {
        out.push_str(&format!("\\u{code:04x}"));
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn encodes_non_ascii_as_escaped_code_points() {
        assert_eq!(encode("café"), "caf\\u00e9");
        assert_eq!(encode("A\nB"), "A\\u000aB");
        assert_eq!(encode("\t"), "\\u0009");
        assert_eq!(encode(""), "");
    }

    #[test]
    fn keeps_printable_ascii_and_escapes_backslashes() {
        assert_eq!(encode("Hello, \"world\"!"), "Hello, \"world\"!");
        assert_eq!(encode(r"a\b"), "a\\\\b");
    }

    #[test]
    fn encodes_emoji_as_surrogate_pairs() {
        assert_eq!(encode("🦢"), "\\ud83e\\udda2");
    }

    #[test]
    fn decodes_code_point_escapes() {
        assert_eq!(decode("caf\\u00e9").unwrap(), "café");
        assert_eq!(decode("caf\\u00E9").unwrap(), "café");
        assert_eq!(decode("A\\u000aB").unwrap(), "A\nB");
        assert_eq!(decode("\\ud83e\\udda2").unwrap(), "🦢");
        assert_eq!(decode("plain text").unwrap(), "plain text");
    }

    #[test]
    fn decodes_short_escapes() {
        assert_eq!(decode("a\\tb\\nc").unwrap(), "a\tb\nc");
        assert_eq!(decode("\\x41\\x42").unwrap(), "AB");
        assert_eq!(decode("say \\\"hi\\\"").unwrap(), "say \"hi\"");
        assert_eq!(decode("100\\\\%").unwrap(), "100\\%");
    }

    #[test]
    fn rejects_unknown_escapes() {
        let error = decode("\\q").unwrap_err();
        assert!(error.contains("Unknown escape sequence"));
    }

    #[test]
    fn rejects_lone_surrogates() {
        assert!(decode("\\ud83d").is_err());
        assert!(decode("\\ud83dabc").is_err());
        assert!(decode("\\udda2").is_err());
    }

    #[test]
    fn rejects_trailing_backslash() {
        let error = decode("abc\\").unwrap_err();
        assert!(error.contains("Trailing backslash"));
    }

    #[test]
    fn round_trips_unicode() {
        let text = "Tsuru 🦢 – こんにちは\nsecond \"line\"";
        assert_eq!(decode(&encode(text)).unwrap(), text);
    }
}
