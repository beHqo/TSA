package com.example.android.strikingarts.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import com.github.skydoves.colorpicker.compose.ColorPickerController


@Composable
fun ConfirmDialog(
    titleId: String,
    textId: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(modifier = modifier,
        title = { Text(titleId) },
        text = { Text(textId) },
        onDismissRequest = onDismiss,
        dismissButton = { TextButton(onDismiss) { Text(dismissButtonText) } },
        confirmButton = { TextButton(onConfirm) { Text(confirmButtonText) } })
}

@Composable
fun ColorPickerDialog(
    controller: ColorPickerController,
    techniqueColor: String,
    onColorChange: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(modifier = modifier,
        properties = DialogProperties(dismissOnClickOutside = false),
        onDismissRequest = onDismiss,
        buttons = { ColorPicker(controller, techniqueColor, onColorChange) })
}
