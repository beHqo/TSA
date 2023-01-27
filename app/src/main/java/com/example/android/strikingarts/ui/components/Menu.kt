package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Delete
import androidx.compose.material.icons.sharp.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.R

@Composable
fun MoreVertDropdownMenu(
    onDelete: () -> Unit, onEdit: () -> Unit, modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(modifier) {
        MoreVertIconButton(onClick = { expanded = true })

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(onClick = {
                expanded = false
                onEdit()
            }) {
                Icon(Icons.Sharp.Edit, null, Modifier.size(24.dp))
                Text(stringResource(R.string.all_edit), Modifier.padding(start = 12.dp))
            }
            DropdownMenuItem(onClick = {
                expanded = false
                onDelete()
            }) {
                Icon(Icons.Sharp.Delete, null, Modifier.size(24.dp))
                Text(stringResource(R.string.all_delete), Modifier.padding(start = 12.dp))
            }
        }
    }
}