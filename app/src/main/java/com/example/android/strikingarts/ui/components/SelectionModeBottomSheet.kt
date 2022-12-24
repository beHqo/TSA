package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Delete
import androidx.compose.material.icons.sharp.Deselect
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
import androidx.compose.material.icons.sharp.KeyboardArrowRight
import androidx.compose.material.icons.sharp.SelectAll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun SelectionModeBottomSheet(
    modifier: Modifier = Modifier,
    visible: Boolean,
    buttonsEnabled: Boolean,
    shrunkStateText: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    onSelectAll: () -> Unit,
    onDeselectAll: () -> Unit,
    onDelete: () -> Unit,
) {
    var expanded by rememberSaveable(visible) { mutableStateOf(false) }

    ExpandOrShrinkVertically(visible = visible, modifier = modifier) {
        ExpandOrShrinkHorizontally(expanded = !expanded) {
            SelectionModeBottomSheetShrunkState(
                onExpand = { expanded = true }, shrunkStateText = shrunkStateText
            )
        }
        ExpandOrShrinkHorizontally(expanded = expanded) {
            SelectionModeBottomSheetExpandedState(
                onShrink = { expanded = false },
                buttonsEnabled = buttonsEnabled,
                buttonText = buttonText,
                onButtonClick = onButtonClick,
                onSelectAll = onSelectAll,
                onDeselectAll = onDeselectAll,
                onDelete = onDelete
            )
        }
    }
}

@Composable
private fun SelectionModeBottomSheetShrunkState(
    modifier: Modifier = Modifier, onExpand: () -> Unit, shrunkStateText: String
) {
    Surface(
        modifier = modifier
            .height(56.dp)
            .clip(CutCornerShape(topStart = 16.dp))
            .clickable(onClick = onExpand), elevation = 8.dp, color = MaterialTheme.colors.primary
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clip(CutCornerShape(topStart = 16.dp))
        ) {
            Icon(
                imageVector = Icons.Sharp.KeyboardArrowLeft,
                contentDescription = "Expand",
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(text = shrunkStateText, modifier = Modifier.padding(end = 8.dp))
        }
    }
}

@Composable
private fun SelectionModeBottomSheetExpandedState(
    modifier: Modifier = Modifier,
    onShrink: () -> Unit,
    buttonsEnabled: Boolean,
    buttonText: String,
    onButtonClick: () -> Unit,
    onSelectAll: () -> Unit,
    onDeselectAll: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = 8.dp,
        color = MaterialTheme.colors.primary
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButtonOnPrimary(
                text = buttonText, onClick = onButtonClick, enabled = buttonsEnabled
            )
            Divider(
                Modifier
                    .width(1.dp)
                    .fillMaxHeight(0.77F)
            )
            IconButton(onClick = onSelectAll) {
                Icon(Icons.Sharp.SelectAll, "Select All")
            }
            IconButton(onClick = onDeselectAll, enabled = buttonsEnabled) {
                Icon(Icons.Sharp.Deselect, "Deselect All")
            }
            IconButton(onClick = onDelete, enabled = buttonsEnabled) {
                Icon(Icons.Sharp.Delete, "Delete")
            }
            Spacer(modifier = Modifier.weight(1F))
            IconButton(onClick = onShrink) {
                Icon(Icons.Sharp.KeyboardArrowRight, "Shrink the Bar")
            }
        }
    }
}