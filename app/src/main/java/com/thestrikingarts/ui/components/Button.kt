package com.thestrikingarts.ui.components

import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.Dp
import com.thestrikingarts.R
import com.thestrikingarts.ui.theme.designsystemmanager.ColorManager
import com.thestrikingarts.ui.theme.designsystemmanager.ContentAlphaManager
import com.thestrikingarts.ui.theme.designsystemmanager.PaddingManager

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
    Button(onClick = onLeftButtonClick, enabled = leftButtonEnabled) {
        Text(leftButtonText.toUpperCase(Locale.current))
    }

    Button(onClick = onRightButtonClick, enabled = rightButtonEnabled) {
        Text(rightButtonText.toUpperCase(Locale.current))
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
) = Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
    TextButton(onClick = onLeftButtonClick, enabled = leftButtonEnabled) {
        Text(leftButtonText.toUpperCase(Locale.current))
    }

    TextButton(onClick = onRightButtonClick, enabled = rightButtonEnabled) {
        Text(rightButtonText.toUpperCase(Locale.current))
    }
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
            interactionSource, ripple(color = containerColor)
        ), interactionSource = interactionSource, onClick = onClick, enabled = enabled
    ) { Text(text) }
}

@Composable
fun DoneTextButton(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.all_done),
    setBottomSheetVisibility: (Boolean) -> Unit,
    onButtonClick: () -> Unit,
    buttonEnabled: Boolean = true
) = TextButton(
    onClick = { setBottomSheetVisibility(false); onButtonClick() },
    enabled = buttonEnabled,
    modifier = modifier.padding(top = PaddingManager.Medium)
) { Text(text.toUpperCase(Locale.current)) }