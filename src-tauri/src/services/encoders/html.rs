use html_escape::decode_html_entities;

pub fn encode(input: &str) -> String {
    let mut encoded = String::with_capacity(input.len());

    for character in input.chars() {
        match character {
            '&' => encoded.push_str("&amp;"),
            '<' => encoded.push_str("&lt;"),
            '>' => encoded.push_str("&gt;"),
            '"' => encoded.push_str("&quot;"),
            '\'' => encoded.push_str("&#39;"),
            _ => encoded.push(character),
        }
    }

    encoded
}

pub fn decode(input: &str) -> Result<String, String> {
    Ok(decode_html_entities(input).to_string())
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn encodes_special_characters() {
        assert_eq!(encode("<script>"), "&lt;script&gt;");
        assert_eq!(encode("Tom & Jerry"), "Tom &amp; Jerry");
        assert_eq!(
            encode(r#""quoted" & <tag> 'it's'"#),
            "&quot;quoted&quot; &amp; &lt;tag&gt; &#39;it&#39;s&#39;"
        );
    }

    #[test]
    fn leaves_safe_text_alone() {
        assert_eq!(encode("Hello, world!"), "Hello, world!");
        assert_eq!(encode(""), "");
    }

    #[test]
    fn decodes_named_entities() {
        assert_eq!(decode("&lt;script&gt;").unwrap(), "<script>");
        assert_eq!(decode("Tom &amp; Jerry").unwrap(), "Tom & Jerry");
        assert_eq!(decode("&quot;quoted&quot;").unwrap(), "\"quoted\"");
    }

    #[test]
    fn decodes_numeric_entities() {
        assert_eq!(decode("&#72;&#101;llo").unwrap(), "Hello");
        assert_eq!(decode("&#x48;i").unwrap(), "Hi");
        assert_eq!(decode("&#x1F600;").unwrap(), "\u{1F600}");
    }

    #[test]
    fn leaves_unknown_entities_in_place() {
        assert_eq!(decode("&nosuchentity;").unwrap(), "&nosuchentity;");
        assert_eq!(decode("100% & more").unwrap(), "100% & more");
    }

    #[test]
    fn round_trips_text() {
        let text = "Tsuru <b>rocks</b> & \"quotes\" 'too'";
        assert_eq!(decode(&encode(text)).unwrap(), text);
    }
}
