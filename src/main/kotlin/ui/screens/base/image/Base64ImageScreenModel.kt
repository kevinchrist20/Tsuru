package ui.screens.base.image

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import kotlinx.coroutines.launch
import java.util.*

class Base64ImageScreenModel : ScreenModel {
    private val _encodedImageString = mutableStateOf("")
    val encodedImageString: State<String> = _encodedImageString

    fun encodeImage(imageFile: MPFile<Any>) {
        screenModelScope.launch {
            val encoded = imageFile.getFileByteArray()
            _encodedImageString.value = "data:image/png;base64," + Base64.getEncoder().encodeToString(encoded)
        }
    }
}