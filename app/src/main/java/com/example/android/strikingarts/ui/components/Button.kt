package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp

@Composable
fun TwoButtonsRow(
    modifier: Modifier = Modifier,
    leftButtonText: String,
    rightButtonText: String,
    onLeftButtonClick: () -> Unit,
    onRightButtonClick: () -> Unit
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Button(
            onClick = onLeftButtonClick,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        ) { Text(leftButtonText.toUpperCase(Locale.current)) }

        Button(
            onClick = onRightButtonClick,
            modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
        ) { Text(rightButtonText.toUpperCase(Locale.current)) }
    }
}