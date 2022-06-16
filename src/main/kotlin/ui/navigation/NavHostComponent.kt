package ui.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.essenty.parcelable.Parcelable
import model.Tool
import model.ToolRoute
import ui.screens.base.Base64Screen
import ui.screens.base.ToolComponent
import ui.screens.dashboard.DashboardComponent
import ui.screens.dashboard.DashboardScreen
import ui.screens.slug.SlugScreen

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
            is ScreenConfig.Item -> {
                when (configuration.route) {
                    ToolRoute.Base64.value -> base64(configuration)
                    ToolRoute.Slugs.value -> slugs(configuration)
                    else -> base64(configuration)
                }
            }
        } // Configurations are handled exhaustively

    /*
    * Routes
    * */
    private fun dashboard(): Content = DashboardComponent(
        onItemClicked = ::onItemClicked
    ).asContent { DashboardScreen(it) }

    private fun base64(configuration: ScreenConfig.Item): Content = ToolComponent(
        route = configuration.route,
        tool = configuration.tool,
        onBackPress = ::onBackClicked
    ).asContent { Base64Screen(it) }

    private fun slugs(configuration: ScreenConfig.Item): Content = ToolComponent(
        route = configuration.route,
        tool = configuration.tool,
        onBackPress = ::onBackClicked
    ).asContent { SlugScreen(it) }

    /**
     * Invoked when `GO` button clicked (InputScreen)
     */
    private fun onItemClicked(tool: Tool) {
        router.push(ScreenConfig.Item(route = tool.route.value, tool))
    }

    /**
     * Invoked when `GO BACK` button clicked (GreetingScreen)
     */
    private fun onBackClicked() {
        router.pop()
    }

    sealed class ScreenConfig : Parcelable {
        object Dashboard : ScreenConfig()
        data class Item(val route: String, val tool: Tool) : ScreenConfig()
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootUi(navHost: NavHostComponent) {
    Children(navHost.routerState) { child ->
        child.instance()
    }
}