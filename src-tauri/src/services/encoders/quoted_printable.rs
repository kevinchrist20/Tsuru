/// Soft-break width: content lines are capped at 73 characters so a trailing
/// space/tab that must be encoded (2 extra characters) still leaves room for the
/// `=` of the soft break within RFC 2045's 76-character limit.
const MAX_CONTENT_WIDTH: usize = 73;

struct Atom {
    /// Length when rendered as-is (literal character).
    len_plain: usize,
    plain: String,
    encoded: String,
    is_soft: bool,
}

/// Encodes text as quoted-printable (RFC 2045): printable ASCII stays literal,
/// `=` becomes `=3D`, everything else becomes `=XX`, and lines wrap at 76
/// characters with `=` soft breaks.
pub fn encode(input: &str) -> String {
    let atoms = to_atoms(input);
    let mut lines: Vec<String> = Vec::new();
    let mut line = String::new();
    let mut line_len = 0usize;
    let mut trailing_soft: Option<(usize, String)> = None;

    for atom in &atoms {
        if line_len + atom.len_plain > MAX_CONTENT_WIDTH {
            flush_soft(&mut line, &mut line_len, trailing_soft.take());
            line.push('=');
            line.push_str("\r\n");
            lines.push(std::mem::take(&mut line));
            line_len = 0;
        }

        line.push_str(&atom.plain);
        line_len += atom.len_plain;

        if atom.is_soft {
            trailing_soft = Some((line_len, atom.encoded.clone()));
        } else {
            trailing_soft = None;
        }
    }

    flush_soft(&mut line, &mut line_len, trailing_soft.take());
    lines.push(line);
    lines.join("")
}

fn flush_soft(line: &mut String, line_len: &mut usize, trailing_soft: Option<(usize, String)>) {
    if let Some((position, encoded)) = trailing_soft {
        line.truncate(position - 1);
        line.push_str(&encoded);
        *line_len += 2;
    }
}

fn to_atoms(input: &str) -> Vec<Atom> {
    input
        .as_bytes()
        .iter()
        .map(|byte| match byte {
            b'=' => Atom {
                len_plain: 3,
                plain: "=3D".to_string(),
                encoded: "=3D".to_string(),
                is_soft: false,
            },
            b'\r' => Atom {
                len_plain: 3,
                plain: "=0D".to_string(),
                encoded: "=0D".to_string(),
                is_soft: false,
            },
            b'\n' => Atom {
                len_plain: 3,
                plain: "=0A".to_string(),
                encoded: "=0A".to_string(),
                is_soft: false,
            },
            b' ' | b'\t' => {
                let character = *byte as char;
                Atom {
                    len_plain: 1,
                    plain: character.to_string(),
                    encoded: format!("={byte:02X}"),
                    is_soft: true,
                }
            }
            0x21..=0x7E => Atom {
                len_plain: 1,
                plain: (*byte as char).to_string(),
                encoded: (*byte as char).to_string(),
                is_soft: false,
            },
            _ => Atom {
                len_plain: 3,
                plain: format!("={byte:02X}"),
                encoded: format!("={byte:02X}"),
                is_soft: false,
            },
        })
        .collect()
}

/// Decodes quoted-printable text: removes soft breaks, drops trailing
/// whitespace per line, and expands `=XX` sequences.
pub fn decode(input: &str) -> Result<String, String> {
    let normalized = input.replace("\r\n", "\n");
    let mut bytes: Vec<u8> = Vec::new();

    for (line_index, raw_line) in normalized.split('\n').enumerate() {
        let had_soft_break = raw_line.ends_with('=');
        let mut line = if had_soft_break {
            &raw_line[..raw_line.len() - 1]
        } else {
            raw_line
        };

        // RFC 2045: trailing white space on a line is not part of the content.
        line = line.trim_end_matches([' ', '\t']);

        let characters = line.chars().collect::<Vec<char>>();
        let mut index = 0;

        while index < characters.len() {
            let character = characters[index];

            if character != '=' {
                let mut buffer = [0u8; 4];
                bytes.extend_from_slice(character.encode_utf8(&mut buffer).as_bytes());
                index += 1;
                continue;
            }

            if index + 2 >= characters.len() {
                return Err(invalid_sequence_error(line_index));
            }

            let hex = characters[index + 1..index + 3].iter().collect::<String>();

            if hex.len() != 2 || !hex.chars().all(|character| character.is_ascii_hexdigit()) {
                return Err(invalid_sequence_error(line_index));
            }

            bytes.push(u8::from_str_radix(&hex, 16).expect("hex digits are valid"));
            index += 3;
        }
    }

    String::from_utf8(bytes).map_err(|_| "Decoded data is not valid UTF-8.".to_string())
}

