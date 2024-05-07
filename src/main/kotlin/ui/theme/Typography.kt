package ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.sp

val JetBrainsMono = FontFamily(
    Font("fonts/Inter-Light.ttf", FontWeight.Light),
    Font("fonts/Inter-Regular.ttf", FontWeight.Normal),
    Font("fonts/Inter-Medium.ttf", FontWeight.Medium),
    Font("fonts/Inter-Bold.ttf", FontWeight.Bold),
    Font("fonts/Inter-SemiBold.ttf", FontWeight.SemiBold),
)

val AppTypography = Typography(
//    defaultFontFamily = JetBrainsMono,

    h1 = TextStyle(
        fontSize = 95.sp,
        fontWeight = FontWeight.Bold,
    ),
    h2 = TextStyle(
        fontSize = 59.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    h3 = TextStyle(
        fontSize = 48.sp,
        fontWeight = FontWeight.SemiBold
    ),
    h4 = TextStyle(
        fontSize = 34.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    h5 = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold
    ),
    h6 = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
    ),
    subtitle1 = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
    ),
    subtitle2 = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
    ),
    body1 = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
    ),
    body2 = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
    ),
    button = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    ),
    caption = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Light,
    ),
    overline = TextStyle(
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
    )
)