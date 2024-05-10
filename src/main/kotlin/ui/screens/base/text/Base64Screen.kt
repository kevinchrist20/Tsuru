package ui.screens.base.text

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import model.Tool
import platform.extensions.copyToClipboard
import ui.components.*
import ui.theme.R
import ui.util.Dimen

class Base64Screen(private val tool: Tool) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val tabs = listOf(R.string.encodeText, R.string.decodeText)
        val screenModel = rememberScreenModel { Base64ScreenModel() }

        var canShowSnackBar by remember { mutableStateOf(false) }
        var selectedTabIndex by remember { mutableIntStateOf(0) }

        val encodeInputText = remember { mutableStateOf("") }
        val decodeInputText = remember { mutableStateOf("") }

        val lazyListState = rememberLazyListState()

        ScreenLayout(
            tool = tool,
            onBackPressed = {
                navigator.pop()
            },
            snackbarMessage = R.string.copiedText,
            showSnackbar = canShowSnackBar,
            onShowSnackbar = {
                canShowSnackBar = it
            }
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
                        onClick = {
                            selectedTabIndex = index
                            if (selectedTabIndex == 0) {
                                screenModel.clearEncodedValue()
                                encodeInputText.value = ""
                            } else {
                                screenModel.clearDecodedValue()
                                decodeInputText.value = ""
                            }
                        },
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

            Box {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(
                            end = Dimen.paddingSmall
                        ), lazyListState
                ) {
                    item {
                        when (selectedTabIndex) {
                            0 -> EncodePage(screenModel, encodeInputText)
                            1 -> DecodePage(screenModel, decodeInputText)
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Dimen.paddingDefault),
                        ) {
                            PrimaryButton(
                                modifier = Modifier,
                                onClick = {
                                    canShowSnackBar = true

                                    if (selectedTabIndex == 0) {
                                        screenModel.encoded.value.copyToClipboard()
                                    } else {
                                        screenModel.decoded.value.copyToClipboard()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    "clear_icon",
                                    tint = R.color.WhiteSmoke
                                )

                                Text(
                                    R.string.copyText,
                                    color = R.color.WhiteSmoke
                                )
                            }

                            Spacer(modifier = Modifier.width(Dimen.paddingDefault))

                            SecondaryButton(
                                modifier = Modifier,
                                onClick = {
                                    if (selectedTabIndex == 0) {
                                        screenModel.clearEncodedValue()
                                        encodeInputText.value = ""
                                    } else {
                                        screenModel.clearDecodedValue()
                                        decodeInputText.value = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = R.color.Red),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    "clear_icon",
                                    tint = R.color.WhiteSmoke
                                )

                                Text(
                                    R.string.clearText,
                                    color = R.color.WhiteSmoke
                                )
                            }
                        }
                    }
                }

                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(
                        scrollState = lazyListState
                    )
                )
            }
        }
    }

    @Composable
    private fun EncodePage(
        screenModel: Base64ScreenModel,
        inputText: MutableState<String>
    ) {

        InputTextField(
            value = inputText.value,
            onValueChange = {
                inputText.value = it
                screenModel.encode(it)
            },
            modifier = Modifier,
            labelText = R.string.inputString
        )

        ReadOnlyInputTextField(
            value = screenModel.encoded.value,
            labelText = R.string.outputBase64,
            modifier = Modifier,
        )
    }

    @Composable
    private fun DecodePage(
        screenModel: Base64ScreenModel,
        inputText: MutableState<String>
    ) {
        InputTextField(
            value = inputText.value,
            onValueChange = {
                inputText.value = it
                screenModel.decode(it)
            },
            modifier = Modifier,
            labelText = R.string.inputBase64
        )

        if (!screenModel.decodingErr.value.isNullOrEmpty()) {
            ErrorContainer(
                message = screenModel.decodingErr.value ?: "",
                modifier = Modifier.padding(vertical = Dimen.paddingDefault)
            )
        }

        ReadOnlyInputTextField(
            value = screenModel.decoded.value,
            labelText = R.string.outputString,
            modifier = Modifier
        )
    }
}

