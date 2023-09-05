package com.example.android.strikingarts.ui.theme.designsystemmanager

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.luminance


private object HighContrastContentAlpha {
    const val high: Float = 1.00f
    const val medium: Float = 0.74f
    const val disabled: Float = 0.38f
}

private object LowContrastContentAlpha {
    const val high: Float = 0.87f
    const val medium: Float = 0.60f
    const val disabled: Float = 0.38f
}

object ContentAlphaManager {
    /**
     * A high level of content alpha, used to represent high emphasis text such as input text in a
     * selected [TextField].
     */
    val high: Float
        @Composable
        get() = contentAlpha(
            highContrastAlpha = HighContrastContentAlpha.high,
            lowContrastAlpha = LowContrastContentAlpha.high
        )

    /**
     * A medium level of content alpha, used to represent medium emphasis text such as
     * placeholder text in a [TextField].
     */
    val medium: Float
        @Composable
        get() = contentAlpha(
            highContrastAlpha = HighContrastContentAlpha.medium,
            lowContrastAlpha = LowContrastContentAlpha.medium
        )

    /**
     * A low level of content alpha used to represent disabled components, such as text in a
     * disabled [Button].
     */
    val disabled: Float
        @Composable
        get() = contentAlpha(
            highContrastAlpha = HighContrastContentAlpha.disabled,
            lowContrastAlpha = LowContrastContentAlpha.disabled
        )

    /**
     * This default implementation uses separate alpha levels depending on the luminance of the
     * incoming color, and whether the theme is light or dark. This is to ensure correct contrast
     * and accessibility on all surfaces.
     *
     * See [HighContrastContentAlpha] and [LowContrastContentAlpha] for what the levels are
     * used for, and under what circumstances.
     */
    @Composable
    private fun contentAlpha(
        /*@FloatRange(from = 0.0, to = 1.0)*/
        highContrastAlpha: Float,
        /*@FloatRange(from = 0.0, to = 1.0)*/
        lowContrastAlpha: Float
    ): Float {
        val contentColor = LocalContentColor.current
        val lightTheme = !isSystemInDarkTheme()
        return if (lightTheme) {
            if (contentColor.luminance() > 0.5) highContrastAlpha else lowContrastAlpha
        } else {
            if (contentColor.luminance() < 0.5) highContrastAlpha else lowContrastAlpha
        }
    }
}
