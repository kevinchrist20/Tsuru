package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import model.Tool
import ui.theme.TsuruTheme
import ui.util.Dimen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenLayout(
    tool: Tool,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    showIsCopiedSnackbar: Boolean = remember { false },
    content: @Composable (PaddingValues) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = MaterialTheme.shapes.medium.copy(
            topEnd = CornerSize(Dimen.paddingMedium),
            topStart = CornerSize(Dimen.paddingMedium)
        ),
        sheetContent = {
            Column(
                modifier = Modifier.padding(Dimen.paddingDefault)
            ) {
                Text(tool.description)
            }
        }
    ) {
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
                        IconButton(onClick = onBackPressed, modifier = Modifier.padding(start = Dimen.paddingMicro)) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Localized description")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch { sheetState.show() }
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

                snackBarTest(snackbarHostState)
            }
        }

        LaunchedEffect(showIsCopiedSnackbar) {
            if (showIsCopiedSnackbar) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("")
                }
            }
        }
    }
}

@Composable
fun snackBarTest(snackbarHostState: SnackbarHostState) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = {
            Snackbar(
//            backgroundColor = Color.Red,
                modifier = Modifier
            ) {
                Text("Snackbar Test")
            }
        }
    )
}