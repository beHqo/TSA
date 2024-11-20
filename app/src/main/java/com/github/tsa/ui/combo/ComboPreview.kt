package com.github.tsa.ui.combo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.window.Dialog
import com.github.tsa.R
import com.github.tsa.ui.components.PrimaryText
import com.github.tsa.ui.theme.designsystemmanager.ColorManager
import com.github.tsa.ui.theme.designsystemmanager.ElevationManager
import com.github.tsa.ui.theme.designsystemmanager.PaddingManager
import com.github.tsa.ui.theme.designsystemmanager.TypographyManager

@Composable
fun ComboPreviewDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onDismiss: () -> Unit,
    comboName: String = "",
    comboText: String = "",
    techniqueColor: Color = Color.Transparent,
    onPlay: () -> Unit,
) {
    if (visible) Dialog(onDismissRequest = onDismiss) {
        ComboPreview(
            modifier = modifier,
            comboName = comboName,
            comboText = comboText,
            techniqueColor = techniqueColor,
            onPlay = onPlay
        )
    }
}

@Composable
fun ComboPreview(
    modifier: Modifier = Modifier,
    comboName: String,
    comboText: String,
    techniqueColor: Color,
    onPlay: () -> Unit
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight(0.90F)
        .background(ColorManager.surfaceColorAtElevation(ElevationManager.Level3))
        .padding(horizontal = PaddingManager.Large)
) {
    Text(
        text = comboName,
        style = TypographyManager.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = ColorManager.primary,
        modifier = Modifier.padding(vertical = PaddingManager.Large)
    )
    Box(
        Modifier
            .fillMaxWidth()
            .weight(1F)
            .padding(bottom = PaddingManager.Large)
            .background(techniqueColor)
    )
    PrimaryText(text = comboText, maxLines = Int.MAX_VALUE)
    TextButton(
        onClick = onPlay, modifier = Modifier.padding(bottom = PaddingManager.Medium)
    ) { Text(text = stringResource(R.string.all_play).toUpperCase(Locale.current)) }
}