package com.example.android.strikingarts.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun TwoButtonsRow(
    modifier: Modifier = Modifier,
    @StringRes leftButtonTextId: Int,
    @StringRes rightButtonTextId: Int,
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
        ) { Text(
            stringResource(leftButtonTextId)) }

        Button(
            onClick = onRightButtonClick,
            modifier = Modifier.padding(end = 24.dp, bottom = 8.dp)
        ) { Text(
            stringResource(rightButtonTextId)) }
    }
}