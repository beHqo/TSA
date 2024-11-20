package com.github.tsa.ui.theme.designsystemmanager

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.ln

@Immutable
object ColorManager {
    val primary: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.primary

    val onPrimary: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onPrimary

    val primaryContainer: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.primaryContainer

    val onPrimaryContainer: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onPrimaryContainer
//
//    val inversePrimary: Color
//        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.inversePrimary
//
//    val secondary: Color
//        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.secondary
//
//    val onSecondary: Color
//        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onSecondary
//
    val secondaryContainer: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.secondaryContainer

    val onSecondaryContainer: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onSecondaryContainer
//
//    val tertiary: Color
//        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.tertiary
//
//    val onTertiary: Color
//        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onTertiary
//
//    val tertiaryContainer: Color
//        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.tertiaryContainer
//
//    val onTertiaryContainer: Color
//        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onTertiaryContainer

    val background: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.background

    val onBackground: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onBackground

    val surface: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.surface

    val onSurface: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onSurface

    //
//    val surfaceVariant: Color
//        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.surfaceVariant
//
//    val onSurfaceVariant: Color
//        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onSurfaceVariant
//
    val surfaceTint: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.surfaceTint

//    val inverseSurface: Color
//        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.inverseSurface
//
//    val inverseOnSurface: Color
//        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.inverseOnSurface
//
    val error: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.error
//
//    val onError: Color
//        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onError
//
val errorContainer: Color
    @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.errorContainer

    val onErrorContainer: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onErrorContainer

//    val outline: Color
//        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.outline
//
//    val outlineVariant: Color
//        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.outlineVariant
//
//    val scrim: Color
//        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.scrim

    @Composable
    fun surfaceColorAtElevation(
        elevation: Dp,
    ): Color {
        if (elevation == 0.dp) return surface
        val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
        return surfaceTint.copy(alpha = alpha).compositeOver(surface)
    }
}