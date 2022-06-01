package ui.screens.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import model.Tool

class DashboardComponent(
    val onItemClicked: (String) -> Unit
) {
    private val _tools = mutableStateOf(listOf<Tool>())
    val tools: State<List<Tool>> = _tools

    init {
        _tools.value = getTools()
    }
    private fun getTools() = mutableListOf(
        Tool(
            "Base64",
            "A lightbulb icon indicates the tool(s) that can be used with the clipboard content. If only one tool is detected, the clipboard content will be automatically pasted in that tool",
            Icons.Default.AccountCircle
        ),
        Tool("Hash", "Smart detection behavior can be customized in the app settings", Icons.Default.Settings),
        Tool(
            "HTML",
            "DevToys works entirely offline! No need to use many untruthful websites to do simple tasks with your data. ",
            Icons.Default.Refresh
        ),
        Tool(
            "Jetpack Compose",
            "A lightbulb icon indicates the tool(s) that can be used with the clipboard content. If only one tool is detected, the clipboard content will be automatically pasted in that tool",
            Icons.Default.Build
        ),
        Tool(
            "Json",
            "If only one tool is detected, the clipboard content will be automatically pasted in that tool",
            Icons.Default.Call
        ),
        Tool("JWT", "An offline Windows app that helps developers in daily tasks", Icons.Default.Notifications),
        Tool("Yaml", " DevToys is designed to embrace Windows ecosystem. ", Icons.Default.Share),
        Tool(
            "UUID Generator",
            "A lightbulb icon indicates the tool(s) that can be used with the clipboard content.",
            Icons.Default.DateRange
        ),
        Tool("Date Parser", "An offline Windows app that helps developers in daily tasks", Icons.Default.Lock),
        Tool(
            "Regex",
            "DevToys works entirely offline! No need to use many untruthful websites to do simple tasks with your data. ",
            Icons.Default.MailOutline
        ),
        Tool("New", "Smart detection behavior can be customized in the app settings", Icons.Default.Favorite),
    )
}