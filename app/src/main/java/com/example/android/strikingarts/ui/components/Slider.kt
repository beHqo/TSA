package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
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
    modifier: Modifier = Modifier
) {
    val startingNum = valueRange.start.toInt()
    val endingNum = valueRange.endInclusive.toInt()

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Text("$startingNum", style = MaterialTheme.typography.subtitle2)

        Slider(
            modifier = Modifier
                .weight(1F)
                .padding(horizontal = 4.dp),
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = (endingNum - startingNum).minus(1),
        )

        Text("$endingNum", style = MaterialTheme.typography.subtitle2)
    }
}