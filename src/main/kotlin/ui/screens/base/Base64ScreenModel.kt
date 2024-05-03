package ui.screens.base

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import java.nio.charset.Charset
import java.util.*

class Base64ScreenModel: ScreenModel {
    private val _encoded = mutableStateOf("")
    val encoded: State<String> = _encoded

    private val _decoded = mutableStateOf("")
    val decoded: State<String> = _decoded

    fun encode(data: String, encoding: Charset = Charsets.UTF_8) {
        _encoded.value = Base64.getEncoder().encodeToString(data.toByteArray(encoding))
    }

    fun decode(encodedData: String, encoding: Charset = Charsets.UTF_8) {
        _decoded.value = String(Base64.getDecoder().decode(encodedData))
    }

    fun clearEncodedValue() {
        _encoded.value = ""
    }

    fun clearDecodedValue() {
        _decoded.value = ""
    }
}