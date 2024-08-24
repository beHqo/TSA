package com.example.android.strikingarts.ui.components.listitem

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.android.strikingarts.ui.components.MoreVertDropdownMenu
import com.example.android.strikingarts.ui.components.NumberPicker
import com.example.android.strikingarts.ui.components.PrimaryText
import com.example.android.strikingarts.ui.components.SecondaryText
import com.example.android.strikingarts.ui.components.SelectableHexagonButton
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.SizeManager.ListItemMinHeight

@OptIn(ExperimentalFoundationApi::class) //combinedClickable is experimental
@Composable
fun BaseItemWithMultipleLinesViewingMode(
    itemId: Long,
    primaryText: String,
    onModeChange: (Long, Boolean) -> Unit,
    onClick: (Long) -> Unit,
    onEdit: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
        .heightIn(min = ListItemMinHeight)
        .combinedClickable(
            onClick = { onClick(itemId) },
            onLongClick = { onModeChange(itemId, true) })
        .padding(vertical = PaddingManager.Medium, horizontal = PaddingManager.Large)
) {
    Column(modifier = Modifier.weight(1F)) { PrimaryText(primaryText); content() }

    MoreVertDropdownMenu(onDelete = { onDelete(itemId) }, onEdit = { onEdit(itemId) })
}

@OptIn(ExperimentalFoundationApi::class) //combinedClickable is experimental
@Composable
fun BaseItemWithMultipleLinesSelectionMode(
    itemId: Long,
    primaryText: String,
    onModeChange: (Long, Boolean) -> Unit,
    selected: Boolean,
    onSelectionChange: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
        .heightIn(min = ListItemMinHeight)
        .combinedClickable(onClick = { onSelectionChange(itemId, !selected) },
            onLongClick = { onModeChange(itemId, false) })
        .padding(vertical = PaddingManager.Medium, horizontal = PaddingManager.Large)
) {
    SelectableHexagonButton(
        selected = selected,
        onSelectionChange = { onSelectionChange(itemId, it) },
        modifier = Modifier.padding(end = PaddingManager.Large)
    )

    Column(modifier = Modifier.weight(1F)) { PrimaryText(primaryText); content() }
}

@OptIn(ExperimentalFoundationApi::class) //combinedClickable is experimental
@Composable
fun BaseItemWithMultipleLinesSelectionMode(
    itemId: Long,
    primaryText: String,
    onModeChange: (Long, Boolean) -> Unit,
    selected: Boolean,
    onSelectionChange: (Long, Boolean) -> Unit,
    onDeselectItem: (Long) -> Unit,
    selectedQuantity: Int,
    setSelectedQuantity: (Long, Int) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
        .heightIn(min = ListItemMinHeight)
        .combinedClickable(onClick = { onSelectionChange(itemId, !selected) },
            onLongClick = { onModeChange(itemId, false) })
        .padding(vertical = PaddingManager.Medium, horizontal = PaddingManager.Large)
) {
    SelectionButton(selected, onDeselectItem, itemId, onSelectionChange)

    Column(modifier = Modifier.weight(1F)) { PrimaryText(primaryText); content() }

    NumberPicker(quantity = selectedQuantity, setQuantity = { setSelectedQuantity(itemId, it) })
}

@Composable
fun TripleLineItemViewingMode(
    itemId: Long,
    primaryText: String,
    secondaryText: String,
    tertiaryText: String,
    onModeChange: (Long, Boolean) -> Unit,
    onClick: (Long) -> Unit,
    onEdit: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    modifier: Modifier = Modifier,
) = BaseItemWithMultipleLinesViewingMode(
    modifier = modifier,
    itemId = itemId,
    primaryText = primaryText,
    onModeChange = onModeChange,
    onClick = onClick,
    onEdit = onEdit,
    onDelete = onDelete
) { SecondaryText(secondaryText); SecondaryText(tertiaryText) }

@Composable
fun TripleLineItemSelectionMode(
    itemId: Long,
    primaryText: String,
    secondaryText: String,
    tertiaryText: String,
    onModeChange: (Long, Boolean) -> Unit,
    selected: Boolean,
    onSelectionChange: (Long, Boolean) -> Unit,
    onDeselectItem: (Long) -> Unit,
    selectedQuantity: Int,
    setSelectedQuantity: (Long, Int) -> Unit,
    modifier: Modifier = Modifier
) = BaseItemWithMultipleLinesSelectionMode(
    modifier = modifier,
    itemId = itemId,
    primaryText = primaryText,
    onModeChange = onModeChange,
    selected = selected,
    onSelectionChange = onSelectionChange,
    onDeselectItem = onDeselectItem,
    selectedQuantity = selectedQuantity,
    setSelectedQuantity = setSelectedQuantity
) { SecondaryText(secondaryText); SecondaryText(tertiaryText) }

@Composable
fun SelectionButton(
    selected: Boolean,
    onDeselect: (Long) -> Unit,
    itemId: Long,
    onSelectionChange: (Long, Boolean) -> Unit
) = SelectableHexagonButton(
    selected = selected,
    onSelectionChange = { if (selected) onDeselect(itemId) else onSelectionChange(itemId, it) },
    modifier = Modifier.padding(end = PaddingManager.Large)
)


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