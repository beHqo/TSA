package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Delete
import androidx.compose.material.icons.sharp.Edit
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.material.icons.sharp.MoreVert
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.R

@Composable
fun DropdownIcon(expanded: Boolean, modifier: Modifier = Modifier) {
    Icon(
        imageVector = if (expanded) Icons.Sharp.KeyboardArrowUp else Icons.Sharp.KeyboardArrowDown,
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

@Composable
fun CountingIconButton(
    imageVector: ImageVector,
    contentDescription: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(enabled = enabled, onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(32.dp)
                .clip(CutCornerShape(8.dp))
                .background(MaterialTheme.colors.primarySurface.copy(alpha = ContentAlpha.medium))
        )
    }
}
