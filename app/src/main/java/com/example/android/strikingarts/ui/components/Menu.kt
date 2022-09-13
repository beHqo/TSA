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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.R

@Composable
fun MoreVertDropdownMenu(
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier) {
        MoreVertIconButton(onClick = { expanded = true })

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(onClick = {
                onEdit()
                expanded = false
            }) {
                Icon(Icons.Default.Edit, null, Modifier.size(24.dp))
                Text(stringResource(R.string.all_edit), Modifier.padding(start = 12.dp))
            }
            DropdownMenuItem(onClick = {
                onDelete()
                expanded = false
            }) {
                Icon(Icons.Default.Delete, null, Modifier.size(24.dp))
                Text(stringResource(R.string.all_delete), Modifier.padding(start = 12.dp))
            }
        }
    }
}