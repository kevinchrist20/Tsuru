import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import ui.screens.dashboard.DashboardScreen
import ui.theme.DevUtilsTheme

@Composable
@Preview
fun App() {
    DevUtilsTheme {
        Navigator(DashboardScreen()) { navigator ->
            SlideTransition(navigator)
        }
    }
}