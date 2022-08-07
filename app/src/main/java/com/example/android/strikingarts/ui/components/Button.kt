package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TwoButtonsRow(
    modifier: Modifier = Modifier,
    leftButtonText: String,
    rightButtonText: String,
    onLeftButtonClick: () -> Unit,
    onRightButtonClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onLeftButtonClick,
            modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
        ) { Text(leftButtonText) }

        Button(
            onClick = onRightButtonClick,
            modifier = Modifier.padding(end = 24.dp, bottom = 8.dp)
        ) { Text(rightButtonText) }
    }
}