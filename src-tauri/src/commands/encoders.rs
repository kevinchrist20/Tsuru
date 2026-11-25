use crate::services::encoders;

#[tauri::command]
pub fn encode_base64(input: &str, url_safe: bool, padding: bool) -> String {
    encoders::encode(input, url_safe, padding)
}

#[tauri::command]
pub fn decode_base64(input: &str, url_safe: bool, padding: bool) -> Result<String, String> {
    encoders::decode(input, url_safe, padding)
}
