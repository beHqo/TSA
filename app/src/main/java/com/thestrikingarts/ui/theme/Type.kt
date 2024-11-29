package com.thestrikingarts.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.thestrikingarts.R

private val eczarFamily = FontFamily(
    Font(R.font.eczar_regular),
    Font(R.font.eczar_medium, FontWeight.Medium),
    Font(R.font.eczar_semibold, FontWeight.SemiBold)
)

val defaultTypography = Typography()

val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = eczarFamily),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = eczarFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = eczarFamily),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = eczarFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = eczarFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = eczarFamily),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = eczarFamily),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = eczarFamily),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = eczarFamily),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = eczarFamily),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = eczarFamily),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = eczarFamily),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = eczarFamily),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = eczarFamily),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = eczarFamily)
)