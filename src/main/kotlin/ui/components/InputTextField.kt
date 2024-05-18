package ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ui.theme.AppTypography
import ui.util.Dimen

@Composable
fun InputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = AppTypography.body1,
    isError: Boolean = false,
    placeholder: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    readOnly: Boolean = false,
    enabled: Boolean = true,
//    backgroundColor: Color = TsuruTheme.colors.inputBackground,
    label: (@Composable () -> Unit)? = null,
    mutableInteractionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        textStyle = textStyle,
        isError = isError,
        label = label,
        maxLines = maxLines,
        interactionSource = mutableInteractionSource,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        readOnly = readOnly,
        enabled = enabled,
    )
}

@Composable
fun InputTextField(
    value: String,
    labelText: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    onValueChange: (String) -> Unit,
) {
    InputTextField(
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        modifier = modifier.fillMaxWidth()
            .height(height = 300.dp)
            .padding(vertical = Dimen.paddingDefault),
        label = {
            Text(labelText, style = AppTypography.body2)
        }
    )
}


@Composable
fun ReadOnlyInputTextField(
    value: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    labelText: String
) {
    InputTextField(
        value = value,
        onValueChange = {},
        isError = isError,
        modifier = modifier.fillMaxWidth()
            .height(height = 300.dp)
            .padding(vertical = Dimen.paddingDefault),
        readOnly = true,
        label = {
            Text(labelText, style = AppTypography.body2)
        }
    )
}

@Composable
fun ReadOnlyInputTextField(
    value: StateFlow<String>,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    labelText: String
) {
    var bases by remember { mutableStateOf("") }
    val string = rememberUpdatedState(bases);

    InputTextField(
        value = bases,
        onValueChange = {},
        isError = isError,
        modifier = modifier.fillMaxWidth()
            .height(height = 300.dp)
            .padding(vertical = Dimen.paddingDefault),
        readOnly = true,
        label = {
            Text(labelText, style = AppTypography.body2)
        }
    )

    LaunchedEffect(Unit) {
        value.collect {
            bases = string.value + it
            delay(500)
        }
    }
}