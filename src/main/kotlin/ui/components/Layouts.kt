package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ui.util.Dimen

@Composable
fun ScreenLayout(
    pageTitle: String,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        TopAppBar(modifier = modifier.padding(bottom = Dimen.paddingDefault)) {
            IconButton(onClick = onBackPressed, modifier = Modifier.padding(start = Dimen.paddingMicro)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }

            Spacer(modifier = Modifier.width(Dimen.paddingLarge))

            Text(text = pageTitle, style = MaterialTheme.typography.body1)
        }

        content()
    }
}