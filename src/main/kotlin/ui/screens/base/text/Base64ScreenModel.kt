package ui.screens.base.text

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import ui.util.Base64Utils

class Base64ScreenModel : ScreenModel {
    private val _encoded = mutableStateOf("")
    val encoded: State<String> = _encoded

    private val _decodingErr = mutableStateOf<String?>(null)
    val decodingErr: State<String?> = _decodingErr

    private val _decoded = mutableStateOf("")
    val decoded: State<String> = _decoded

    fun encode(data: String) {
        _encoded.value = Base64Utils.encode(data)
    }

    fun decode(encodedData: String) {
        try {
            _decodingErr.value = null
            _decoded.value = Base64Utils.decode(encodedData)
        } catch (e: Exception) {
            _decodingErr.value = e.message
        }
    }

    fun clearEncodedValue() {
        _encoded.value = ""
    }

    fun clearDecodedValue() {
        _decoded.value = ""
    }
}