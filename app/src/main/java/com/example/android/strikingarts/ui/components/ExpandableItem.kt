package com.example.android.strikingarts.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableListItem(
    @StringRes primaryTextId: Int,
    @StringRes secondaryTextId: Int,
    @StringRes tertiaryTextId: Int,
    onRemove: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
        ) {
            Text(
                text = stringResource(primaryTextId),
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                modifier = if (expanded) Modifier.padding(bottom = 8.dp) else Modifier
            )
            DropdownIcon(
                expanded,
                Modifier
                    .weight(1f, false)
                    .offset(x = 4.dp)
            )
        }

        AnimatedVisibility(visible = expanded) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.8F)
                ) {
                    Text(
                        text = stringResource(secondaryTextId),
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = stringResource(tertiaryTextId),
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onBackground.copy(0.5f),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                EditAndRemoveIconButtons(
                    onRemove = onRemove,
                    onEdit = onEdit,
                    modifier = Modifier
                        .weight(1f, false)
                        .offset(x = 16.dp)
                )
            }
        }
    }
}

