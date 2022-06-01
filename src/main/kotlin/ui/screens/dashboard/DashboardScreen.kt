package ui.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DashboardScreen(dashboard: DashboardComponent) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Hello, World")
    }
}