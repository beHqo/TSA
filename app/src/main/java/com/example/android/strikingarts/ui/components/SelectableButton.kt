package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RadioButtonWithName(
    name: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(Modifier.selectableGroup()) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = name,
            style = MaterialTheme.typography.body2
        )
    }
}