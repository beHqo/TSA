package com.example.android.strikingarts.ui.combo

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.R
import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.domain.model.ComboListItem
import com.example.android.strikingarts.ui.components.SelectionModeBottomSheet
import com.example.android.strikingarts.ui.components.columnitem.TripleLineItemSelectionMode
import com.example.android.strikingarts.ui.components.columnitem.TripleLineItemViewingMode
import com.example.android.strikingarts.ui.parentlayouts.ListScreenLayout
import com.example.android.strikingarts.utils.getTechniqueNumberFromCombo

@Composable
fun ComboScreen(
    model: ComboViewModel = hiltViewModel(),
    navigateToComboDetailsScreen: (id: Long) -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit
) {
    val deleteDialogVisible by model.deleteDialogVisible.collectAsStateWithLifecycle()
    val visibleItems by model.comboList.collectAsStateWithLifecycle()
    val selectedItemsIdList by model.selectedItemsIdList.collectAsStateWithLifecycle()
    val selectionMode by model.selectionMode.collectAsStateWithLifecycle()

    val selectionButtonsEnabled by remember { derivedStateOf { selectedItemsIdList.isNotEmpty() } }

    ComboScreen(
        setSelectionModeValueGlobally = setSelectionModeValueGlobally,
        navigateToComboDetailsScreen = navigateToComboDetailsScreen,
        selectionMode = selectionMode,
        selectedItemsIdList = selectedItemsIdList,
        exitSelectionMode = model::exitSelectionMode,
        onLongPress = model::onLongPress,
        onItemSelectionChange = model::onItemSelectionChange,
        onDeselectItem = model::deselectItem,
        selectAllItems = model::selectAllItems,
        deselectAllItems = model::deselectAllItems,
        setSelectedQuantity = model::setSelectedQuantity,
        deleteDialogVisible = deleteDialogVisible,
        showDeleteDialogAndUpdateId = model::showDeleteDialogAndUpdateId,
        setDeleteDialogVisibility = model::setDeleteDialogVisibility,
        selectionButtonsEnabled = selectionButtonsEnabled,
        visibleCombos = visibleItems,
        deleteItem = model::deleteItem,
        deleteSelectedItems = model::deleteSelectedItems
    )
}

@Composable
private fun ComboScreen(
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    navigateToComboDetailsScreen: (Long) -> Unit,
    selectionMode: Boolean,
    selectedItemsIdList: ImmutableList<Long>,
    exitSelectionMode: () -> Unit,
    onLongPress: (Long) -> Unit,
    onItemSelectionChange: (Long, Boolean) -> Unit,
    onDeselectItem: (Long) -> Unit,
    selectAllItems: () -> Unit,
    deselectAllItems: () -> Unit,
    setSelectedQuantity: (Long, Int) -> Unit,
    deleteDialogVisible: Boolean,
    showDeleteDialogAndUpdateId: (Long) -> Unit,
    setDeleteDialogVisibility: (Boolean) -> Unit,
    selectionButtonsEnabled: Boolean,
    visibleCombos: ImmutableList<ComboListItem>,
    deleteItem: () -> Unit,
    deleteSelectedItems: () -> Unit,
) = ListScreenLayout(selectionMode = selectionMode,
    exitSelectionMode = { setSelectionModeValueGlobally(false); exitSelectionMode() },
    deleteDialogVisible = deleteDialogVisible,
    dismissDeleteDialog = setDeleteDialogVisibility,
    onDeleteItem = deleteItem,
    onDeleteMultipleItems = deleteSelectedItems,
    lazyColumnContent = {
        comboList(
            visibleCombos = visibleCombos,
            selectionMode = selectionMode,
            onSelectionModeChange = setSelectionModeValueGlobally,
            onLongPress = onLongPress,
            selectedItemsIdList = selectedItemsIdList,
            onSelectionChange = onItemSelectionChange,
            onDeselectItem = onDeselectItem,
            setSelectedQuantity = setSelectedQuantity,
            onClick = {}, /* TODO: idk implement some shit */
            onEdit = navigateToComboDetailsScreen,
            onDelete = showDeleteDialogAndUpdateId
        )
    },
    bottomSlot = {
        SelectionModeBottomSheet(
            visible = selectionMode,
            buttonsEnabled = selectionButtonsEnabled,
            previewText = stringResource(
                R.string.all_bottom_selection_bar_selected, selectedItemsIdList.size
            ),
            buttonText = stringResource(R.string.combo_details_add_to_workout),
            onButtonClick = { /*TODO: Navigate to WorkoutDetailsScreen n stuff*/ },
            onSelectAll = selectAllItems,
            onDeselectAll = deselectAllItems,
            onDelete = { setDeleteDialogVisibility(true) },
        )
    })

private fun LazyListScope.comboList(
    visibleCombos: ImmutableList<ComboListItem>,
    selectionMode: Boolean,
    onSelectionModeChange: (Boolean) -> Unit,
    onLongPress: (Long) -> Unit,
    selectedItemsIdList: ImmutableList<Long>,
    onSelectionChange: (Long, Boolean) -> Unit,
    onDeselectItem: (Long) -> Unit,
    setSelectedQuantity: (Long, Int) -> Unit,
    onClick: (id: Long) -> Unit,
    onEdit: (id: Long) -> Unit,
    onDelete: (id: Long) -> Unit
) = if (selectionMode) items(items = visibleCombos,
    key = { it.id },
    contentType = { "SelectionModeComboItem" }) {
    TripleLineItemSelectionMode(
        itemId = it.id,
        primaryText = it.name,
        secondaryText = it.desc,
        tertiaryText = getTechniqueNumberFromCombo(it.techniqueList),
        onModeChange = { id, selectionMode ->
            onSelectionModeChange(selectionMode); onLongPress(id)
        },
        selected = selectedItemsIdList.contains(it.id),
        onSelectionChange = onSelectionChange,
        onDeselectItem = onDeselectItem,
        selectedQuantity = selectedItemsIdList.count { id -> id == it.id },
        setSelectedQuantity = setSelectedQuantity
    )
} else items(items = visibleCombos, key = { it.id }, contentType = { "ViewingModeComboItem" }) {
    TripleLineItemViewingMode(
        itemId = it.id,
        primaryText = it.name,
        secondaryText = it.desc,
        tertiaryText = getTechniqueNumberFromCombo(it.techniqueList),
        onModeChange = { id, selectionMode ->
            onSelectionModeChange(selectionMode); onLongPress(id)
        },
        onClick = onClick,
        onEdit = onEdit,
        onDelete = onDelete
    )
}
