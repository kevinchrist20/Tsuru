use base64::{
    engine::general_purpose::{
        GeneralPurpose, STANDARD, STANDARD_NO_PAD, STANDARD_PAD_INDIFFERENT, URL_SAFE,
        URL_SAFE_NO_PAD, URL_SAFE_PAD_INDIFFERENT,
    },
    Engine as _,
};

fn encoding_engine(url_safe: bool, padding: bool) -> GeneralPurpose {
    match (url_safe, padding) {
        (false, true) => STANDARD,
        (false, false) => STANDARD_NO_PAD,
        (true, true) => URL_SAFE,
        (true, false) => URL_SAFE_NO_PAD,
    }
}

pub fn encode(input: &str, url_safe: bool, padding: bool, wrap: bool) -> String {
    let encoded = encoding_engine(url_safe, padding).encode(input);

    if wrap {
        wrap_lines(&encoded, 76)
    } else {
        encoded
    }
}

/// RFC 2045 limits encoded lines to 76 characters including the soft break.
fn wrap_lines(encoded: &str, width: usize) -> String {
    encoded
        .as_bytes()
        .chunks(width)
        .map(|chunk| std::str::from_utf8(chunk).expect("Base64 output is ASCII"))
        .collect::<Vec<&str>>()
        .join("\r\n")
}

pub fn decode(input: &str, url_safe: bool, _padding: bool) -> Result<String, String> {
    let cleaned = input
        .chars()
        .filter(|c| !c.is_ascii_whitespace())
        .collect::<String>();

    if cleaned.is_empty() {
        return Ok(String::new());
    }

    // Accept both padded and unpadded input, and fall back to the other alphabet so
    // users do not need to flip the URL-safe toggle just to decode a value.
    let primary = if url_safe {
        URL_SAFE_PAD_INDIFFERENT
    } else {
        STANDARD_PAD_INDIFFERENT
    };
    let fallback = if url_safe {
        STANDARD_PAD_INDIFFERENT
    } else {
        URL_SAFE_PAD_INDIFFERENT
    };

    let decoded = match primary.decode(&cleaned) {
        Ok(value) => value,
        Err(primary_error) => fallback
            .decode(&cleaned)
            .map_err(|_| format!("Invalid Base64 input: {primary_error}"))?,
    };

    String::from_utf8(decoded).map_err(|e| format!("Decoded data is not valid UTF-8: {e}"))
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn encodes_standard_with_padding() {
        assert_eq!(encode("hello", false, true, false), "aGVsbG8=");
    }

    #[test]
    fn encodes_standard_without_padding() {
        assert_eq!(encode("hello", false, false, false), "aGVsbG8");
    }

    #[test]
    fn encodes_url_safe_alphabet() {
        assert_eq!(encode("???", false, true, false), "Pz8/");
        assert_eq!(encode("???", true, true, false), "Pz8_");
    }

    #[test]
    fn decodes_standard_input() {
        assert_eq!(decode("aGVsbG8=", false, true).unwrap(), "hello");
    }

    #[test]
    fn decodes_unpadded_input() {
        assert_eq!(decode("aGVsbG8", false, true).unwrap(), "hello");
    }

    #[test]
    fn decodes_whitespace_wrapped_input() {
        assert_eq!(decode("aGVs\nbG8=", false, true).unwrap(), "hello");
        assert_eq!(decode(" aGVs\tbG8= ", false, true).unwrap(), "hello");
    }

    #[test]
    fn decodes_url_safe_input() {
        assert_eq!(decode("Pz8_", true, true).unwrap(), "???");
        assert_eq!(decode("Pz8_", false, true).unwrap(), "???");
    }

    #[test]
    fn wraps_long_output_at_76_characters() {
        let text = "a".repeat(200);
        let wrapped = encode(&text, false, true, true);

        for line in wrapped.split("\r\n") {
            assert!(line.chars().count() <= 76, "line too long: {}", line.len());
        }

        assert!(wrapped.contains("\r\n"));
        assert_eq!(decode(&wrapped, false, true).unwrap(), text);
    }

    #[test]
    fn wrapping_short_input_changes_nothing() {
        assert_eq!(encode("hello", false, true, true), "aGVsbG8=");
    }

    #[test]
    fn decodes_empty_input() {
        assert_eq!(decode("", false, true).unwrap(), "");
        assert_eq!(encode("", false, true, false), "");
    }

    #[test]
    fn rejects_invalid_characters() {
        let error = decode("!!!!", false, true).unwrap_err();
        assert!(error.contains("Invalid Base64 input"));
    }

    #[test]
    fn rejects_invalid_utf8() {
        let error = decode("/w==", false, true).unwrap_err();
        assert!(error.contains("UTF-8"));
    }

    #[test]
    fn round_trips_unicode() {
        let text = "Tsuru 🦢 – こんにちは";
        let encoded = encode(text, false, true, false);
        assert_eq!(decode(&encoded, false, true).unwrap(), text);

        let url_safe = encode(text, true, false, false);
        assert_eq!(decode(&url_safe, true, false).unwrap(), text);
    }

    #[test]
    fn round_trips_large_input() {
        let text = "tsuru".repeat(200_000);
        let encoded = encode(&text, false, true, false);
        assert_eq!(decode(&encoded, false, true).unwrap(), text);
    }
}
