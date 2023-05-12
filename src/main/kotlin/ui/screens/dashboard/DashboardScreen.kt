package ui.screens.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import ui.components.SearchView
import ui.components.toolCard
import ui.util.Dimen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardScreen(dashboard: DashboardComponent) {
    var searchValue by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(Dimen.paddingDefault)) {
        SearchView(
            value = searchValue,
            onValueChange = {
                searchValue = it
            },
            placeHolderText = "Search for any tool...",
            trailingIcon = Icons.Default.Search
        )

        Spacer(modifier = Modifier.height(Dimen.paddingLarge))

        LazyVerticalGrid(cells = GridCells.Fixed(3)) {
            items(dashboard.tools.value) {
                toolCard(tool = it, onClicked = { dashboard.onItemClicked(it) })
            }
        }

        Spacer(modifier = Modifier.height(Dimen.paddingLarge))
    }
}