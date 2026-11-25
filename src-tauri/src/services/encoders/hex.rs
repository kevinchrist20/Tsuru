pub fn encode(input: &str, uppercase: bool) -> String {
    let hex = input
        .as_bytes()
        .iter()
        .map(|byte| format!("{byte:02x}"))
        .collect::<String>();

    if uppercase {
        hex.to_uppercase()
    } else {
        hex
    }
}

pub fn decode(input: &str) -> Result<String, String> {
    let mut cleaned = input
        .chars()
        .filter(|character| !character.is_ascii_whitespace())
        .collect::<String>();

    if cleaned.len() >= 2 && (cleaned.starts_with("0x") || cleaned.starts_with("0X")) {
        cleaned = cleaned[2..].to_string();
    }

    if cleaned.is_empty() {
        return Ok(String::new());
    }

    if cleaned.len() % 2 != 0 {
        return Err("Hex input must contain an even number of digits.".to_string());
    }

    let bytes = (0..cleaned.len() / 2)
        .map(|index| {
            let pair = &cleaned[index * 2..index * 2 + 2];
            u8::from_str_radix(pair, 16)
                .map_err(|_| format!("Invalid hex digits at position {}.", index * 2 + 1))
        })
        .collect::<Result<Vec<u8>, String>>()?;

    String::from_utf8(bytes).map_err(|_| "Decoded data is not valid UTF-8.".to_string())
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn encodes_hex() {
        assert_eq!(encode("hello", false), "68656c6c6f");
        assert_eq!(encode("hello", true), "68656C6C6F");
        assert_eq!(encode("", false), "");
    }

    #[test]
    fn decodes_hex() {
        assert_eq!(decode("68656c6c6f").unwrap(), "hello");
        assert_eq!(decode("68656C6C6F").unwrap(), "hello");
        assert_eq!(decode("68 65 6c 6c 6f").unwrap(), "hello");
        assert_eq!(decode("0x68656c6c6f").unwrap(), "hello");
        assert_eq!(decode("").unwrap(), "");
    }

    #[test]
    fn rejects_odd_length() {
        let error = decode("abc").unwrap_err();
        assert!(error.contains("even number of digits"));
    }

    #[test]
    fn rejects_invalid_digits() {
        let error = decode("zz").unwrap_err();
        assert!(error.contains("Invalid hex digits"));
    }

    #[test]
    fn rejects_invalid_utf8() {
        assert!(decode("ff").is_err());
    }

    #[test]
    fn round_trips_unicode() {
        let text = "Tsuru 🦢";
        assert_eq!(decode(&encode(text, true)).unwrap(), text);
    }
}
