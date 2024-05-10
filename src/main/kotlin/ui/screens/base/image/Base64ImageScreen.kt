package ui.screens.base.image

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import model.Tool
import platform.extensions.copyToClipboard
import ui.components.*
import ui.theme.AppTypography
import ui.theme.R
import ui.util.Dimen
import java.io.File

class Base64ImageScreen(private val tool: Tool) : Screen {

    companion object {
        val IMAGE_FORMAT = listOf("jpg", "png", "jpeg")
    }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = rememberScreenModel { Base64ImageScreenModel() }

        val tabs = listOf(R.string.encodeText, R.string.decodeText)
        var selectedTabIndex by remember { mutableIntStateOf(0) }

        var canShowSnackBar by remember { mutableStateOf(false) }
        val lazyListState = rememberLazyListState()

        val imagePickerFieldText = remember { mutableStateOf(R.string.selectImage) }
        val decodeInputText = remember { mutableStateOf("") }

        var showFilePicker by remember { mutableStateOf(false) }
        FilePicker(show = showFilePicker, fileExtensions = IMAGE_FORMAT) { platformFile ->
            showFilePicker = false

            if (platformFile != null) {
                imagePickerFieldText.value = platformFile.path
                screenModel.encodeImage(platformFile)
            }
        }

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
                            screenModel.clear()
                            decodeInputText.value = ""
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
                            0 -> EncodePage(
                                screenModel = screenModel,
                                imagePickerFieldText = imagePickerFieldText.value,
                                onBrowseImageClick = {
                                    showFilePicker = true
                                }
                            )

                            1 -> DecodePage(
                                screenModel = screenModel,
                                inputText = decodeInputText
                            )
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
                                    if (selectedTabIndex == 0) {
                                        screenModel.encodedImageString.value.copyToClipboard()
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
                                    screenModel.clear()
                                    decodeInputText.value = ""
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
        screenModel: Base64ImageScreenModel,
        imagePickerFieldText: String,
        onBrowseImageClick: () -> Unit
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(4.dp)),
            border = BorderStroke(width = Dp.Hairline, color = R.color.Grey)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimen.paddingMicro, horizontal = Dimen.paddingSmall),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    imagePickerFieldText,
                    style = AppTypography.caption.copy(fontSize = 14.sp)
                )

                PrimaryButton(
                    onClick = onBrowseImageClick
                ) {
                    Text(R.string.browse)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        ReadOnlyInputTextField(
            value = screenModel.encodedImageString.value,
            labelText = R.string.outputBase64,
            modifier = Modifier,
        )
    }

    @Composable
    private fun DecodePage(
        screenModel: Base64ImageScreenModel,
        inputText: MutableState<String>
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        InputTextField(
            value = inputText.value,
            onValueChange = {
                inputText.value = it
                screenModel.decode(it)
            },
            modifier = Modifier,
            labelText = R.string.inputString
        )

        screenModel.decodeImageBitmap.value?.let { it1 ->
            Image(
                bitmap = it1,
                contentDescription = ""
            )
        }
    }
}