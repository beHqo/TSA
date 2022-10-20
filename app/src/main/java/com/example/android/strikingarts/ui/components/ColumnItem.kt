package com.example.android.strikingarts.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

//@Composable
//fun SingleLineItem(
//    primaryText: String,
//    onItemClick: () -> Unit,
//    onEdit: () -> Unit,
//    onDelete: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Row(modifier = modifier
//        .heightIn(min = 48.dp)
//        .padding(top = 12.dp, bottom = 12.dp, start = 16.dp, end = 16.dp)
//        .clickable { onItemClick() }) {
//        PrimaryText(primaryText, Modifier.weight(1F))
//        MoreVertDropdownMenu(onDelete = onDelete, onEdit = onEdit)
//    }
//
//}
//
//@Composable
//fun DoubleLineItem(
//    primaryText: String,
//    secondaryText: String,
//    onItemClick: () -> Unit,
//    onEdit: () -> Unit,
//    onDelete: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Row(verticalAlignment = Alignment.CenterVertically,
//        modifier = modifier
//            .heightIn(min = 64.dp)
//            .padding(vertical = 8.dp, horizontal = 16.dp)
//            .clickable { onItemClick() }) {
//        Column(
//            verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1F)
//        ) {
//            PrimaryText(primaryText)
//            SecondaryText(secondaryText)
//        }
//        MoreVertDropdownMenu(onDelete = onDelete, onEdit = onEdit)
//    }
//}

@OptIn(ExperimentalFoundationApi::class) //combinedClickable is experimental
@Composable
fun DoubleLineItemWithImage(
    itemId: Long, // Needed for now to pass onClick parameters as reference
    primaryText: String,
    secondaryText: String,
    @DrawableRes image: Int,
    selectionMode: Boolean,
    onModeChange: (Long, Boolean) -> Unit,
    selected: Boolean,
    onSelectionChange: (Long, Boolean) -> Unit,
    onClick: (Long) -> Unit,
    onEdit: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .heightIn(min = 72.dp)
            .combinedClickable(onClick = {
                if (selectionMode) onSelectionChange(itemId, !selected) else onClick(itemId)
            }, onLongClick = {
                onSelectionChange(itemId, !selected); onModeChange(itemId, !selectionMode)
            })
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 16.dp)
                .height(56.dp)
        )
        Column(
            verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1F)
        ) {
            PrimaryText(primaryText)
            SecondaryText(secondaryText)
        }
        if (!selectionMode)
            MoreVertDropdownMenu(onDelete = { onDelete(itemId) }, onEdit = { onEdit(itemId) })
        if (selectionMode)
            Checkbox(checked = selected, onCheckedChange = { onSelectionChange(itemId, !selected) })
    }
}

@OptIn(ExperimentalFoundationApi::class) //combinedClickable is experimental
@Composable
fun TripleLineItem(
    itemId: Long, // Needed for now to pass onClick parameters as reference
    primaryText: String,
    secondaryText: String,
    tertiaryText: String,
    selectionMode: Boolean,
    onModeChange: (Long, Boolean) -> Unit,
    selected: Boolean,
    onSelectionChange: (Long, Boolean) -> Unit,
    onClick: (Long) -> Unit,
    onEdit: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .heightIn(min = 88.dp)
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .combinedClickable(onClick = {
                if (selectionMode) onSelectionChange(itemId, !selected) else onClick(itemId)
            }, onLongClick = {
                onSelectionChange(itemId, !selected); onModeChange(itemId, !selectionMode)
            })
    ) {
        Column(
            verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1F)
        ) {
            PrimaryText(primaryText)
            SecondaryText(secondaryText)
            TertiaryText(tertiaryText)
        }
        if (!selectionMode)
            MoreVertDropdownMenu(onDelete = { onDelete(itemId) }, onEdit = { onEdit(itemId) })
        if (selectionMode)
            Checkbox(checked = selected, onCheckedChange = { onSelectionChange(itemId, !selected) })
    }
}

@Composable
private fun PrimaryText(primaryText: String, modifier: Modifier = Modifier) {
    Text(
        text = primaryText,
        color = MaterialTheme.colors.onSurface,
        style = MaterialTheme.typography.subtitle1,
        maxLines = 1,
        modifier = modifier
    )
}

@Composable
private fun SecondaryText(secondaryText: String, modifier: Modifier = Modifier) {
    Text(
        text = secondaryText,
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
        modifier = modifier
    )
}

@Composable
private fun TertiaryText(tertiaryText: String, modifier: Modifier = Modifier) =
    SecondaryText(limitTextByMaxChars(tertiaryText, TEXTFIELD_DESC_MAX_CHARS), modifier)

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