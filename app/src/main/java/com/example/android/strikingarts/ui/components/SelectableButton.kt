package com.example.android.strikingarts.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import com.example.android.strikingarts.ui.theme.designsystemmanager.BorderStrokeWidthManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.ShapeManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.SizeManager.SelectionSize

@Composable
fun SelectableHexagonButton(
    selected: Boolean, onSelectionChange: (Boolean) -> Unit, modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) ColorManager.primaryContainer else ColorManager.surface,
        animationSpec = tween(ANIMATION_DURATION),
        label = "Animate Background Color"
    )
    val borderColor = ColorManager.onSurface
    val shape = ShapeManager.ExtraSmall

    Box(contentAlignment = Alignment.Center,
        modifier = modifier
            .size(SelectionSize)
            .clip(shape)
            .drawBehind { drawRect(color = backgroundColor) }
            .border(BorderStroke(BorderStrokeWidthManager.Level1, borderColor), shape)
            .clickableWithNoIndication { onSelectionChange(!selected) }
            .padding(PaddingManager.ExtraSmall)) {
        FadingAnimatedVisibility(visible = selected) {
            Text(text = "✓", color = contentColorFor(ColorManager.primaryContainer))
        }
    }
}