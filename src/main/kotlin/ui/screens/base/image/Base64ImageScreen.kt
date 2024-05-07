package ui.screens.base.image

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
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
import model.Tool
import ui.components.PrimaryButton
import ui.components.ReadOnlyInputTextField
import ui.components.ScreenLayout
import ui.theme.AppTypography
import ui.theme.R

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

        val imagePickerFieldText = remember { mutableStateOf("Select Image") }

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
                0 -> {
                    Spacer(modifier = Modifier.height(24.dp))
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        shape = RoundedCornerShape(corner = CornerSize(4.dp)),
                        border = BorderStroke(width = Dp.Hairline, color = R.color.Grey)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                imagePickerFieldText.value,
                                style = AppTypography.caption.copy(fontSize = 14.sp)
                            )

                            PrimaryButton(
                                onClick = {
                                    showFilePicker = true
                                }
                            ) {
                                Text("Browse")
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

                1 -> {}
            }
        }
    }
}