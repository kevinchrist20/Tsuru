package ui.screens.base

import androidx.compose.runtime.mutableStateOf
import java.nio.charset.Charset

class Base64 {
    val encoded = mutableStateOf("")

    fun encode(data: String, encoding: Charset = Charsets.UTF_8) {
        encoded.value = java.util.Base64.getEncoder().encodeToString(data.toByteArray(encoding))
    }

    fun decode(encodedData: String, encoding: Charset = Charsets.UTF_8): String {
        return java.util.Base64.getDecoder().decode(encodedData).toString()
    }
}