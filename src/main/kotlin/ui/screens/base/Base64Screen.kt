package ui.screens.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import model.Tool
import platform.extensions.copyToClipboard
import ui.components.PrimaryButton
import ui.components.ScreenLayout
import ui.components.SecondaryButton
import ui.theme.R
import ui.util.Dimen

class Base64Screen(private val tool: Tool) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val tabs = listOf("Encode", "Decode")
        val screenModel = rememberScreenModel { Base64ScreenModel() }

        val isCopyPressed = remember { mutableStateOf(false) }
        var selectedTabIndex by remember { mutableIntStateOf(0) }

        ScreenLayout(
            pageTitle = tool.name,
            onBackPressed = {
                navigator.pop()
            },
            showIsCopiedSnackbar = isCopyPressed.value
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                backgroundColor = Color.Transparent,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = R.color.TelegramBlue
                    )
                }
            ) {
                tabs.forEachIndexed { index, text ->
                    val selected = selectedTabIndex == index
                    val bgColor = if (selected) R.color.DarkBackground else Color.Transparent

                    Tab(
                        selected = selected,
                        onClick = { selectedTabIndex = index },
                        modifier = Modifier.background(bgColor),
                        text = {
                            Text(
                                text = text,
                                color = if (selected) R.color.TelegramBlue else R.color.LightGrey,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> EncodePage(screenModel)
                1 -> DecodePage(screenModel)
                else -> EncodePage(screenModel)
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = Dimen.paddingDefault),
            ) {
                PrimaryButton(
                    modifier = Modifier,
                    onClick = {
                        screenModel.encoded.value.copyToClipboard()
                        isCopyPressed.value = true
                    }
                ) {
                    Text("Copy")
                }

                Spacer(modifier = Modifier.width(Dimen.paddingDefault))

                SecondaryButton(
                    modifier = Modifier,
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(backgroundColor = R.color.Red),
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        "clear_icon",
                        tint = R.color.WhiteSmoke
                    )

                    Text(
                        "Clear",
                        color = R.color.WhiteSmoke
                    )
                }
            }
        }
    }

    @Composable
    private fun EncodePage(
        screenModel: Base64ScreenModel
    ) {
        var data by remember { mutableStateOf("") }

        TextField(
            value = data,
            onValueChange = {
                data = it
                screenModel.encode(data)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 200.dp)
                .padding(top = Dimen.paddingMedium),
        )

        TextField(
            value = screenModel.encoded.value,
            readOnly = true,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 200.dp)
                .padding(vertical = Dimen.paddingDefault),
        )
    }

    @Composable
    private fun DecodePage(
        screenModel: Base64ScreenModel
    ) {
        var data by remember { mutableStateOf("") }

        TextField(
            value = data,
            onValueChange = {
                data = it
                screenModel.decode(data)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 200.dp)
                .padding(top = Dimen.paddingMedium),
        )

        TextField(
            value = screenModel.decoded.value,
            readOnly = true,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 200.dp)
                .padding(vertical = Dimen.paddingDefault),
        )
    }
}

