package ui.screens.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import model.Tool
import model.ToolCategory
import model.ToolRoute

class DashboardScreenModel: ScreenModel {
    private val _tools = mutableStateOf(listOf<Tool>())
    val tools: State<List<Tool>> = _tools

    init {
        _tools.value = getTools()
    }

    private fun getTools() = listOf(
        Tool(
            name = "Base64 Encoder/Decoder",
            sub = "Base64 Encoder/Decoder is a utility for converting text to Base64 or decoding Base64 to text. It comprises two modes: Base64 Encoder and Base64 Decoder.",
            description = """
                Base64 is a binary-to-text encoding representing binary data in an ASCII string format. Base64 encoding uses different 64 ASCII characters including uppercase letters A-Z, lowercase letters a-z, 
                digits 0-9, and the special symbols + and / to represent binary data in a text format.
                
                <b>Base64 Encoder</b> - Converts plain text or binary data into a Base64 encoded format, increasing readability and ensuring compatibility with systems that only support ASCII characters.
                Enter text in the input field, and the output Base64 will be displayed accordingly.
                
                Base64 Decoder - Reverses the process, converting Base64 encoded data back into its original form, facilitating data transfer and interpretation across different platforms.
                Paste Base64 in the input field, and the output string will be displayed accordingly.
            """.trimIndent() ,
            route = ToolRoute.Base64,
            category = ToolCategory.EncodeDecode
        ),
        Tool(
            name = "Base64 Image Encoder/Decoder",
            sub = "Base64 Image",
            description = "Its a binary-to-text encoding representing binary data in an ASCII string format. A Base64 encoded image is the result of converting a binary image to a Base64 string. Base64 encoded images are commonly used in web development as inlined images embedded in HTML, CSS, and JavaScript files.",
            route = ToolRoute.Base64Image,
            category = ToolCategory.EncodeDecode
        ),
        Tool(
            name = "Slugs",
            sub = "Slugs Generator",
            description = "It slugify words to an SEO-friendly and human-readable URL slug for SEO optimization. A URL slug is the part located at the very end of a URL that refers to a page or post on a website.",
            route = ToolRoute.Slugs,
            category = ToolCategory.EncodeDecode
        ),
        Tool(
            name = "UUID",
            sub = "UUID Generator",
            description = "UUID is an abbreviation standing for Universally Unique Identifier. A UUID is a 128-bit value used to identify a computer resource to ensure its uniqueness.",
            route = ToolRoute.UUID,
            category = ToolCategory.EncodeDecode
        ),
    )
}