package com.thestrikingarts.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.thestrikingarts.R

@Composable
fun AddNewItemFab(modifier: Modifier = Modifier, onClick: () -> Unit) =
    FloatingActionButton(modifier = modifier, onClick = onClick) {
        Icon(Icons.Rounded.Add, stringResource(R.string.all_fab_desc))
    }