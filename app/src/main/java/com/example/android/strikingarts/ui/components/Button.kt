package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.Dp
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.ContentAlphaManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager

@Composable
fun DoubleButtonsRow(
    modifier: Modifier = Modifier,
    leftButtonText: String,
    rightButtonText: String,
    leftButtonEnabled: Boolean,
    rightButtonEnabled: Boolean,
    onLeftButtonClick: () -> Unit,
    onRightButtonClick: () -> Unit
) = Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
    Button(
        onClick = onLeftButtonClick,
        enabled = leftButtonEnabled,
        modifier = Modifier.padding(end = PaddingManager.Small)
    ) { Text(leftButtonText.toUpperCase(Locale.current)) }

    Button(
        onClick = onRightButtonClick,
        enabled = rightButtonEnabled,
        modifier = Modifier.padding(start = PaddingManager.Small)
    ) { Text(rightButtonText.toUpperCase(Locale.current)) }
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
) = Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
    TextButton(
        onClick = onLeftButtonClick,
        enabled = leftButtonEnabled,
        modifier = Modifier.padding(end = PaddingManager.Small)
    ) { Text(leftButtonText.toUpperCase(Locale.current)) }

    TextButton(
        onClick = onRightButtonClick,
        enabled = rightButtonEnabled,
        modifier = Modifier.padding(start = PaddingManager.Small)
    ) { Text(rightButtonText.toUpperCase(Locale.current)) }
}

@Composable
fun TextButtonOnElevatedSurface(
    text: String,
    onClick: () -> Unit,
    elevation: Dp,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }

    val containerColor = ColorManager.surfaceColorAtElevation(elevation)
    val contentColor = contentColorFor(containerColor)

    TextButton(
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(ContentAlphaManager.disabled),
            disabledContentColor = contentColor.copy(ContentAlphaManager.disabled),
        ), modifier = modifier.indication(
            interactionSource, rememberRipple(color = containerColor)
        ), interactionSource = interactionSource, onClick = onClick, enabled = enabled
    ) { Text(text) }
}