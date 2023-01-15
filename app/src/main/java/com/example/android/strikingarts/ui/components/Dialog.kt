package com.example.android.strikingarts.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
