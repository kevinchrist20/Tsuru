// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import ui.navigation.NavHostComponent
import ui.navigation.RootUi
import ui.theme.DevUtilsTheme
import ui.theme.R
import ui.util.Dimen

@Composable
@Preview
fun App() {
    val root = NavHostComponent(componentContext = DefaultComponentContext(lifecycle = LifecycleRegistry()))
    DevUtilsTheme {
        RootUi(root)
    }
}

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
