package ui.util

import java.nio.charset.Charset
import java.util.*

object Base64Utils {
    private const val PNG_BASE64_HEADER = "data:image/png;base64,"

    fun encode(data: String, encoding: Charset = Charsets.UTF_8): String {
        try {
            return Base64.getEncoder().encodeToString(data.toByteArray(encoding))
        } catch (e: Exception) {
            throw Exception("Could not encode string to base64", e)
        }
    }

    fun encodeImage(data: ByteArray): String {
        try {
            return PNG_BASE64_HEADER + Base64.getEncoder().encodeToString(data)
        } catch (e: Exception) {
            throw Exception("Could not encode string to base64", e)
        }
    }

    fun decode(data: String): String {
        try {
            return String(Base64.getDecoder().decode(data))
        } catch (e: Exception) {
            throw Exception("Could not decode string from base 64 string.", e)
        }
    }

    fun decodeImage(imgBase64: String?): ByteArray? {
        try {
            return imgBase64?.let {
                if (it.startsWith(PNG_BASE64_HEADER)) {
                    Base64.getDecoder().decode(it.replaceFirst(PNG_BASE64_HEADER, ""))
                } else {
                    println("Unknown image format: '${imgBase64.take(20)}'")
                    null
                }
            }
        } catch (e: Exception) {
            throw Exception("Could not resolve image from base 64 string.", e)
        }
    }
}