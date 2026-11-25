use crate::services::generators;

#[tauri::command]
pub fn generate_uuid() -> String {
    generators::generate_uuid()
}

#[tauri::command]
pub fn generate_uuids(count: u32) -> Vec<String> {
    generators::generate_uuids(count)
}

#[tauri::command]
pub fn generate_slug(input: &str) -> String {
    generators::generate_slug(input)
}
