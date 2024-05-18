package ui.screens.base.image

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ui.util.Base64Utils
import ui.util.imageBitmapFromBytes
import java.io.File

class Base64ImageScreenModel : ScreenModel {

    private val _encodedImageString = MutableStateFlow("")
    val encodedImageString: StateFlow<String> = _encodedImageString

    private val _decodeImageBitmap = mutableStateOf<ImageBitmap?>(null)
    val decodeImageBitmap: State<ImageBitmap?> = _decodeImageBitmap

    private val _decodingErr = mutableStateOf<String?>(null)
    val decodingErr: State<String?> = _decodingErr

    private val _encodingErr = mutableStateOf<String?>(null)
    val encodingErr: State<String?> = _encodingErr

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading


    @Suppress("UNCHECKED_CAST")
    fun encodeImage(mpFile: MPFile<Any>) {
        _encodingErr.value = null
        _loading.value = true

        val imageFile = mpFile as? MPFile<File>
        if (imageFile != null) {
            screenModelScope.launch(Dispatchers.IO) {
                var encodedString = ""

                try {
                    val encoded = imageFile.getFileByteArray()

                    encodedString = Base64Utils.encodeImage(encoded)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _encodingErr.value = e.message
                    }
                }

                withContext(Dispatchers.Main) {
                    encodedString.reader().apply {
                        val chunked = readText().chunked(10000)
                        for (u in chunked) {
                            _encodedImageString.tryEmit(u)
                            delay(500)
                        }

                        close()
                    }

                    _loading.value = false
                }
            }
        }
    }

    fun decode(string: String) {
//        _loading.value = true
        _encodingErr.value = null

        try {
            val image = Base64Utils.decodeImage(string)
//            _loading.value = false

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
//        _encodedImageString.value = ""
        _decodeImageBitmap.value = null

        _decodingErr.value = null
        _encodingErr.value = null
    }
}