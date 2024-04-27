package ui.screens.base

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import java.nio.charset.Charset

class Base64ScreenModel: ScreenModel {
    private val _encoded = mutableStateOf("")
    val encoded: State<String> = _encoded

    fun encode(data: String, encoding: Charset = Charsets.UTF_8) {
        _encoded.value = java.util.Base64.getEncoder().encodeToString(data.toByteArray(encoding))
    }

    fun decode(encodedData: String, encoding: Charset = Charsets.UTF_8): String {
        return java.util.Base64.getDecoder().decode(encodedData).toString()
    }
}