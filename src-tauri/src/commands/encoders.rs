use crate::services::encoders::{
    base32, base64, binary, hex, html, punycode, quoted_printable, unicode, url,
};

#[tauri::command]
pub fn encode_base64(input: &str, url_safe: bool, padding: bool, wrap: bool) -> String {
    base64::encode(input, url_safe, padding, wrap)
}

#[tauri::command]
pub fn decode_base64(input: &str, url_safe: bool, padding: bool) -> Result<String, String> {
    base64::decode(input, url_safe, padding)
}

#[tauri::command]
pub fn encode_url(input: &str, mode: &str) -> Result<String, String> {
    url::encode(input, mode)
}

#[tauri::command]
pub fn decode_url(input: &str) -> Result<String, String> {
    url::decode(input)
}

#[tauri::command]
pub fn encode_html(input: &str) -> String {
    html::encode(input)
}

#[tauri::command]
pub fn decode_html(input: &str) -> Result<String, String> {
    html::decode(input)
}

#[tauri::command]
pub fn encode_hex(input: &str, uppercase: bool) -> String {
    hex::encode(input, uppercase)
}

#[tauri::command]
pub fn decode_hex(input: &str) -> Result<String, String> {
    hex::decode(input)
}

#[tauri::command]
pub fn encode_binary(input: &str) -> String {
    binary::encode(input)
}

#[tauri::command]
pub fn decode_binary(input: &str) -> Result<String, String> {
    binary::decode(input)
}

#[tauri::command]
pub fn encode_base32(input: &str, padding: bool) -> String {
    base32::encode(input, padding)
}

#[tauri::command]
pub fn decode_base32(input: &str) -> Result<String, String> {
    base32::decode(input)
}

#[tauri::command]
pub fn encode_unicode(input: &str) -> String {
    unicode::encode(input)
}

#[tauri::command]
pub fn decode_unicode(input: &str) -> Result<String, String> {
    unicode::decode(input)
}

#[tauri::command]
pub fn encode_punycode(input: &str) -> Result<String, String> {
    punycode::encode(input)
}

#[tauri::command]
pub fn decode_punycode(input: &str) -> Result<String, String> {
    punycode::decode(input)
}

#[tauri::command]
pub fn encode_quoted_printable(input: &str) -> String {
    quoted_printable::encode(input)
}

#[tauri::command]
pub fn decode_quoted_printable(input: &str) -> Result<String, String> {
    quoted_printable::decode(input)
}
