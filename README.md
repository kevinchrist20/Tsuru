# Tsuru

Tsuru is a desktop app that provides a collection of developer tools designed to assist your workflow. From simple utilities 
like string manipulation, JSON formatters, validation tools, cryptography and security, encoders and decoders, converters 
and more. Tsuru covers various categories in real-world programming.

## Features

- **Comprehensive Toolbox**: Tsuru offers a wide range of tools to cater to different programming needs.
- **Easy to Use**: Each tool is designed with simplicity in mind, ensuring ease of use for developers of all levels.
- **Desktop Application**: Tsuru is a desktop app, providing offline access and convenience for developers.

## Tools

1. **String Utilities**: Easily manipulate strings with various functions such as encoding, decoding, and formatting.
2. **JSON Formatter**: Format and beautify JSON data for better readability.
3. **Validation Tools**: Validate data inputs against predefined rules and patterns.
4. **Cryptography and Security**: Encrypt and decrypt data, generate hashes, and ensure data security.
5. **Encoders and Decoders**: Convert data between different encoding formats such as Base64, URL encoding, etc.
6. **Converters**: Convert data between different formats, such as JSON to XML, CSV to JSON, etc.
7. *(Add more tools here)*

## Getting Started

To get started with Tsuru, download the latest version from our [website](https://example.com) and install it on your desktop. 
Then, simply launch the app and explore the available tools.

## Contributing

We welcome contributions from the community to improve and expand Tsuru. If you'd like to contribute, 
please see our [Contribution Guidelines](CONTRIBUTING.md) for more information.

## License

Tsuru is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.


## Recommended IDE Setup

- [VS Code](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) + [Tauri](https://marketplace.visualstudio.com/items?itemName=tauri-apps.tauri-vscode) + [rust-analyzer](https://marketplace.visualstudio.com/items?itemName=rust-lang.rust-analyzer)

## Type Support For `.vue` Imports in TS

Since TypeScript cannot handle type information for `.vue` imports, they are shimmed to be a generic Vue component type by default. In most cases this is fine if you don't really care about component prop types outside of templates. However, if you wish to get actual prop types in `.vue` imports (for example to get props validation when using manual `h(...)` calls), you can enable Volar's Take Over mode by following these steps:

1. Run `Extensions: Show Built-in Extensions` from VS Code's command palette, look for `TypeScript and JavaScript Language Features`, then right click and select `Disable (Workspace)`. By default, Take Over mode will enable itself if the default TypeScript extension is disabled.
2. Reload the VS Code window by running `Developer: Reload Window` from the command palette.

You can learn more about Take Over mode [here](https://github.com/johnsoncodehk/volar/discussions/471).
