package ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import model.ToolRoute
import ui.components.SearchView
import ui.components.ToolCard
import ui.screens.base.Base64Screen
import ui.screens.slug.SlugScreen
import ui.util.Dimen

class DashboardScreen : Screen {
    @Composable
    override fun Content() {
        val dashboardScreenModel = rememberScreenModel { DashboardScreenModel() }
        val navigator = LocalNavigator.currentOrThrow

        var searchValue by remember { mutableStateOf("") }

        Column(modifier = Modifier.fillMaxSize().padding(Dimen.paddingDefault)) {
            SearchView(
                value = searchValue,
                onValueChange = {
                    searchValue = it
                },
                placeHolderText = "Search for any tools...",
                trailingIcon = Icons.Default.Search
            )

            Spacer(modifier = Modifier.height(Dimen.paddingLarge))

            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                items(dashboardScreenModel.tools.value) {
                    ToolCard(tool = it, onClicked = {
                        when (it.route) {
                            ToolRoute.Slugs -> {
                                navigator.push(SlugScreen())
                            }
                            ToolRoute.UUID -> {}
                            ToolRoute.Base64 -> {
                                navigator.push(Base64Screen())
                            }
                        }
                    })
                }
            }

            Spacer(modifier = Modifier.height(Dimen.paddingLarge))
        }
    }
}