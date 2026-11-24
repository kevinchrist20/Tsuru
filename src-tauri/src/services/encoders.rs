use base64::{engine::general_purpose, Engine as _};

pub fn encode(input: &str) -> String {
    general_purpose::STANDARD.encode(input)
}

pub fn decode(input: &str) -> Result<String, String> {
    let decoded = general_purpose::STANDARD
        .decode(input)
        .map_err(|e| e.to_string())?;

    String::from_utf8(decoded).map_err(|e| e.to_string())
}
