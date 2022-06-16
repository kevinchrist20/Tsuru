package ui.screens.slug

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ui.screens.base.ToolComponent

@Composable
fun SlugScreen(component: ToolComponent) {
    Text("Hello, ${component.tool.name}", color = Color.White)
}