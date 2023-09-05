package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.ElevationManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.TypographyManager

@Composable
fun FilterChip(
    modifier: Modifier = Modifier, title: String, selected: Boolean, onClick: () -> Unit
) {
    val unSelectedColor = ColorManager.surfaceColorAtElevation(ElevationManager.Level1)
    val selectedColor = ColorManager.primaryContainer

    Box(
        modifier = modifier
            .selectable(selected = selected, onClick = onClick)
            .drawBehind { drawRect(color = if (selected) selectedColor else unSelectedColor) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = TypographyManager.bodyMedium,
            color = contentColorFor(if (selected) selectedColor else unSelectedColor),
            modifier = Modifier.padding(horizontal = PaddingManager.Medium)
        )
    }
}