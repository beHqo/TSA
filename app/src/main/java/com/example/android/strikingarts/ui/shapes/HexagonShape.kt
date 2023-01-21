package com.example.android.strikingarts.ui.shapes

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

fun generateHexagonPath(width: Float, height: Float) = Path().apply {
    moveTo(width / 2f, 0f)

    lineTo(width, height / 4)
    lineTo(width, height / 4 * 3)
    lineTo(width / 2, height)
    lineTo(0f, height / 4 * 3)
    lineTo(0f, height / 4)

    close()
}

class HexagonShape : Shape {
    override fun createOutline(
        size: Size, layoutDirection: LayoutDirection, density: Density
    ): Outline {
        return Outline.Generic(generateHexagonPath(size.width, size.height))
    }
}