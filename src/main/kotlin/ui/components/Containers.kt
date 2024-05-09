package ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.theme.AppTypography
import ui.theme.R
import ui.util.Dimen

@Composable
fun ErrorContainer(
    message: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFF8B0000),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(5.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(Dimen.paddingSmall),
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "error_icon",
                tint = Color(0xFFff7373)
            )

            Column(
                modifier = Modifier.fillMaxWidth().padding(start = Dimen.paddingSmall),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "Error",
                    style = AppTypography.subtitle1.copy(color = Color(0xFFff7373))
                )

                Spacer(modifier = Modifier.height(Dimen.paddingMicro))

                Text(
                    message,
                    style = AppTypography.body2.copy(color = R.color.WhiteSmoke)
                )
            }
        }
    }
}