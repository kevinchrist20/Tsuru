package ui.screens.base

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ui.components.ScreenLayout
import ui.components.Toast
import ui.theme.R
import ui.util.Dimen
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection

class Base64Screen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val screenModel = rememberScreenModel { Base64ScreenModel() }
        var data by remember { mutableStateOf("") }

        val isCopyPressed = remember { mutableStateOf(false) }

//        if (isCopyPressed.value) {
//            Toast {
//                isCopyPressed.value = false
//            }
//        }

        ScreenLayout(
            pageTitle = R.string.base64Screen,
            onBackPressed = {
                navigator.pop()
            },
            showIsCopiedSnackbar = isCopyPressed.value
        ) {
            TextField(
                value = data,
                onValueChange = {
                    data = it
                    screenModel.encode(data)
                },
                modifier = Modifier.fillMaxWidth().height(height = 200.dp).padding(Dimen.paddingDefault),
            )

            TextField(
                value = screenModel.encoded.value,
                readOnly = true,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth().height(height = 200.dp).padding(Dimen.paddingDefault),
            )

            Button(onClick = {
                copyToClipboard(screenModel.encoded.value)
                isCopyPressed.value = true
            }) {
                Text("Copy")
            }
        }
    }

    private fun copyToClipboard(message: String) {
        val stringSelection = StringSelection(message)
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(stringSelection, null)
    }
}

