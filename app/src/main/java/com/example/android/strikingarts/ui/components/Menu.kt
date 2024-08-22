package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager

@Composable
fun MoreVertDropdownMenu(
    onDelete: () -> Unit, onEdit: () -> Unit, modifier: Modifier = Modifier
) = Box(modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    MoreVertIconButton(onClick = { expanded = true })

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        DropdownMenuItem(onClick = {
            expanded = false
            onEdit()
        }, text = {
            Text(stringResource(R.string.all_edit), Modifier.padding(start = PaddingManager.Medium))
        }, leadingIcon = {
            Icon(Icons.Rounded.Edit, null, Modifier.size(24.dp))
        })
        DropdownMenuItem(onClick = {
            expanded = false
            onDelete()
        }, text = {
            Text(
                stringResource(R.string.all_delete),
                Modifier.padding(start = PaddingManager.Medium)
            )
        }, leadingIcon = {
            Icon(Icons.Rounded.Delete, null, Modifier.size(24.dp))
        })
    }
}