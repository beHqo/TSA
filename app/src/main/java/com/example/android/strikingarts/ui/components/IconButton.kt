package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.R

@Composable
fun DropdownIcon(expanded: Boolean, modifier: Modifier = Modifier) {
    Icon(
        imageVector = if (expanded)
            Icons.Sharp.KeyboardArrowUp else Icons.Sharp.KeyboardArrowDown,
        contentDescription = null,
        modifier = modifier
    )
}

@Composable
fun MoreVertIconButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(
        modifier = modifier.size(24.dp), onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Sharp.MoreVert, contentDescription = null
        )
    }
}

@Composable
fun EditAndRemoveIconButtons(
    onRemove: () -> Unit, onEdit: () -> Unit, modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Sharp.Delete,
                contentDescription = stringResource(R.string.all_delete)
            )
        }
        IconButton(onClick = onEdit) {
            Icon(
                imageVector = Icons.Sharp.Edit,
                contentDescription = stringResource(R.string.all_edit)
            )
        }
    }
}