package ui

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
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import model.Tool
import ui.theme.AppTypography
import ui.theme.R
import ui.util.Dimen
import java.util.concurrent.TimeUnit

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
fun toolCard(
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
                text = tool.description,
                style = AppTypography.body2,
                textAlign = TextAlign.Start,
                color = R.color.MediumGrey,
            )
        }
    }
}

@Composable
fun toolIconView(
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
fun toast(onDismiss: (Boolean) -> Unit) {
    // Create a state variable to track whether the alert dialog is open
    var alertDialogOpen by remember { mutableStateOf(true) }

    // Use the Dialog and AlertDialog composable to show a timed alert dialog
    Dialog(
        onCloseRequest = {
            alertDialogOpen = false
        },
        undecorated = true,
    ) {
        Column(modifier = Modifier.fillMaxSize().size(100.dp, 100.dp).background(color = Color.Black)) {
            Text(text = "This is a timed alert dialog that will close after 5 seconds", color = Color.White)
        }
    }

    // Use the delay function to close the alert dialog after 5 seconds
    LaunchedEffect(Unit) {
        delay(TimeUnit.SECONDS.toMillis(2))
        alertDialogOpen = false
    }

    LaunchedEffect(alertDialogOpen) {
        onDismiss(alertDialogOpen)
    }
}