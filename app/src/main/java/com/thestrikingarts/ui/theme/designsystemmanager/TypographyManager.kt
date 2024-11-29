package com.thestrikingarts.ui.theme.designsystemmanager

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import javax.annotation.concurrent.Immutable

@Immutable
object TypographyManager {
    //    val displayLarge: TextStyle
//        @Composable @ReadOnlyComposable get() = MaterialTheme.typography.displayLarge
//
//    val displayMedium: TextStyle
//        @Composable @ReadOnlyComposable get() = MaterialTheme.typography.displayMedium
//
    val displaySmall: TextStyle
        @Composable @ReadOnlyComposable get() = MaterialTheme.typography.displaySmall

    val headlineLarge: TextStyle
        @Composable @ReadOnlyComposable get() = MaterialTheme.typography.headlineLarge
//
//    val headlineMedium: TextStyle
//        @Composable @ReadOnlyComposable get() = MaterialTheme.typography.headlineMedium
//
//    val headlineSmall: TextStyle
//        @Composable @ReadOnlyComposable get() = MaterialTheme.typography.headlineSmall
//
    val titleLarge: TextStyle
        @Composable @ReadOnlyComposable get() = MaterialTheme.typography.titleLarge

    val titleMedium: TextStyle
        @Composable @ReadOnlyComposable get() = MaterialTheme.typography.titleMedium

    val titleSmall: TextStyle
        @Composable @ReadOnlyComposable get() = MaterialTheme.typography.titleSmall

    val bodyLarge: TextStyle
        @Composable @ReadOnlyComposable get() = MaterialTheme.typography.bodyLarge

    val bodyMedium: TextStyle
        @Composable @ReadOnlyComposable get() = MaterialTheme.typography.bodyMedium

    val bodySmall: TextStyle
        @Composable @ReadOnlyComposable get() = MaterialTheme.typography.bodySmall

    val labelLarge: TextStyle
        @Composable @ReadOnlyComposable get() = MaterialTheme.typography.labelLarge
//
//    val labelMedium: TextStyle
//        @Composable @ReadOnlyComposable get() = MaterialTheme.typography.labelMedium
//
//    val labelSmall: TextStyle
//        @Composable @ReadOnlyComposable get() = MaterialTheme.typography.labelSmall
}