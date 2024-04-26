import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import ui.theme.R
import ui.util.Dimen

fun main() = application {
    Window(
        state = WindowState(
            size = DpSize(Dimen.screenWidth, Dimen.screenHeight),
            position = WindowPosition(alignment = Alignment.Center)
        ),
        onCloseRequest = ::exitApplication,
        title = R.string.appName,
    ) {
        App()
    }
}
