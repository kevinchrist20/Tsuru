package ui.screens.base.image

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import model.Tool
import platform.extensions.copyToClipboard
import ui.components.*
import ui.theme.AppTypography
import ui.theme.R
import ui.util.Dimen


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
            },
            bottomNavContent = {
                BottomNavContent(
                    onDeleteClick = {
                        screenModel.clear()
                        decodeInputText.value = ""
                    },
                    onCopyClick = {
                        canShowSnackBar = true

                        if (selectedTabIndex == 0) {
                            screenModel.encodedImageString.value.copyToClipboard()
                        }
                    }
                )
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            end = Dimen.paddingSmall
                        ),
                    state = lazyListState
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
                }

                if (screenModel.loading.value) {
                    AlertDialog(
                        properties = DialogProperties(
                            dismissOnClickOutside = false
                        ),
                        modifier = Modifier.align(Alignment.Center),
                        text = {
                            Column {
                                Spacer(modifier = Modifier.height(16.dp))
                                LinearProgressIndicator()
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Encoding image...")
                            }

                        },
                        buttons = {},
                        onDismissRequest = {}
                    )
//                    Row(
//                        horizontalArrangement = Arrangement.SpaceAround,
//                        modifier = Modifier.width(100.dp)
//                    ) {
//                        BubbleLoadingAnimation()
//                        BubbleLoadingAnimation()
//                        BubbleLoadingAnimation()
//                    }
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
            value = screenModel.encodedImageString,
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

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(4.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimen.paddingDefault),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Preview Image",
                    style = AppTypography.subtitle2
                )

                Spacer(modifier = Modifier.height(Dimen.paddingDefault))

                screenModel.decodeImageBitmap.value?.let { it1 ->
                    Image(
                        bitmap = it1,
                        contentDescription = "decoded_image",
                        modifier = Modifier.padding(bottom = Dimen.paddingMedium)
                    )
                }
            }
        }
    }

    @Composable
    fun BubbleLoadingAnimation() {
        val scale = remember { Animatable(0.5f) }

        Box(
            modifier = Modifier
                .size(20.dp)
                .scale(scale.value)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(color = Color.Blue)
            }
        }

        LaunchedEffect(key1 = true) {
            while (true) {
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 500, easing = LinearEasing)
                )
                scale.animateTo(
                    targetValue = 0.5f,
                    animationSpec = tween(durationMillis = 500, easing = LinearEasing)
                )
            }
        }
    }
}