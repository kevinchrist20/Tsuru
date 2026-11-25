use percent_encoding::{percent_decode_str, utf8_percent_encode, AsciiSet, NON_ALPHANUMERIC};

/// Matches `encodeURIComponent`: everything except `A-Za-z0-9-_.!~*'()`.
const COMPONENT_SET: &AsciiSet = &NON_ALPHANUMERIC
    .remove(b'-')
    .remove(b'.')
    .remove(b'_')
    .remove(b'~')
    .remove(b'!')
    .remove(b'*')
    .remove(b'\'')
    .remove(b'(')
    .remove(b')');

/// Matches `encodeURI`: component mode plus URI-reserved characters.
const FULL_URL_SET: &AsciiSet = &COMPONENT_SET
    .remove(b'$')
    .remove(b'&')
    .remove(b'+')
    .remove(b',')
    .remove(b';')
    .remove(b'=')
    .remove(b':')
    .remove(b'/')
    .remove(b'?')
    .remove(b'#')
    .remove(b'[')
    .remove(b']')
    .remove(b'@');

pub const COMPONENT_MODE: &str = "component";
pub const FULL_URL_MODE: &str = "url";

pub fn encode(input: &str, mode: &str) -> Result<String, String> {
    match mode {
        COMPONENT_MODE => Ok(utf8_percent_encode(input, COMPONENT_SET).to_string()),
        FULL_URL_MODE => Ok(utf8_percent_encode(input, FULL_URL_SET).to_string()),
        _ => Err(format!("Unknown URL encoding mode: {mode}")),
    }
}

pub fn decode(input: &str) -> Result<String, String> {
    percent_decode_str(input)
        .decode_utf8()
        .map(|decoded| decoded.to_string())
        .map_err(|_| "Decoded data is not valid UTF-8.".to_string())
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn encodes_components() {
        assert_eq!(
            encode("hello world", COMPONENT_MODE).unwrap(),
            "hello%20world"
        );
        assert_eq!(encode("a&b=c/d", COMPONENT_MODE).unwrap(), "a%26b%3Dc%2Fd");
        assert_eq!(encode("café", COMPONENT_MODE).unwrap(), "caf%C3%A9");
        // encodeURIComponent keeps the unreserved set.
        assert_eq!(encode("a-._~*'()", COMPONENT_MODE).unwrap(), "a-._~*'()");
    }

    #[test]
    fn encodes_full_urls() {
        assert_eq!(
            encode("https://tsuru.app/search?q=hello world", FULL_URL_MODE).unwrap(),
            "https://tsuru.app/search?q=hello%20world"
        );
        // URI-reserved characters are kept in full-URL mode.
        assert_eq!(
            encode("a&b=c/d?e#f[g]h@i:j;k,l+~*!'()-._$", FULL_URL_MODE).unwrap(),
            "a&b=c/d?e#f[g]h@i:j;k,l+~*!'()-._$"
        );
    }

    #[test]
    fn rejects_unknown_mode() {
        assert!(encode("x", "nope").is_err());
    }

    #[test]
    fn decodes_percent_sequences() {
        assert_eq!(decode("hello%20world").unwrap(), "hello world");
        assert_eq!(decode("caf%C3%A9").unwrap(), "café");
    }

    #[test]
    fn leaves_stray_percent_signs_in_place() {
        assert_eq!(decode("100%").unwrap(), "100%");
    }

    #[test]
    fn rejects_invalid_utf8() {
        assert!(decode("%FF").is_err());
    }

    #[test]
    fn round_trips_unicode() {
        let text = "Tsuru 🦢 – /?q=&x";
        let encoded = encode(text, COMPONENT_MODE).unwrap();
        assert_eq!(decode(&encoded).unwrap(), text);
    }
}
