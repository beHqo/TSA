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

@OptIn(ExperimentalFoundationApi::class) //combinedClickable is experimental
@Composable
fun DoubleLineItemWithImage(
    itemId: Long, // Needed for now to pass onClick parameters as reference
    primaryText: String,
    secondaryText: String,
    @DrawableRes imageId: Int,
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
            painter = painterResource(imageId),
            contentDescription = secondaryText,
            modifier = Modifier
                .padding(end = 16.dp)
                .height(56.dp)
        )
        Column(
            verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1F)
        ) {
            PrimaryText(primaryText); SecondaryText(secondaryText)
        }
        MoreVertOrCheckbox(selected = selected,
            onSelectionChange = { onSelectionChange(itemId, !selected) },
            selectionMode = selectionMode,
            onDelete = { onDelete(itemId) },
            onEdit = { onEdit(itemId) })
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
            .combinedClickable(onClick = {
                if (selectionMode) onSelectionChange(itemId, !selected) else onClick(itemId)
            }, onLongClick = {
                onSelectionChange(itemId, !selected); onModeChange(itemId, !selectionMode)
            })
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1F)
        ) {
            PrimaryText(primaryText); SecondaryText(secondaryText); TertiaryText(tertiaryText)
        }
        MoreVertOrCheckbox(selected = selected,
            onSelectionChange = { onSelectionChange(itemId, !selected) },
            selectionMode = selectionMode,
            onDelete = { onDelete(itemId) },
            onEdit = { onEdit(itemId) })
    }
}

@Composable
private fun MoreVertOrCheckbox(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onSelectionChange: (Boolean) -> Unit,
    selectionMode: Boolean,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    FadingAnimatedContent(modifier = modifier,
        targetState = selectionMode,
        currentStateComponent = {
            MoreVertDropdownMenu(onDelete, onEdit, Modifier.padding(end = 8.dp))
        },
        targetStateComponent = { Checkbox(selected, onSelectionChange) })
}

@Composable
private fun MoreVertOrNumberPicker(
    modifier: Modifier = Modifier,
    quantity: Int,
    setQuantity: (Int) -> Unit,
    onDeselect: () -> Unit,
    selectionMode: Boolean,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    FadingAnimatedContent(modifier = modifier,
        targetState = selectionMode,
        currentStateComponent = {
            MoreVertDropdownMenu(onDelete, onEdit, Modifier.padding(end = 8.dp))
        },
        targetStateComponent = {
            NumberPicker(quantity, setQuantity, onDeselect)
        })
}

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
//        .clickable { onItemClick() }
//        .padding(top = 12.dp, bottom = 12.dp, start = 16.dp, end = 16.dp)) {
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