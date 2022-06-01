package ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

val LightTheme = lightColors() // TODO :
val DarkTheme = darkColors(
    primary = R.color.TelegramBlue,
    onPrimary = R.color.TextColor,
    secondary = R.color.DarkBackground,
    onSecondary = R.color.TextColor,
    surface = R.color.DarkShade,
    error = R.color.Red
)

@Composable
fun DevUtilsTheme(
    isDark: Boolean = true,
    content: @Composable (ColumnScope) -> Unit
) {
    MaterialTheme(
        colors = if (isDark) DarkTheme else LightTheme,
        typography = AppTypography
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                content(this)
            }
        }
    }
}