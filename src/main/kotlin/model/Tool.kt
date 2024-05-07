package model

import androidx.compose.ui.graphics.vector.ImageVector
import java.io.Serializable

data class Tool(
    val name: String,
    val sub: String,
    val description: String,
    val route: ToolRoute,
    val icon: ImageVector? = null
): Serializable

enum class ToolRoute(val value: String) {
    Base64("base64"),
    Base64Image("base64Image"),
    Slugs("slug"),
    UUID("uuid")
}