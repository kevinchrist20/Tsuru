#![cfg_attr(
    all(not(debug_assertions), target_os = "windows"),
    windows_subsystem = "windows"
)]

mod commands;

use commands::encoders;
use commands::generators;

fn main() {
    tauri::Builder::default()
        .invoke_handler(tauri::generate_handler![
            encoders::encode_base64,
            encoders::decode_base64,
            generators::generate_uuid,
            generators::generate_slug
        ])
        .run(tauri::generate_context!())
        .expect("error while running tauri application");
}
