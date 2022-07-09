package com.example.android.strikingarts.components

import androidx.compose.foundation.layout.offset
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class IconButtons {
}

@Composable
fun DropdownIcon(expanded: Boolean) {
    if (expanded)
        Icon(imageVector = Icons.Filled.KeyboardArrowUp, contentDescription = null)
    else
        Icon(imageVector = Icons.Filled.KeyboardArrowDown, contentDescription = null)
//    Icon(imageVector =
//    if (expanded) Icons.Filled.KeyboardArrowUp
//    else Icons.Filled.KeyboardArrowDown,
//        contentDescription = null)
}

@Composable
fun MoreVertIconButton(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier.offset(x = 16.dp),
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = null
        )
    }
}