fn invalid_sequence_error(line_index: usize) -> String {
    format!(
        "Invalid quoted-printable sequence on line {}.",
        line_index + 1
    )
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn encodes_printable_ascii_literally() {
        assert_eq!(encode("hello world"), "hello world");
        assert_eq!(encode(""), "");
    }

    #[test]
    fn encodes_equals_and_non_ascii() {
        assert_eq!(encode("a=b"), "a=3Db");
        assert_eq!(encode("café"), "caf=C3=A9");
        assert_eq!(encode("new\r\nline"), "new=0D=0Aline");
    }

    #[test]
    fn encodes_trailing_whitespace_at_line_ends() {
        assert_eq!(encode("abc "), "abc=20");
        assert_eq!(encode("abc\t"), "abc=09");
        assert_eq!(encode("a b"), "a b");
    }

    #[test]
    fn wraps_long_lines_within_rfc_limits() {
        let text = "word ".repeat(60);
        let encoded = encode(&text);

        for line in encoded.split("\r\n") {
            assert!(
                line.chars().count() <= 76,
                "line is {} characters: {line:?}",
                line.chars().count()
            );
        }

        assert!(encoded.contains("=\r\n"));
        assert_eq!(decode(&encoded).unwrap(), text);
    }

    #[test]
    fn encodes_trailing_space_that_follows_a_break() {
        // 73 'a's fill the first line; the trailing space starts a new line and
        // must be encoded there because it is trailing whitespace.
        let text = format!("{} ", "a".repeat(73));
        let encoded = encode(&text);

        assert!(encoded.ends_with("=20"));
        assert_eq!(decode(&encoded).unwrap(), text);
    }

    #[test]
    fn keeps_space_literal_when_not_trailing() {
        let text = format!("{} b", "a".repeat(73));
        let encoded = encode(&text);

        assert!(encoded.contains("=\r\n"));
        assert!(encoded.ends_with(" b"));
        assert_eq!(decode(&encoded).unwrap(), text);
    }

    #[test]
    fn decodes_hex_sequences() {
        assert_eq!(decode("hello=20world").unwrap(), "hello world");
        assert_eq!(decode("caf=C3=A9").unwrap(), "café");
        assert_eq!(decode("a=3Db").unwrap(), "a=b");
        assert_eq!(decode("").unwrap(), "");
    }

    #[test]
    fn removes_soft_breaks() {
        assert_eq!(decode("foo=\r\nbar").unwrap(), "foobar");
        assert_eq!(decode("foo=\nbar").unwrap(), "foobar");
    }

    #[test]
    fn strips_trailing_whitespace() {
        assert_eq!(decode("foo \r\nbar").unwrap(), "foobar");
        assert_eq!(decode("foo\t\nbar").unwrap(), "foobar");
    }

    #[test]
    fn rejects_invalid_sequences() {
        assert!(decode("a=b").is_err());
        assert!(decode("=G1").is_err());
        assert!(decode("=A").is_err());
    }

    #[test]
    fn rejects_invalid_utf8() {
        assert!(decode("=FF").is_err());
    }

    #[test]
    fn round_trips_unicode() {
        let text = "Tsuru 🦢 – a=long=body with\ttabs and  spaces";
        assert_eq!(decode(&encode(text)).unwrap(), text);
    }
}
