package com.example.android.strikingarts.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//private val DarkColorPalette = darkColors(
//    primary = Purple200,
//    primaryVariant = Purple700,
//    secondary = Teal200
//)
//
//private val LightColorPalette = lightColors(
//    primary = Purple500,
//    primaryVariant = Purple700,
//    secondary = Teal200
//
//    /* Other default colors to override
//    background = Color.White,
//    surface = Color.White,
//    onPrimary = Color.White,
//    onSecondary = Color.Black,
//    onBackground = Color.Black,
//    onSurface = Color.Black,
//    */
//)

private val DarkColorPalette = darkColors(
    primary = Wood,
    primaryVariant = WoodVariantLight,
    secondary = Color.White,
    secondaryVariant = Color.Gray,
    onSecondary = Color.Black,
)

private val LightColorPalette = lightColors(
    primary = Wood,
    primaryVariant = WoodVariantLight,
    secondary = Color.Black,
    secondaryVariant = Color.DarkGray,
    onSecondary = Color.White
    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

//private val LightColorPalette = lightColors(
//    primary = Blue,
////    primaryVariant = CrimsonVariantLight,
//    secondary = Color.Black,
//    secondaryVariant = Color.DarkGray,
//    onSecondary = Color.White,
//    background = Wood,
//    onBackground = Color.Black,
//    surface = Wood,
//    onSurface = Color.Black
//    /* Other default colors to override
//    onPrimary = Color.White,
//    onSecondary = Color.Black,
//    */
//)

@Composable
fun StrikingArtsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}