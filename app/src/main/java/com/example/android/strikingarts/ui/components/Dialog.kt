package com.example.android.strikingarts.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.github.skydoves.colorpicker.compose.ColorPickerController


@Composable
fun ConfirmDialog(
    @StringRes titleId: Int,
    @StringRes textId: Int,
    @StringRes confirmButtonTextId: Int,
    @StringRes dismissButtonTextId: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        properties = DialogProperties(dismissOnClickOutside = false),
        title = { Text(stringResource(titleId)) },
        text = { Text(stringResource(textId)) },
        onDismissRequest = onDismiss,
        dismissButton = { TextButton(onDismiss) { Text(stringResource(dismissButtonTextId)) } },
        confirmButton = { TextButton(onConfirm) { Text(stringResource(confirmButtonTextId)) } }
    )
}

@Composable
fun ColorPickerDialog(
    controller: ColorPickerController,
    techniqueColor: String,
    onColorChange: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        properties = DialogProperties(dismissOnClickOutside = false),
        onDismissRequest = onDismiss,
        buttons = { ColorPicker(controller, techniqueColor, onColorChange) }
    )
}