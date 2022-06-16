package ui.screens.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import model.Tool
import model.ToolRoute

class DashboardComponent(
    val onItemClicked: (Tool) -> Unit
) {
    private val _tools = mutableStateOf(listOf<Tool>())
    val tools: State<List<Tool>> = _tools

    init {
        _tools.value = getTools()
    }

    private fun getTools() = mutableListOf(
        Tool(
            name = "Base64 Text",
            description = "It is a binary-to-text encoding representing binary data in an ASCII string format. It encodes text to Base64 or decodes Base64 to text.",
            route = ToolRoute.Base64
        ),
        Tool(
            name = "Slugs",
            description = "It slugify words to an SEO-friendly and human-readable URL slug for SEO optimization. A URL slug is the part located at the very end of a URL that refers to a page or post on a website.",
            route = ToolRoute.Slugs
        ),
        Tool(
            name = "UUID Generator",
            description = "UUID is an abbreviation standing for Universally Unique Identifier. A UUID is a 128-bit value used to identify a computer resource to ensure its uniqueness.",
            route = ToolRoute.UUID
        ),
    )
}