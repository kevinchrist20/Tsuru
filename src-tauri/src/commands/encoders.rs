use crate::services::encoders;

#[tauri::command]
pub fn encode_base64(input: &str) -> String {
    encoders::encode(input)
}

#[tauri::command]
pub fn decode_base64(input: &str) -> Result<String, String> {
    encoders::decode(input)
}
