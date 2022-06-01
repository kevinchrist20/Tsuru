package ui.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.essenty.parcelable.Parcelable
import ui.screens.base.Base64Screen
import ui.screens.base.ToolComponent
import ui.screens.dashboard.DashboardComponent
import ui.screens.dashboard.DashboardScreen

typealias Content = @Composable () -> Unit

fun <T : Any> T.asContent(content: @Composable (T) -> Unit): Content = { content(this) }

class NavHostComponent(componentContext: ComponentContext) : ComponentContext by componentContext {
    private val router = router<ScreenConfig, Content>(
        initialConfiguration = ScreenConfig.Dashboard,
        childFactory = ::createChild
    )

    val routerState = router.state

    private fun createChild(configuration: ScreenConfig, context: ComponentContext): Content =
        when (configuration) {
            is ScreenConfig.Dashboard -> dashboard()
            is ScreenConfig.Tool -> base64(configuration)
        } // Configurations are handled exhaustively

    /*
    * Routes
    * */
    private fun dashboard(): Content = DashboardComponent(
        onItemClicked = ::onItemClicked
    ).asContent { DashboardScreen(it) }

    private fun base64(configuration: ScreenConfig.Tool): Content = ToolComponent(
        name = configuration.name,
        onBackPress = ::onBackClicked
    ).asContent { Base64Screen(it) }

    /**
     * Invoked when `GO` button clicked (InputScreen)
     */
    private fun onItemClicked(name: String) {
        router.push(ScreenConfig.Tool(name))
    }

    /**
     * Invoked when `GO BACK` button clicked (GreetingScreen)
     */
    private fun onBackClicked() {
        router.pop()
    }

    sealed class ScreenConfig : Parcelable {
        object Dashboard : ScreenConfig()
        data class Tool(val name: String) : ScreenConfig()
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootUi(navHost: NavHostComponent) {
    Children(navHost.routerState) { child ->
        child.instance()
    }
}