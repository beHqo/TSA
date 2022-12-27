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
import com.example.android.strikingarts.ui.components.ListScreenLayout
import com.example.android.strikingarts.ui.components.SelectionModeBottomSheet
import com.example.android.strikingarts.ui.components.TripleLineItem
import com.example.android.strikingarts.utils.ImmutableList
import com.example.android.strikingarts.utils.getTechniqueNumberFromCombo

@Composable
fun ComboScreen(
    model: ComboViewModel = hiltViewModel(),
    navigateToComboDetailsScreen: (id: Long) -> Unit,
    notifyBottomNavBarOnSelectionMode: (Boolean) -> Unit
) {
    val state by model.uiState.collectAsState()

    ListScreenLayout(selectionMode = state.selectionMode,
        exitSelectionMode = {
            notifyBottomNavBarOnSelectionMode(false); model.selectionActions.exitSelectionMode()
        },
        showDeleteDialog = state.showDeleteDialog,
        dismissDeleteDialog = model.deleteDialogActions::hideDeleteDialog,
        onDeleteItem = model::deleteItem,
        onDeleteMultipleItems = model::deleteSelectedItems,
        lazyColumnContent = {
            comboList(visibleCombos = ImmutableList(state.visibleCombos),
                selectionMode = state.selectionMode,
                onSelectionModeChange = notifyBottomNavBarOnSelectionMode,
                onLongPress = model.selectionActions::onLongPress,
                selectedCombos = ImmutableList(state.selectedItems),
                onSelectionChange = model.selectionActions::onItemSelectionChange,
                onItemClick = {}, /* TODO: idk implement some shit */
                onEdit = navigateToComboDetailsScreen,
                onDelete = model.deleteDialogActions::showDeleteDialogAndUpdateId)
        },
        BottomSlot = {
            val buttonsEnabled by remember { derivedStateOf { state.selectedItems.isNotEmpty() } }

            SelectionModeBottomSheet(
                modifier = Modifier.align(Alignment.BottomEnd),
                visible = state.selectionMode,
                buttonsEnabled = buttonsEnabled,
                shrunkStateText = stringResource(R.string.all_selected, state.selectedItems.size),
                buttonText = stringResource(R.string.combo_details_add_to_workout),
                onButtonClick = { /*TODO: Add selected item ids to repo and navigate away */ },
                onSelectAll = {
                    model.selectionActions.selectAllItems(
                        ImmutableList(state.visibleCombos.map { it.combo.comboId })
                    )
                },
                onDeselectAll = model.selectionActions::deselectAllItems,
                onDelete = model.deleteDialogActions::showDeleteDialog,
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
//    LazyColumn(modifier = modifier.fillMaxSize()) {
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
//    }
}
