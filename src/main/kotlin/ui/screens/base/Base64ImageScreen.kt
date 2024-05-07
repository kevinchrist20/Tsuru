package ui.screens.base

import androidx.compose.material.Text
import androidx.compose.runtime.*
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import model.Tool
import ui.components.PrimaryButton
import ui.components.ScreenLayout

class Base64ImageScreen(private val tool: Tool) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        var showFilePicker by remember { mutableStateOf(false) }

        val fileType = listOf("jpg", "png")
        FilePicker(show = showFilePicker, fileExtensions = fileType) { platformFile ->
            showFilePicker = false
            // do something with the file
        }

        ScreenLayout(
            tool = tool,
            onBackPressed = {
                navigator.pop()
            }
        ) {
            PrimaryButton(
                onClick = {
                    showFilePicker = true
                }
            ) {
                Text("Pick Image")
            }
        }
    }
}