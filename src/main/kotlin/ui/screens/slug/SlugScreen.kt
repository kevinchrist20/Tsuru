package ui.screens.slug

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ui.components.ScreenLayout
import ui.theme.R

class SlugScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ScreenLayout(
            pageTitle = R.string.slugScreen,
            onBackPressed = {
                navigator.pop()
            }
        ) {
            Text("Welcome")
        }
    }
}