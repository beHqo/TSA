package com.example.android.strikingarts.ui.components.detailsitem

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.ui.components.PrimaryText

@Composable
fun SelectableDetailsItem(
    modifier: Modifier = Modifier, text: String, selected: Boolean, onSelectionChange: () -> Unit
) {
    val backgroundColor by animateColorAsState(if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.surface)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .heightIn(min = 32.dp)
            .fillMaxWidth()
            .background(color = backgroundColor)
            .selectable(selected = selected, onClick = { onSelectionChange() })
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        PrimaryText(
            text = text,
            color = if (selected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
        )
    }
}