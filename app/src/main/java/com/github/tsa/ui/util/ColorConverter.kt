package com.github.tsa.ui.util

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

/**
 * Converts the value to its `compose.ui.graphics.Color` equivalent, or transparent if the string
 * cannot be parsed.
 * */
fun String.toComposeColor(): Color {
    val colorInt: Int

    try {
        colorInt = this.toColorInt()
    } catch (e: IllegalArgumentException) {
        Log.e("ColorConverter", "toComposeColor: Failed to parse {$this} to color int.")
        return Color.Transparent
    }

    return Color(colorInt)
}

fun Color.toHexCode(): String {
    val alpha = (this.alpha * 255).toInt()
    val red = (this.red * 255).toInt()
    val green = (this.green * 255).toInt()
    val blue = (this.blue * 255).toInt()

    return String.format("#%02X%02X%02X%02X", alpha, red, green, blue)
}