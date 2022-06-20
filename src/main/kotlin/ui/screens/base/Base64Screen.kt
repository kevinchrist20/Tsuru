package ui.screens.base

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.components.ScreenLayout
import ui.util.Dimen
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection


@Composable
fun Base64Screen(component: ToolComponent) {
    val base64 = remember { Base64() }
    var data by remember { mutableStateOf("") }

    ScreenLayout(toolComponent = component) {
        TextField(
            value = data,
            onValueChange = {
                data = it
                base64.encode(data)
            },
            modifier = Modifier.fillMaxWidth().height(height = 200.dp).padding(Dimen.paddingDefault),
        )

        TextField(
            value = base64.encoded.value,
            readOnly = true,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth().height(height = 200.dp).padding(Dimen.paddingDefault),
        )

        Button(onClick = {
            copyToClipboard(base64.encoded.value)
        }) {
            Text("Copy")
        }
    }
}

fun copyToClipboard(message: String) {
    val stringSelection = StringSelection(message)
    val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(stringSelection, null)
}