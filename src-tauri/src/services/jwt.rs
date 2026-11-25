use base64::engine::general_purpose::URL_SAFE_PAD_INDIFFERENT;
use base64::Engine as _;
use serde::Serialize;
use ts_rs::TS;

#[derive(Debug, Serialize, TS)]
#[serde(rename_all = "camelCase")]
#[ts(export, rename_all = "camelCase")]
pub struct JwtParts {
    pub header: String,
    pub payload: String,
    pub signature: String,
    pub algorithm: String,
}

pub fn decode_jwt(token: &str) -> Result<JwtParts, String> {
    let trimmed = token.trim();
    let segments = trimmed.split('.').collect::<Vec<&str>>();

    if segments.len() != 3 {
        return Err("Expected a JWT in the form header.payload.signature.".to_string());
    }

    let header_bytes = decode_segment(segments[0], "header")?;
    let payload_bytes = decode_segment(segments[1], "payload")?;

    let header_json: serde_json::Value = serde_json::from_slice(&header_bytes)
        .map_err(|_| "The header is not valid JSON.".to_string())?;
    let payload_json: serde_json::Value = serde_json::from_slice(&payload_bytes)
        .map_err(|_| "The payload is not valid JSON.".to_string())?;

    let algorithm = header_json
        .get("alg")
        .and_then(|value| value.as_str())
        .unwrap_or("unknown")
        .to_string();

    let header = serde_json::to_string_pretty(&header_json)
        .map_err(|_| "Could not format the header.".to_string())?;
    let payload = serde_json::to_string_pretty(&payload_json)
        .map_err(|_| "Could not format the payload.".to_string())?;

    Ok(JwtParts {
        header,
        payload,
        signature: segments[2].to_string(),
        algorithm,
    })
}

fn decode_segment(segment: &str, part: &str) -> Result<Vec<u8>, String> {
    let cleaned = segment
        .chars()
        .filter(|character| !character.is_ascii_whitespace())
        .collect::<String>();

    if cleaned.is_empty() {
        return Err(format!("The {part} segment is empty."));
    }

    URL_SAFE_PAD_INDIFFERENT
        .decode(cleaned.as_bytes())
        .map_err(|_| format!("The {part} segment is not valid Base64URL."))
}

#[cfg(test)]
mod tests {
    use super::*;
    use base64::engine::general_purpose::URL_SAFE_NO_PAD;
    use serde_json::json;

    fn encode_segment(value: &serde_json::Value) -> String {
        URL_SAFE_NO_PAD.encode(value.to_string().as_bytes())
    }

    #[test]
    fn decodes_a_valid_token() {
        let header = json!({"alg": "HS256", "typ": "JWT"});
        let payload = json!({"sub": "1234567890", "name": "Tsuru", "admin": false});
        let token = format!(
            "{}.{}.c2lnbmF0dXJl",
            encode_segment(&header),
            encode_segment(&payload)
        );

        let parts = decode_jwt(&token).unwrap();

        assert!(parts.header.contains("\"alg\": \"HS256\""));
        assert!(parts.payload.contains("\"sub\": \"1234567890\""));
        assert_eq!(parts.signature, "c2lnbmF0dXJl");
        assert_eq!(parts.algorithm, "HS256");
    }

    #[test]
    fn accepts_padded_segments() {
        let header = json!({"alg": "none"});
        let payload = json!({"sub": "42"});
        let token = format!(
            "{}.{}.sig",
            encode_segment(&header),
            encode_segment(&payload)
        );

        let parts = decode_jwt(&token).unwrap();
        assert_eq!(parts.algorithm, "none");
    }

    #[test]
    fn rejects_wrong_segment_count() {
        let error = decode_jwt("abc.def").unwrap_err();
        assert!(error.contains("header.payload.signature"));
    }

    #[test]
    fn rejects_invalid_base64() {
        let error = decode_jwt("!!!!.aGVsbG8.sig").unwrap_err();
        assert!(error.contains("header segment"));
    }

    #[test]
    fn rejects_non_json_header() {
        let header = URL_SAFE_NO_PAD.encode(b"not json");
        let payload = encode_segment(&json!({"sub": "1"}));
        let error = decode_jwt(&format!("{header}.{payload}.sig")).unwrap_err();

        assert!(error.contains("header is not valid JSON"));
    }

    #[test]
    fn rejects_non_json_payload() {
        let header = encode_segment(&json!({"alg": "HS256"}));
        let payload = URL_SAFE_NO_PAD.encode(b"nope");
        let error = decode_jwt(&format!("{header}.{payload}.sig")).unwrap_err();

        assert!(error.contains("payload is not valid JSON"));
    }

    #[test]
    fn reports_unknown_algorithm_when_missing() {
        let header = encode_segment(&json!({"typ": "JWT"}));
        let payload = encode_segment(&json!({"sub": "1"}));
        let parts = decode_jwt(&format!("{header}.{payload}.sig")).unwrap();

        assert_eq!(parts.algorithm, "unknown");
    }
}
