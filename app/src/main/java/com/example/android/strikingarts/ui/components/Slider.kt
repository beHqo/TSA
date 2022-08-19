package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DelaySlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    startingNumber: String,
    finishingNumber: String,
    modifier: Modifier = Modifier
) {

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Text(text = startingNumber, style = MaterialTheme.typography.subtitle2)
        Slider(
            modifier = Modifier
                .weight(1F)
                .padding(start = 4.dp, end = 4.dp),
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = ((valueRange.endInclusive - valueRange.start).minus(1)).toInt(),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colors.secondary,
                activeTickColor = MaterialTheme.colors.secondary,
                inactiveTickColor = MaterialTheme.colors.secondary,
                activeTrackColor = MaterialTheme.colors.secondary,
                inactiveTrackColor = MaterialTheme.colors.onSurface.copy(alpha = 0.24F)
            )
        )
        Text(text = finishingNumber, style = MaterialTheme.typography.subtitle2)
    }
}