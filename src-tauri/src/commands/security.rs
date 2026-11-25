use crate::services::jwt::{self, JwtParts};

#[tauri::command]
pub fn decode_jwt(token: &str) -> Result<JwtParts, String> {
    jwt::decode_jwt(token)
}
