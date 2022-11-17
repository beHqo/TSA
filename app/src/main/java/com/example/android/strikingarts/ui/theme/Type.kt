package com.example.android.strikingarts.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.android.strikingarts.R

private val eczarFamily = FontFamily(
    Font(R.font.eczar_regular),
    Font(R.font.eczar_medium, FontWeight.Medium),
    Font(R.font.eczar_semibold, FontWeight.SemiBold)
)

private val inknutAntiquaFamily = FontFamily(Font(R.font.inknut_antiqua_light, FontWeight.Light))

// Set of Material typography styles to start with
val Typography = Typography(
    defaultFontFamily = eczarFamily, h1 = TextStyle(
        fontFamily = inknutAntiquaFamily,
        fontWeight = FontWeight.Light,
        fontSize = 96.sp,
        letterSpacing = (-1.5).sp
    ), h2 = TextStyle(
        fontFamily = inknutAntiquaFamily,
        fontWeight = FontWeight.Light,
        fontSize = 60.sp,
        letterSpacing = (-0.5).sp
    ), h3 = TextStyle(
        fontFamily = eczarFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
        letterSpacing = 0.sp
    ), h4 = TextStyle(
        fontFamily = eczarFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp,
        letterSpacing = (0.25).sp
    ), h5 = TextStyle(
        fontFamily = eczarFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        letterSpacing = 0.sp
    ), h6 = TextStyle(
        fontFamily = eczarFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        letterSpacing = (0.15).em
    ), subtitle1 = TextStyle(
        fontFamily = eczarFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = (0.15).sp
    ), subtitle2 = TextStyle(
        fontFamily = eczarFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = (0.1).sp
    ), body1 = TextStyle(
        fontFamily = eczarFamily,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
        fontSize = 16.sp,
        letterSpacing = (0.5).sp
    ), body2 = TextStyle(
        fontFamily = eczarFamily,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
        fontSize = 14.sp,
        letterSpacing = (0.25).sp
    ), button = TextStyle(
        fontFamily = eczarFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = (1.25).sp
    ), caption = TextStyle(
        fontFamily = eczarFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        letterSpacing = (0.4).sp
    ), overline = TextStyle(
        fontFamily = eczarFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        letterSpacing = (1.5).sp
    )
)