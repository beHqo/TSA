package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.R

@Composable
fun MoreVertDropdownMenu(
    expanded: Boolean,
    onExpand: () -> Unit,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        MoreVertIconButton(onClick = onExpand)

        DropdownMenu(expanded, onDismiss) {
            DropdownMenuItem(onEdit) {
                Icon(Icons.Default.Edit, null, Modifier.size(24.dp))
                Text(stringResource(R.string.all_edit), Modifier.padding(start = 12.dp))
            }
            DropdownMenuItem(onDelete) {
                Icon(Icons.Default.Delete, null, Modifier.size(24.dp))
                Text(stringResource(R.string.all_delete), Modifier.padding(start = 12.dp))
            }
        }
    }
}