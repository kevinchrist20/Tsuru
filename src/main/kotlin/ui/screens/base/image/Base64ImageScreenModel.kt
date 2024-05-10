package ui.screens.base.image

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import kotlinx.coroutines.launch
import ui.util.Base64Utils
import ui.util.imageBitmapFromBytes
import java.io.File

class Base64ImageScreenModel : ScreenModel {

    private val _encodedImageString = mutableStateOf("")
    val encodedImageString: State<String> = _encodedImageString

    private val _decodeImageBitmap = mutableStateOf<ImageBitmap?>(null)
    val decodeImageBitmap: State<ImageBitmap?> = _decodeImageBitmap

    private val _decodingErr = mutableStateOf<String?>(null)
    val decodingErr: State<String?> = _decodingErr

    private val _encodingErr = mutableStateOf<String?>(null)
    val encodingErr: State<String?> = _encodingErr

    @Suppress("UNCHECKED_CAST")
    fun encodeImage(mpFile: MPFile<Any>) {
        _encodingErr.value = null
        val imageFile = mpFile as? MPFile<File>
        if (imageFile != null) {
            screenModelScope.launch {
                val encoded = imageFile.getFileByteArray()
                try {
                    _encodedImageString.value = Base64Utils.encodeImage(encoded)
                } catch (e: Exception) {
                    _encodingErr.value = e.message
                }
            }
        }
    }

    fun decode(string: String) {
        _encodingErr.value = null
        try {
            val image = Base64Utils.decodeImage(string)
            if (image != null) {
                _decodeImageBitmap.value = imageBitmapFromBytes(image)
            } else {
                _decodingErr.value = "Unable to decode image"
            }
        } catch (e: Exception) {
            _decodingErr.value = e.message
        }
    }

    fun clear() {
        _encodedImageString.value = ""
        _decodeImageBitmap.value = null

        _decodingErr.value = null
        _encodingErr.value = null
    }
}