package model

import androidx.compose.ui.graphics.vector.ImageVector
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
data class Tool(
    val name: String,
    val sub: String,
    val description: String,
    val route: ToolRoute,
    val icon: ImageVector? = null
)

enum class ToolRoute(val value: String) {
    Base64("base64"),
    Slugs("slug"),
    UUID("uuid")
}