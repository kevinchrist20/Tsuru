// Learn more about Tauri commands at https://tauri.app/v1/guides/features/command

use tauri::Manager;

#[cfg_attr(mobile, tauri::mobile_entry_point)]
pub fn run() {
    tauri::Builder::default()
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
        .plugin(tauri_plugin_shell::init())
        .invoke_handler(tauri::generate_handler![])
        .run(tauri::generate_context!())
        .expect("error while running tauri application");
}
