package ui.screens.slug

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import model.Tool
import ui.components.ScreenLayout
import ui.theme.R

class SlugScreen(private val tool: Tool) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ScreenLayout(
            tool = tool,
            onBackPressed = {
                navigator.pop()
            }
        ) {
            Text("Welcome")
        }
    }
}