package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@Composable
fun FilterChip(
    modifier: Modifier = Modifier, title: String, selected: Boolean, onClick: () -> Unit
) {
    val unSelectedColor = MaterialTheme.colors.surface
    val selectedColor = MaterialTheme.colors.primary

    Box(
        modifier = modifier
            .selectable(selected = selected, onClick = onClick)
            .shadow(3.dp)
            .drawBehind { drawRect(color = if (selected) selectedColor else unSelectedColor) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.body2,
            color = contentColorFor(if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.surface),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}