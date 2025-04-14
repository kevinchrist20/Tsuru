use uuid::Uuid;

#[tauri::command]
pub fn generate_uuid() -> String {
    Uuid::new_v4().to_string()
}

#[tauri::command]
pub fn generate_slug(input: &str) -> String {
    input
        .to_lowercase()
        .chars()
        .map(|c| match c {
            'a'..='z' | '0'..='9' => c.to_string(),
            _ if c.is_whitespace() => "-".to_string(),
            _ => "".to_string(),
        })
        .collect::<Vec<String>>()
        .join("")
        .replace("--", "-")
}