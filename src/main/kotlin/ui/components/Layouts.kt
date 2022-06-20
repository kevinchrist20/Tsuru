package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ui.screens.base.ToolComponent
import ui.util.Dimen

@Composable
fun ScreenLayout(
    toolComponent: ToolComponent,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        TopAppBar(modifier = Modifier.padding(bottom = Dimen.paddingDefault)) {
            IconButton(onClick = toolComponent.onBackPress, modifier = Modifier.padding(start = Dimen.paddingMicro)) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }

            Spacer(modifier = Modifier.width(Dimen.paddingLarge))

            Text(text = toolComponent.tool.sub, style = MaterialTheme.typography.body1)
        }

        content()
    }
}