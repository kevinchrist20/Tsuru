package ui.screens.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.components.ScreenLayout
import ui.theme.R
import ui.util.Dimen


@Composable
fun Base64Screen(component: ToolComponent) {
    val base64 = remember { Base64() }
    var data by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf(false) }

    var selectedIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Encode", "Decode")

    ScreenLayout(toolComponent = component) {
        Column(modifier = Modifier.padding(Dimen.paddingDefault)) {
            TabRow(selectedTabIndex = selectedIndex,
                backgroundColor = R.color.DarkBackground,
                modifier = Modifier
                    .padding(vertical = Dimen.paddingMicro, horizontal = Dimen.paddingNano)
                    .clip(RoundedCornerShape(50))
                    .padding(1.dp),
                indicator = { tabPositions: List<TabPosition> ->
                    Box {}
                }
            ) {
                tabs.forEachIndexed { index, text ->
                    val selected = selectedIndex == index
                    Tab(
                        modifier = if (selected) Modifier
                            .clip(RoundedCornerShape(50))
                            .background(R.color.WhiteSmoke)
                        else Modifier
                            .clip(RoundedCornerShape(50))
                            .background(R.color.LightGrey),
                        selected = selected,
                        onClick = { selectedIndex = index },
                        text = { Text(text = text, color = R.color.OutlineColor) }
                    )
                }
            }
//            TextField(
//                value = data,
//                onValueChange = {
//                    data = it
//                    base64.encode(data)
//                },
//                modifier = Modifier.fillMaxWidth().height(height = 200.dp).padding(bottom = Dimen.paddingDefault),
//            )
//
//            TextField(
//                value = base64.encoded.value,
//                readOnly = true,
//                onValueChange = {},
//                modifier = Modifier.fillMaxWidth().height(height = 200.dp).padding(bottom = Dimen.paddingDefault),
//            )
//
//            Button(
//                onClick = {
//                    copyToClipboard(base64.encoded.value)
//                    showToast = true
//                }) {
//                Text(
//                    "Copy".uppercase(),
//                    style = AppTypography.button.copy(fontSize = 18.sp),
//                    modifier = Modifier.padding(vertical = Dimen.paddingMicro, horizontal = Dimen.paddingDefault),
//                )
//            }
        }
    }
}