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

class Base64ImageScreenModel : ScreenModel {

    private val _encodedImageString = mutableStateOf("")
    val encodedImageString: State<String> = _encodedImageString

    private val _decodeImageBitmap = mutableStateOf<ImageBitmap?>(null)
    val decodeImageBitmap: State<ImageBitmap?> = _decodeImageBitmap

    fun encodeImage(imageFile: MPFile<Any>) {
        screenModelScope.launch {
            val encoded = imageFile.getFileByteArray()
            _encodedImageString.value = Base64Utils.encodeImage(encoded)
        }
    }

    fun decode(string: String) {
        val image = Base64Utils.decodeImage(string)
        _decodeImageBitmap.value = image?.let { imageBitmapFromBytes(it) }
    }
}