mod commands;
mod services;

use commands::{encoders, generators, security};
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
            encoders::encode_url,
            encoders::decode_url,
            encoders::encode_html,
            encoders::decode_html,
            encoders::encode_hex,
            encoders::decode_hex,
            encoders::encode_binary,
            encoders::decode_binary,
            encoders::encode_base32,
            encoders::decode_base32,
            encoders::encode_unicode,
            encoders::decode_unicode,
            encoders::encode_punycode,
            encoders::decode_punycode,
            encoders::encode_quoted_printable,
            encoders::decode_quoted_printable,
            generators::generate_uuid,
            generators::generate_uuids,
            generators::generate_slug,
            security::decode_jwt
        ])
        .run(tauri::generate_context!())
        .expect("error while running tauri application");
}
