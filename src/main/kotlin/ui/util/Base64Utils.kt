package ui.util

import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import java.util.*

object Base64Utils {
    private const val PNG_BASE64_HEADER = "data:image/png;base64,"

    fun encode(data: String, encoding: Charset = Charsets.UTF_8): String {
        return try {
            Base64.getEncoder().encodeToString(data.toByteArray(encoding))
        } catch (e: Exception) {
            throw Exception("Could not encode string to base64", e)
        }
    }

    fun encodeImage(data: ByteArray): String {
        return try {
            PNG_BASE64_HEADER + Base64.getEncoder().encodeToString(data)
        } catch (e: Exception) {
            throw Exception("Could not encode string to base64", e)
        }
    }

//    fun encodeImage(data: ByteArray): ByteArray {
//        val outputStream = ByteArrayOutputStream()
//        return try {
//            outputStream.write(PNG_BASE64_HEADER.toByteArray())
//            Base64.getEncoder().encodeToString(data).toByteArray()
//        } catch (e: Exception) {
//            throw Exception("Could not encode string to base64", e)
//        }
//    }

    fun decode(data: String): String {
        return try {
            String(Base64.getDecoder().decode(data))
        } catch (e: Exception) {
            throw Exception("Could not decode string from base 64 string.", e)
        }
    }

    fun decodeImage(imgBase64: String?): ByteArray? {
        return try {
            imgBase64?.let {
                if (it.startsWith(PNG_BASE64_HEADER)) {
                    Base64.getDecoder().decode(it.replaceFirst(PNG_BASE64_HEADER, ""))
                } else {
                    throw Exception("Unknown image format: '${imgBase64.take(20)}'")
                }
            }
        } catch (e: Exception) {
            throw Exception("Could not resolve image from base 64 string.", e)
        }
    }
}