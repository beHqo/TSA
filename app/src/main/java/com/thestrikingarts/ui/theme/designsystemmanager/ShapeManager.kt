package com.thestrikingarts.ui.theme.designsystemmanager

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

object ShapeManager {
    val ExtraSmall: CornerBasedShape
        @Composable get() = MaterialTheme.shapes.extraSmall

    val Small: CornerBasedShape
        @Composable get() = MaterialTheme.shapes.small

    val Medium: CornerBasedShape
        @Composable get() = MaterialTheme.shapes.medium

    val Large: CornerBasedShape
        @Composable get() = MaterialTheme.shapes.large

    val ExtraLarge: CornerBasedShape
        @Composable get() = MaterialTheme.shapes.extraLarge
}