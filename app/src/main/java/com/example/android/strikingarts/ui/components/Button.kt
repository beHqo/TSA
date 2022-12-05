package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp

@Composable
fun DoubleButtonsRow(
    modifier: Modifier = Modifier,
    leftButtonText: String,
    rightButtonText: String,
    leftButtonEnabled: Boolean,
    rightButtonEnabled: Boolean,
    onLeftButtonClick: () -> Unit,
    onRightButtonClick: () -> Unit
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Button(
            onClick = onLeftButtonClick,
            enabled = leftButtonEnabled,
            modifier = Modifier.padding(end = 4.dp)
        ) { Text(leftButtonText.toUpperCase(Locale.current)) }

        Button(
            onClick = onRightButtonClick,
            enabled = rightButtonEnabled,
            modifier = Modifier.padding(start = 4.dp)
        ) { Text(rightButtonText.toUpperCase(Locale.current)) }
    }
}

@Composable
fun DoubleTextButtonRow(
    modifier: Modifier = Modifier,
    leftButtonText: String,
    rightButtonText: String,
    leftButtonEnabled: Boolean,
    rightButtonEnabled: Boolean,
    onLeftButtonClick: () -> Unit,
    onRightButtonClick: () -> Unit
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        TextButton(
            onClick = onLeftButtonClick,
            enabled = leftButtonEnabled,
            modifier = Modifier.padding(end = 4.dp)
        ) { Text(leftButtonText.toUpperCase(Locale.current)) }

        TextButton(
            onClick = onRightButtonClick,
            enabled = rightButtonEnabled,
            modifier = Modifier.padding(start = 4.dp)
        ) { Text(rightButtonText.toUpperCase(Locale.current)) }
    }
}

@Composable
fun TextButtonOnPrimary(
    text: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean
) {
    val interactionSource = remember { MutableInteractionSource() }

    TextButton(
        modifier = modifier.indication(
            interactionSource, rememberRipple(color = MaterialTheme.colors.onPrimary)
        ), interactionSource = interactionSource, onClick = onClick, enabled = enabled
    ) {
        Text(
            text, color = if (enabled) MaterialTheme.colors.onPrimary
            else MaterialTheme.colors.onPrimary.copy(alpha = ContentAlpha.disabled)
        )
    }
}