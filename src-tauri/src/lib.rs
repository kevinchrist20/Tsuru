mod commands;
mod services;

use commands::{encoders, generators};
use tauri::Manager;

#[cfg_attr(mobile, tauri::mobile_entry_point)]
pub fn run() {
    tauri::Builder::default()
        .plugin(tauri_plugin_shell::init())
        .plugin(tauri_plugin_opener::init())
        .setup(|app| {
            let window = app.get_webview_window("main").unwrap();
            #[cfg(target_os = "macos")]
            window
                .eval(
                    r#"
                window.addEventListener('keydown', (e) => {
                    const isContentEditable = document.activeElement.isContentEditable;
                    if (e.key === 'Backspace' &&
                        !['INPUT', 'TEXTAREA'].includes(document.activeElement.tagName) &&
                        !isContentEditable) {
                        e.preventDefault();
                    }
                });
            "#,
                )
                .unwrap();

            Ok(())
        })
        .invoke_handler(tauri::generate_handler![
            encoders::encode_base64,
            encoders::decode_base64,
            generators::generate_uuid,
            generators::generate_uuids,
            generators::generate_slug
        ])
        .run(tauri::generate_context!())
        .expect("error while running tauri application");
}
