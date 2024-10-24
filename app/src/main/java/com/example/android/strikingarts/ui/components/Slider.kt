package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.TypographyManager
import com.example.android.strikingarts.ui.util.localized

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
        Text(startingNum.localized(), style = TypographyManager.titleSmall)

        Slider(
            modifier = Modifier
                .weight(1F)
                .padding(horizontal = PaddingManager.Medium),
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = (endingNum - startingNum).minus(1),
        )

        Text(endingNum.localized(), style = TypographyManager.titleSmall)
    }
}