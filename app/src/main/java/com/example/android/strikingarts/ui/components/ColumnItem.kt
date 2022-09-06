package com.example.android.strikingarts.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.theme.Crimson
import com.example.android.strikingarts.ui.theme.StrikingArtsTheme

@Composable
fun SingleLineItem(
    primaryText: String,
    onItemClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .heightIn(min = 48.dp)
            .padding(top = 12.dp, bottom = 12.dp, start = 16.dp, end = 16.dp)
            .clickable { onItemClick() }
    ) {
        PrimaryText(primaryText, Modifier.weight(1F))
        MoreVertDropdownMenu(onDelete = onDelete, onEdit = onEdit)
    }

}

@Composable
fun DoubleLineItem(
    primaryText: String,
    secondaryText: String,
    onItemClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .heightIn(min = 64.dp)
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable { onItemClick() }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1F)
        ) {
            PrimaryText(primaryText)
            SecondaryText(secondaryText)
        }
        MoreVertDropdownMenu(onDelete = onDelete, onEdit = onEdit)
    }
}

@Composable
fun DoubleLineItemWithImage(
    primaryText: String,
    secondaryText: String,
    @DrawableRes image: Int,
    imageContentDescription: String,
    onItemClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .heightIn(min = 72.dp)
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable { onItemClick() }
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = imageContentDescription,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(end = 16.dp)
                .height(56.dp)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1F)
        ) {
            PrimaryText(primaryText)
            SecondaryText(secondaryText)
        }
        MoreVertDropdownMenu(onDelete = onDelete, onEdit = onEdit)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DoubleLineItemWithImage(
    primaryText: String,
    secondaryText: String,
    @DrawableRes image: Int,
    selectableMode: Boolean,
    onModeChange: () -> Unit,
    selected: Boolean,
    onSelectionChange: () -> Unit,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(color = if (selected) Crimson else MaterialTheme.colors.surface)
            .heightIn(min = 72.dp)
            .combinedClickable (
                onClick = { if (selectableMode) onSelectionChange() else onClick() },
                onLongClick = { onModeChange() }
                )
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Image(
            painter = if (!selected)
                painterResource(image) else painterResource(R.drawable.ic_baseline_check_56),
            contentDescription = null,
            colorFilter = if (selected) ColorFilter.tint(MaterialTheme.colors.onPrimary) else null,
            modifier = Modifier
                .padding(end = 16.dp)
                .height(56.dp)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1F)
        ) {
            PrimaryText(primaryText, selected = selected)
            SecondaryText(secondaryText, selected = selected)
        }
        if (!selected) MoreVertDropdownMenu(onDelete = onDelete, onEdit = onEdit)
    }
}

@Preview()
@Composable
fun SelectableItem() {
    var selected by remember { mutableStateOf(false) }
    var selectableMode by remember { mutableStateOf(false) }

    StrikingArtsTheme {
        Surface {
            DoubleLineItemWithImage(
                primaryText = "Uppercut",
                secondaryText = "PUNCH",
                image = R.drawable.punch_color,
                selectableMode = selectableMode,
                onModeChange = { selectableMode = !selectableMode },
                selected = selected,
                onSelectionChange = { selected = !selected },
                onClick = {},
                onDelete = {},
                onEdit = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun TripleLineItem(
    primaryText: String,
    secondaryText: String,
    tertiaryText: String,
    onItemClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .heightIn(min = 88.dp)
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .clickable { onItemClick() }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1F)
        ) {
            PrimaryText(primaryText)
            SecondaryText(secondaryText)
            TertiaryText(tertiaryText)
        }
        MoreVertDropdownMenu(onDelete = onDelete, onEdit = onEdit)
    }
}

@Composable
private fun PrimaryText(
    primaryText: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false
) {
    Text(
        text = primaryText,
        color = if (selected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
        style = MaterialTheme.typography.subtitle1,
        maxLines = 1,
        modifier = modifier
    )
}

@Composable
private fun SecondaryText(
    secondaryText: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false
) {
    Text(
        text = secondaryText,
        style = MaterialTheme.typography.caption,
        color = if (selected)
            MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface.copy(alpha = 0.5F),
        maxLines = 1,
        modifier = modifier
    )
}

@Composable
private fun TertiaryText(
    tertiaryText: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false
) =
    SecondaryText(limitTextByMaxChars(tertiaryText, TEXTFIELD_DESC_MAX_CHARS), modifier, selected)

private fun limitTextByMaxChars(text: String, maxChars: Int): String =
    if (text.length < maxChars) text else (text.substring(0..maxChars) + "...")


// Needs more work
//@Composable
//fun ExpandableListItem(
//    primaryText: String,
//    secondaryText: String,
//    tertiaryText: String,
//    onRemove: () -> Unit,
//    onEdit: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    var expanded by rememberSaveable { mutableStateOf(false) }
//
//    Column(modifier = modifier) {
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable { expanded = !expanded }
//        ) {
//            Text(
//                text = primaryText,
//                style = MaterialTheme.typography.subtitle1,
//                maxLines = 1,
//                modifier = if (expanded) Modifier.padding(bottom = 8.dp) else Modifier
//            )
//            DropdownIcon(
//                expanded,
//                Modifier
//                    .weight(1f, false)
//                    .offset(x = 4.dp)
//            )
//        }
//
//        AnimatedVisibility(visible = expanded) {
//            Row(
//                horizontalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Column(
//                    modifier = Modifier.fillMaxWidth(0.8F)
//                ) {
//                    Text(
//                        text = secondaryText,
//                        style = MaterialTheme.typography.subtitle1,
//                        color = MaterialTheme.colors.onBackground,
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    )
//                    Text(
//                        text = tertiaryText,
//                        style = MaterialTheme.typography.subtitle1,
//                        color = MaterialTheme.colors.onBackground.copy(0.5f),
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    )
//                }
//                EditAndRemoveIconButtons(
//                    onRemove = onRemove,
//                    onEdit = onEdit,
//                    modifier = Modifier
//                        .weight(1f, false)
//                        .offset(x = 16.dp)
//                )
//            }
//        }
//    }
//}