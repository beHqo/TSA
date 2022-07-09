package com.example.android.strikingarts.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.R

@Composable
fun ExpandableListItem(primaryText: String, vararg expandedText: String) {

    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
        ) {
            Text(
                text = primaryText,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1
            )
            DropdownIcon(expanded)
        }

        AnimatedVisibility(
            visible = expanded
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    expandedText.forEachIndexed { index, text ->
                        Text(
                            text = text,
                            style = MaterialTheme.typography.subtitle1,
                            color = if (index == expandedText.lastIndex)
                                MaterialTheme.colors.onBackground.copy(0.5f) else
                                    MaterialTheme.colors.onBackground,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(x = 16.dp)
                ) {

                    IconButton(
                        onClick = { /*TODO*/ },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.Delete)
                        )
                    }
                    IconButton(
                        onClick = { /*TODO*/ },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = stringResource(R.string.edit)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewExpandable() {
    ExpandableListItem(
        "The Mike Tyson Combo",
        "Done in his fight with RJJ",
        "1 2 Slip Right 4 Body 5"
    )
}

