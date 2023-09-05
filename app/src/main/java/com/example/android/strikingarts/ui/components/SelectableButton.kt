package com.example.android.strikingarts.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.ui.shapes.generateHexagonPath
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager

@Composable
fun SelectableHexagonButton(
    selected: Boolean, onSelectionChange: (Boolean) -> Unit, modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        if (selected) ColorManager.primaryContainer else ColorManager.surface,
        animationSpec = tween(ANIMATION_DURATION)
    )
    val borderColor = ColorManager.onSurface

    Box(contentAlignment = Alignment.Center, modifier = modifier
        .size(24.dp)
        .drawBehind {
            val path = generateHexagonPath(size.width, size.height)

            drawPath(path = path, style = Stroke(width = 2.dp.toPx()), color = borderColor)

            drawPath(path = path, color = backgroundColor)
        }
        .clickableWithNoIndication { onSelectionChange(!selected) }
        .padding(2.dp)) {
        FadingAnimatedVisibility(visible = selected) {
            Text(text = "✓", color = contentColorFor(ColorManager.primaryContainer))
        }
    }
}