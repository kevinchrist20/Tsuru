package ui.screens.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import model.Tool
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
            sub = "Base64 Text",
            description = "Base64 is a binary-to-text encoding representing binary data in an ASCII string format. Base64 encoding uses different 64 ASCII characters including uppercase letters A-Z, lowercase letters a-z, digits 0-9, and the special symbols + and / to represent binary data in a text format.",
            route = ToolRoute.Base64
        ),
        Tool(
            name = "Base64 Image Encoder/Decoder",
            sub = "Base64 Image",
            description = "Its a binary-to-text encoding representing binary data in an ASCII string format. A Base64 encoded image is the result of converting a binary image to a Base64 string. Base64 encoded images are commonly used in web development as inlined images embedded in HTML, CSS, and JavaScript files.",
            route = ToolRoute.Base64Image
        ),
        Tool(
            name = "Slugs",
            sub = "Slugs Generator",
            description = "It slugify words to an SEO-friendly and human-readable URL slug for SEO optimization. A URL slug is the part located at the very end of a URL that refers to a page or post on a website.",
            route = ToolRoute.Slugs
        ),
        Tool(
            name = "UUID",
            sub = "UUID Generator",
            description = "UUID is an abbreviation standing for Universally Unique Identifier. A UUID is a 128-bit value used to identify a computer resource to ensure its uniqueness.",
            route = ToolRoute.UUID
        ),
    )
}