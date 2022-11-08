package com.example.android.strikingarts.ui.combo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.R
import com.example.android.strikingarts.database.entity.ComboWithTechniques
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.ui.components.ConfirmDialog
import com.example.android.strikingarts.ui.components.SelectionModeBottomSheet
import com.example.android.strikingarts.ui.components.TripleLineItem
import com.example.android.strikingarts.utils.ImmutableList
import com.example.android.strikingarts.utils.getTechniqueNumberFromCombo

@Composable
fun ComboScreen(
    model: ComboViewModel = hiltViewModel(),
    navigateToComboDetailsScreen: (id: Long) -> Unit,
    onSelectionModeChange: (Boolean) -> Unit
) {
    val state = model.uiState.collectAsState()

    BackHandler(enabled = state.value.selectionMode) {
        model.exitSelectionMode(); onSelectionModeChange(false)
    }

    if (state.value.showDeleteDialog) ConfirmDialog(
        titleId = stringResource(R.string.all_delete),
        textId = if (state.value.selectionMode) stringResource(R.string.combo_delete_multiple)
        else stringResource(R.string.combo_dialog_delete),
        confirmButtonText = stringResource(R.string.all_delete),
        dismissButtonText = stringResource(R.string.all_cancel),
        onConfirm = {
            if (state.value.selectionMode) model.deleteSelectedItems()
            else model.deleteItem()
        },
        onDismiss = model::hideDeleteDialog
    )

    Box(modifier = Modifier.fillMaxSize()) {
        ComboList(
            visibleCombos = ImmutableList(state.value.visibleCombos),
            selectionMode = state.value.selectionMode,
            onSelectionModeChange = onSelectionModeChange,
            onLongPress = model::onLongPress,
            selectedCombos = ImmutableList(state.value.selectedItems),
            onSelectionChange = model::onItemSelectionChange,
            onItemClick = {}, //TODO: Play the preview of the combo
            onEdit = navigateToComboDetailsScreen,
            onDelete = model::showDeleteDialogAndUpdateId
        )

        SelectionModeBottomSheet(
            modifier = Modifier.align(Alignment.BottomEnd),
            visible = state.value.selectionMode,
            shrunkStateText = "${state.value.numberOfSelectedItems} Selected",
            buttonsEnabled = state.value.selectedItems.isNotEmpty(),
            buttonText = stringResource(R.string.technique_create_combo),
            onButtonClick = { /*TODO: Add combos to the workout and navigate to WorkoutDetails*/ },
            onSelectAll = model::selectAllItems,
            onDeselectAll = model::deselectAllItems,
            onDelete = model::showDeleteDialog
        )
    }
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

@Composable
private fun ComboList(
    modifier: Modifier = Modifier,
    visibleCombos: ImmutableList<ComboWithTechniques>,
    selectionMode: Boolean,
    onSelectionModeChange: (Boolean) -> Unit,
    onLongPress: (Long, Boolean) -> Unit,
    selectedCombos: ImmutableList<Long>,
    onSelectionChange: (Long, Boolean) -> Unit,
    onItemClick: (id: Long) -> Unit,
    onEdit: (id: Long) -> Unit,
    onDelete: (id: Long) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(visibleCombos, key = { it.combo.comboId }) {
            ComboItem(
                comboId = it.combo.comboId,
                comboName = it.combo.name,
                comboDesc = it.combo.description,
                techniqueList = ImmutableList(it.techniques),
                selectionMode = selectionMode,
                onModeChange = { id, selectionMode ->
                    onSelectionModeChange(selectionMode); onLongPress(id, selectionMode)
                },
                selected = selectedCombos.contains(it.combo.comboId),
                onSelectionChange = onSelectionChange,
                onItemClick = onItemClick,
                onEdit = onEdit,
                onDelete = onDelete
            )
        }
    }
}