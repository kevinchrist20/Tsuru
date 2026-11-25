pub fn encode(input: &str) -> String {
    input
        .as_bytes()
        .iter()
        .map(|byte| format!("{byte:08b}"))
        .collect::<Vec<String>>()
        .join(" ")
}

pub fn decode(input: &str) -> Result<String, String> {
    let has_separators = input
        .chars()
        .any(|character| character.is_ascii_whitespace());
    let bytes = if has_separators {
        decode_separated(input)?
    } else {
        decode_compact(input)?
    };

    if bytes.is_empty() {
        return Ok(String::new());
    }

    String::from_utf8(bytes).map_err(|_| "Decoded data is not valid UTF-8.".to_string())
}

fn decode_separated(input: &str) -> Result<Vec<u8>, String> {
    input
        .split_whitespace()
        .map(|group| parse_group(group, None))
        .collect()
}

fn decode_compact(input: &str) -> Result<Vec<u8>, String> {
    let cleaned = input
        .chars()
        .filter(|character| !character.is_ascii_whitespace())
        .collect::<String>();

    if cleaned.is_empty() {
        return Ok(Vec::new());
    }

    if cleaned.len() % 8 != 0 {
        return Err("Compact binary input must be a multiple of 8 bits.".to_string());
    }

    cleaned
        .as_bytes()
        .chunks(8)
        .enumerate()
        .map(|(index, group)| parse_group(std::str::from_utf8(group).unwrap(), Some(index * 8)))
        .collect()
}

fn parse_group(group: &str, offset: Option<usize>) -> Result<u8, String> {
    let at = offset
        .map(|value| format!(" at position {value}"))
        .unwrap_or_default();

    if group.len() != 8 {
        return Err(format!("Each binary group must be exactly 8 bits{at}."));
    }

    if !group
        .chars()
        .all(|character| character == '0' || character == '1')
    {
        return Err(format!("Binary groups may only contain 0 and 1{at}."));
    }

    u8::from_str_radix(group, 2).map_err(|_| format!("Invalid binary group{at}."))
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn encodes_binary_groups() {
        assert_eq!(encode("hi"), "01101000 01101001");
        assert_eq!(encode(""), "");
    }

    #[test]
    fn decodes_separated_groups() {
        assert_eq!(decode("01101000 01101001").unwrap(), "hi");
        assert_eq!(decode("  01101000   01101001  ").unwrap(), "hi");
    }

    #[test]
    fn decodes_compact_input() {
        assert_eq!(decode("0110100001101001").unwrap(), "hi");
    }

    #[test]
    fn rejects_bad_group_lengths() {
        let error = decode("0110100 01101001").unwrap_err();
        assert!(error.contains("exactly 8 bits"));
    }

    #[test]
    fn rejects_invalid_characters() {
        assert!(decode("0110100a").is_err());
        assert!(decode("01101000 22101001").is_err());
    }

    #[test]
    fn rejects_invalid_utf8() {
        assert!(decode("11111111").is_err());
    }

    #[test]
    fn round_trips_unicode() {
        let text = "Tsuru 🦢";
        assert_eq!(decode(&encode(text)).unwrap(), text);
    }
}
