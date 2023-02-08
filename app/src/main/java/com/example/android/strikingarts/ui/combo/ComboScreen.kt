package com.example.android.strikingarts.ui.combo

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.R
import com.example.android.strikingarts.database.entity.ComboWithTechniques
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.ui.components.ProgressBar
import com.example.android.strikingarts.ui.components.SelectionModeBottomSheet
import com.example.android.strikingarts.ui.components.TripleLineItem
import com.example.android.strikingarts.ui.parentlayouts.ListScreenLayout
import com.example.android.strikingarts.utils.ImmutableList
import com.example.android.strikingarts.utils.getTechniqueNumberFromCombo

@Composable
fun ComboScreen(
    model: ComboViewModel = hiltViewModel(),
    navigateToComboDetailsScreen: (id: Long) -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit
) {
    val loadingScreen by model.loadingScreen.collectAsState()

    if (loadingScreen) ProgressBar() else {
        val deleteDialogVisible by model.deleteDialogVisible.collectAsState()
        val visibleItems by model.visibleCombos.collectAsState()
        val selectedItemsIdList by model.selectedItemsIdList.collectAsState()
        val selectionMode by model.selectionMode.collectAsState()

        val selectionButtonsEnabled by remember { derivedStateOf { selectedItemsIdList.isNotEmpty() } }

        ComboScreen(setSelectionModeValueGlobally = setSelectionModeValueGlobally,
            navigateToComboDetailsScreen = navigateToComboDetailsScreen,
            selectionMode = selectionMode,
            selectedItems = selectedItemsIdList,
            exitSelectionMode = model::exitSelectionMode,
            onLongPress = model::onLongPress,
            onItemSelectionChange = model::onItemSelectionChange,
            selectAllItems = model::selectAllItems,
            deselectAllItems = model::deselectAllItems,
            deleteDialogVisible = deleteDialogVisible,
            showDeleteDialogAndUpdateId = model::showDeleteDialogAndUpdateId,
            setDeleteDialogVisibility = model::setDeleteDialogVisibility,
            selectionButtonsEnabled = selectionButtonsEnabled,
            visibleCombos = visibleItems,
            deleteItem = model::deleteItem,
            deleteSelectedItems = model::deleteSelectedItems,
            updateSelectedItemIds = { })
    }
}

@Composable
private fun ComboScreen(
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    navigateToComboDetailsScreen: (Long) -> Unit,
    selectionMode: Boolean,
    selectedItems: ImmutableList<Long>,
    exitSelectionMode: () -> Unit,
    onLongPress: (Long) -> Unit,
    onItemSelectionChange: (Long, Boolean) -> Unit,
    selectAllItems: (ImmutableList<Long>) -> Unit,
    deselectAllItems: () -> Unit,
    deleteDialogVisible: Boolean,
    showDeleteDialogAndUpdateId: (Long) -> Unit,
    setDeleteDialogVisibility: (Boolean) -> Unit,
    selectionButtonsEnabled: Boolean,
    visibleCombos: ImmutableList<ComboWithTechniques>,
    deleteItem: () -> Unit,
    deleteSelectedItems: () -> Unit,
    updateSelectedItemIds: () -> Unit
) {
    ListScreenLayout(selectionMode = selectionMode,
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
                selectedCombos = selectedItems,
                onSelectionChange = onItemSelectionChange,
                onItemClick = {}, /* TODO: idk implement some shit */
                onEdit = navigateToComboDetailsScreen,
                onDelete = showDeleteDialogAndUpdateId
            )
        },
        bottomSlot = {
            SelectionModeBottomSheet(
                modifier = Modifier.align(Alignment.BottomEnd),
                visible = selectionMode,
                buttonsEnabled = selectionButtonsEnabled,
                shrunkStateText = stringResource(
                    R.string.all_bottom_selection_bar_selected, selectedItems.size
                ),
                buttonText = stringResource(R.string.combo_details_add_to_workout),
                onButtonClick = updateSelectedItemIds,
                onSelectAll = {
                    selectAllItems(ImmutableList(visibleCombos.map { it.combo.comboId }))
                },
                onDeselectAll = deselectAllItems,
                onDelete = { setDeleteDialogVisibility(true) },
            )
        })
}

@Composable
private fun ComboItem(
    comboId: Long, // Need to pass this in order to avoid using unstable lambdas for our onClick(s)
    comboName: String,
    comboDesc: String,
    techniqueList: ImmutableList<Technique>,
    selectionMode: Boolean,
    onModeChange: (Long, Boolean) -> Unit,
    selected: Boolean,
    onSelectionChange: (Long, Boolean) -> Unit,
    onItemClick: (id: Long) -> Unit,
    onEdit: (id: Long) -> Unit,
    onDelete: (id: Long) -> Unit
) {
    TripleLineItem(
        itemId = comboId,
        primaryText = comboName,
        secondaryText = comboDesc,
        tertiaryText = getTechniqueNumberFromCombo(techniqueList),
        selectionMode = selectionMode,
        onModeChange = onModeChange,
        selected = selected,
        onSelectionChange = onSelectionChange,
        onClick = onItemClick,
        onEdit = onEdit,
        onDelete = onDelete
    )
}

private fun LazyListScope.comboList(
    visibleCombos: ImmutableList<ComboWithTechniques>,
    selectionMode: Boolean,
    onSelectionModeChange: (Boolean) -> Unit,
    onLongPress: (Long) -> Unit,
    selectedCombos: ImmutableList<Long>,
    onSelectionChange: (Long, Boolean) -> Unit,
    onItemClick: (id: Long) -> Unit,
    onEdit: (id: Long) -> Unit,
    onDelete: (id: Long) -> Unit
) {
    items(visibleCombos, key = { it.combo.comboId }) {
        ComboItem(
            comboId = it.combo.comboId,
            comboName = it.combo.name,
            comboDesc = it.combo.description,
            techniqueList = ImmutableList(it.techniques),
            selectionMode = selectionMode,
            onModeChange = { id, selectionMode ->
                onSelectionModeChange(selectionMode); onLongPress(id)
            },
            selected = selectedCombos.contains(it.combo.comboId),
            onSelectionChange = onSelectionChange,
            onItemClick = onItemClick,
            onEdit = onEdit,
            onDelete = onDelete
        )
    }
}
