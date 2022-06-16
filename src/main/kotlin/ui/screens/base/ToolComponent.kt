package ui.screens.base

import model.Tool


class ToolComponent(
    val route: String,
    val tool: Tool,
    val onBackPress: () -> Unit
){}