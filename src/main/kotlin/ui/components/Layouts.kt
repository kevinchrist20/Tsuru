package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import ui.util.Dimen

@Composable
fun ScreenLayout(
    pageTitle: String,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    showIsCopiedSnackbar: Boolean = remember { false },
    content: @Composable (PaddingValues) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(topBar = {
        TopAppBar(modifier = modifier.padding(bottom = Dimen.paddingDefault)) {
            IconButton(onClick = onBackPressed, modifier = Modifier.padding(start = Dimen.paddingMicro)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }

            Spacer(modifier = Modifier.width(Dimen.paddingLarge))

            Text(text = pageTitle, style = MaterialTheme.typography.body1)
        }
    }) {
        Column {
            content(it)
        }

        snackBarTest(snackbarHostState)
    }

    LaunchedEffect(showIsCopiedSnackbar) {
        if (showIsCopiedSnackbar) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("")
            }
        }
    }
}

@Composable
fun snackBarTest(snackbarHostState: SnackbarHostState) {
    SnackbarHost(hostState = snackbarHostState, snackbar = {
        Snackbar(
            backgroundColor = Color.Red,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Snackbar Test")
        }
    })
}