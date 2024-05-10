package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import model.Tool
import ui.util.Dimen

@Composable
fun ScreenLayout(
    tool: Tool,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarMessage: String = "",
    showSnackbar: Boolean = remember { false },
    onShowSnackbar: (Boolean) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        AppDialog(
            title = tool.name,
            content = tool.description
        ) {
            showDialog.value = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = modifier.padding(bottom = Dimen.paddingDefault),
                title = {
                    Text(
                        text = tool.name,
                        style = MaterialTheme.typography.body1,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed,
                        modifier = Modifier.padding(start = Dimen.paddingMicro)
                    ) {
                        Icon(
                            Icons.Default.ArrowBackIosNew,
                            contentDescription = "Localized description",
                            modifier = modifier.size(16.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            showDialog.value = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        },
    ) {
        Column(
            modifier = modifier.padding(Dimen.paddingDefault)
        ) {
            content(it)
        }

        if (showSnackbar) {
            AppSnackbar(
                message = snackbarMessage,
                onShowSnackbar = {
                    onShowSnackbar(it)
                }
            )
        }
    }
}