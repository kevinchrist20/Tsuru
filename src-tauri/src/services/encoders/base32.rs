use data_encoding::{BASE32, BASE32_NOPAD};

pub fn encode(input: &str, padding: bool) -> String {
    if padding {
        BASE32.encode(input.as_bytes())
    } else {
        BASE32_NOPAD.encode(input.as_bytes())
    }
}

pub fn decode(input: &str) -> Result<String, String> {
    let cleaned = input
        .chars()
        .filter(|character| !character.is_ascii_whitespace())
        .collect::<String>();

    if cleaned.is_empty() {
        return Ok(String::new());
    }

    // Accept lowercase input and both padded and unpadded payloads.
    let normalized = cleaned.to_uppercase();
    let bytes = BASE32
        .decode(normalized.as_bytes())
        .or_else(|_| BASE32_NOPAD.decode(normalized.as_bytes()))
        .map_err(|_| "Invalid Base32 input.".to_string())?;

    String::from_utf8(bytes).map_err(|_| "Decoded data is not valid UTF-8.".to_string())
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn encodes_rfc4648_vectors() {
        assert_eq!(encode("", true), "");
        assert_eq!(encode("f", true), "MY======");
        assert_eq!(encode("fo", true), "MZXQ====");
        assert_eq!(encode("foo", true), "MZXW6===");
        assert_eq!(encode("foobar", true), "MZXW6YTBOI======");
    }

    #[test]
    fn encodes_without_padding() {
        assert_eq!(encode("foobar", false), "MZXW6YTBOI");
    }

    #[test]
    fn decodes_padded_and_unpadded() {
        assert_eq!(decode("MZXW6YTBOI======").unwrap(), "foobar");
        assert_eq!(decode("MZXW6YTBOI").unwrap(), "foobar");
    }

    #[test]
    fn accepts_lowercase_input() {
        assert_eq!(decode("mzxw6ytboi======").unwrap(), "foobar");
    }

    #[test]
    fn ignores_whitespace() {
        assert_eq!(decode("MZXW6 YTBOI ======").unwrap(), "foobar");
    }

    #[test]
    fn rejects_invalid_characters() {
        assert!(decode("MZXW6YTBO1======").is_err());
        assert!(decode("!!!").is_err());
    }

    #[test]
    fn rejects_invalid_utf8() {
        assert!(decode("7777").is_err());
    }

    #[test]
    fn round_trips_unicode() {
        let text = "Tsuru 🦢";
        assert_eq!(decode(&encode(text, true)).unwrap(), text);
        assert_eq!(decode(&encode(text, false)).unwrap(), text);
    }
}
