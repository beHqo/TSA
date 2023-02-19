package com.example.android.strikingarts.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.ui.shapes.HexagonShape
import com.example.android.strikingarts.ui.shapes.generateHexagonPath

@Composable
fun HexagonRadioButton(
    selected: Boolean, onSelectionChange: (Boolean) -> Unit, modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        if (selected) MaterialTheme.colors.primarySurface else MaterialTheme.colors.surface
    )
    val borderColor = MaterialTheme.colors.onSurface

    Box(modifier = modifier
        .size(20.dp)
        .drawWithCache {
            onDrawBehind {
                val path = generateHexagonPath(size.width, size.height)

                drawPath(path = path, style = Stroke(width = 2.dp.toPx()), color = borderColor)
            }
        }
        .background(color = backgroundColor, shape = HexagonShape)
        .clickableWithNoIndication { onSelectionChange(!selected) }
        .padding(2.dp)) {
        FadingAnimatedVisibility(Modifier.offset(x = 2.dp, y = (-2).dp), selected) {
            Text(text = "✓", color = MaterialTheme.colors.onPrimary)
        }
    }
}