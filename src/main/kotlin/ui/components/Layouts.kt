package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import model.Tool
import platform.extensions.copyToClipboard
import ui.theme.AppTypography
import ui.theme.R
import ui.util.Dimen

@OptIn(ExperimentalRichTextApi::class)
@Composable
fun ScreenLayout(
    tool: Tool,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarMessage: String = "",
    showSnackbar: Boolean = remember { false },
    onShowSnackbar: (Boolean) -> Unit = {},
    bottomNavContent: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val richTextState = rememberRichTextState()
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        AppDialog(
            title = tool.name,
            onDismiss = {
                showDialog.value = false
            },
        ) {
            richTextState.setConfig(
                linkColor = R.color.TelegramBlue,
                linkTextDecoration = TextDecoration.None,
                codeBackgroundColor = Color(0xFF272822),
                codeStrokeColor = Color(0x00000066),
                codeColor = Color(0xFFf8f8f2)
            )
            richTextState.setHtml(tool.description)

            RichText(
                state = richTextState,
                color = Color.White,
                style = AppTypography.body2
            )
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
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                elevation = 10.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    content = bottomNavContent
                )
            }
        }
    ) {
        Box {
            Column(
                modifier = modifier.padding(Dimen.paddingDefault)
            ) {
                content(it)
            }

            if (showSnackbar) {
                AppSnackbar(
                    modifier = Modifier.align(Alignment.TopCenter),
                    message = snackbarMessage,
                    onShowSnackbar = {
                        onShowSnackbar(it)
                    }
                )
            }
        }

    }
}