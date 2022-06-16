package ui.screens.base

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ui.util.Dimen

@Composable
fun Base64Screen(base64ScreenComponent: ToolComponent) {
    Column {
        TopAppBar {
            IconButton(onClick = base64ScreenComponent.onBackPress, modifier = Modifier.padding(start = Dimen.paddingMicro)) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }

            Spacer(Modifier.width(Dimen.paddingDefault))

            Text("Welcome ${base64ScreenComponent.name}")
        }
    }
}