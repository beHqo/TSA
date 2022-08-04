package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterChip(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .toggleable(
                value = selected,
                onValueChange = {
                    onClick()
                }
            ),
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
        color = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = if (selected) "✓$title" else title,
                style = MaterialTheme.typography.body2,
                color = if (selected)
                    MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
                modifier =
                if (selected) Modifier.padding(start = 8.dp, end = 12.dp)
                else Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}