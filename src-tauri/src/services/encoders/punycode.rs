/// Converts a Unicode domain to punycode (IDN) form, label by label.
/// Non-ASCII labels become `xn--` labels; ASCII labels pass through unchanged.
pub fn encode(input: &str) -> Result<String, String> {
    let trimmed = input.trim();

    if trimmed.is_empty() {
        return Ok(String::new());
    }

    // Domains are case-insensitive; IDNA lookup applies lowercasing first.
    let lowered = trimmed.to_lowercase();

    lowered
        .split('.')
        .map(|label| encode_label(label))
        .collect::<Result<Vec<String>, String>>()
        .map(|labels| labels.join("."))
}

/// Converts a punycode/IDN domain back to Unicode, label by label.
pub fn decode(input: &str) -> Result<String, String> {
    let trimmed = input.trim();

    if trimmed.is_empty() {
        return Ok(String::new());
    }

    trimmed
        .split('.')
        .map(|label| decode_label(label))
        .collect::<Result<Vec<String>, String>>()
        .map(|labels| labels.join("."))
}

fn encode_label(label: &str) -> Result<String, String> {
    if label.is_empty() {
        return Ok(label.to_string());
    }

    if label.is_ascii() {
        return Ok(label.to_string());
    }

    punycode::encode(label)
        .map(|encoded| format!("xn--{encoded}"))
        .map_err(|error| format!("Could not encode label \"{label}\": {error:?}"))
}

fn decode_label(label: &str) -> Result<String, String> {
    let prefix = label
        .strip_prefix("xn--")
        .or_else(|| label.strip_prefix("XN--"));

    let Some(rest) = prefix else {
        return Ok(label.to_string());
    };

    if rest.is_empty() {
        return Err(format!(
            "Punycode label \"{label}\" is missing its payload."
        ));
    }

    punycode::decode(rest).map_err(|error| format!("Could not decode label \"{label}\": {error:?}"))
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn encodes_known_idn_examples() {
        assert_eq!(encode("münchen.de").unwrap(), "xn--mnchen-3ya.de");
        assert_eq!(encode("bücher.example").unwrap(), "xn--bcher-kva.example");
    }

    #[test]
    fn leaves_ascii_domains_untouched() {
        assert_eq!(encode("tsuru.app").unwrap(), "tsuru.app");
        assert_eq!(encode("").unwrap(), "");
    }

    #[test]
    fn lowercases_before_encoding() {
        assert_eq!(encode("MÜNCHEN.DE").unwrap(), "xn--mnchen-3ya.de");
    }

    #[test]
    fn decodes_punycode_labels() {
        assert_eq!(decode("xn--mnchen-3ya.de").unwrap(), "münchen.de");
        // Punycode preserves the case annotation of the original label.
        assert_eq!(decode("xn--Bcher-kva.example").unwrap(), "Bücher.example");
    }

    #[test]
    fn leaves_non_punycode_labels_untouched() {
        assert_eq!(decode("tsuru.app").unwrap(), "tsuru.app");
        assert_eq!(decode("").unwrap(), "");
    }

    #[test]
    fn rejects_malformed_punycode() {
        assert!(decode("xn--!!!.app").is_err());
        assert!(decode("xn--.app").is_err());
    }

    #[test]
    fn round_trips_idn() {
        let domain = "münchen-bücher.example";
        let encoded = encode(domain).unwrap();
        assert_eq!(decode(&encoded).unwrap(), domain);
    }

    #[test]
    fn round_trips_multi_label_domains() {
        let domain = "münchen.日本.example";
        let encoded = encode(domain).unwrap();

        assert!(encoded.starts_with("xn--"));
        assert!(encoded.contains(".xn--"));
        assert_eq!(decode(&encoded).unwrap(), domain);
    }
}
