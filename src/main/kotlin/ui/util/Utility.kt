package ui.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import java.util.*

fun resolveImage(imgBase64: String?): ByteArray? {
    try {
        return imgBase64?.let {
            if (it.startsWith(PNG_BASE64_HEADER)) {
                Base64.getDecoder().decode(it.replaceFirst(PNG_BASE64_HEADER,""))
            } else {
                println("Unknown image format: '${imgBase64.take(20)}'")
                null
            }
        }
    } catch (e: Exception) {
        throw Exception("Could not resolve image from base 64 string.", e)
    }
}

fun imageBitmapFromBytes(encodedImageData: ByteArray): ImageBitmap {
    return Image.makeFromEncoded(encodedImageData).toComposeImageBitmap()
}