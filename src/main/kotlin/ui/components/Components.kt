package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import model.Tool
import ui.theme.AppTypography
import ui.theme.R
import ui.util.Dimen

@Composable
fun SearchView(
    value: String,
    onValueChange: (String) -> Unit,
    placeHolderText: String,
    trailingIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxWidth()) {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            placeholder = {
                Text(text = placeHolderText, style = AppTypography.subtitle2)
            },
            trailingIcon = {
                Icon(trailingIcon, null)
            }
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ToolCard(
    tool: Tool,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClicked,
        modifier = modifier.padding(Dimen.paddingDefault).size(width = 350.dp, height = 200.dp),
        shape = RoundedCornerShape(size = Dimen.paddingMicro),
        backgroundColor = R.color.DarkBackground,
        elevation = Dimen.paddingMicro
    ) {
        Column(modifier = modifier.padding(Dimen.paddingDefault)) {
            Spacer(modifier = Modifier.height(Dimen.paddingDefault))

            Text(text = tool.name, style = AppTypography.h6, textAlign = TextAlign.Start)

            Spacer(modifier = Modifier.height(Dimen.paddingSmall))

            Text(
                text = tool.sub,
                style = AppTypography.body2,
                textAlign = TextAlign.Start,
                color = R.color.MediumGrey,
            )
        }
    }
}

@Composable
fun ToolIconView(
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(50.dp).background(R.color.DarkShade), contentAlignment = Alignment.Center) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = modifier.size(48.dp),
            tint = Color.White
        )
    }
}

@Composable
fun AppDialog(
    title: String,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = title, style = AppTypography.h5)
        },
        text = {
            Column {
                content()
            }
        },
        buttons = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(
                    start = Dimen.paddingDefault,
                    end = Dimen.paddingDefault,
                    bottom = Dimen.paddingDefault
                ),
                horizontalArrangement = Arrangement.End
            ) {
                PrimaryButton(
                    onClick = onDismiss
                ) {
                    Text(R.string.close)
                }
            }
        },
        onDismissRequest = onDismiss
    )
}

@Composable
fun AppSnackbar(
    message: String,
    onShowSnackbar: (Boolean) -> Unit,
    duration: SnackbarDuration = SnackbarDuration.Short,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarHostState) {
        snackbarHostState.showSnackbar(message, duration = duration)
        onShowSnackbar(false)
    }

    Box(
        modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        SnackbarHost(
            modifier = modifier.width(200.dp),
            hostState = snackbarHostState
        )
    }
}