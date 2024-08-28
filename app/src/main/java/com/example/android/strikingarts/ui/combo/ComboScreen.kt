package com.example.android.strikingarts.ui.combo

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.R
import com.example.android.strikingarts.domain.model.Combo
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.ui.components.SelectionModeBottomSheet
import com.example.android.strikingarts.ui.components.listitem.TripleLineItemSelectionMode
import com.example.android.strikingarts.ui.components.listitem.TripleLineItemViewingMode
import com.example.android.strikingarts.ui.compositionlocal.LocalUserPreferences
import com.example.android.strikingarts.ui.mapper.getTechniqueRepresentation
import com.example.android.strikingarts.ui.parentlayouts.ListScreenLayout
import com.example.android.strikingarts.ui.util.toComposeColor
import kotlinx.coroutines.launch

@Composable
fun ComboScreen(
    vm: ComboViewModel = hiltViewModel(),
    showSnackbar: (String) -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    navigateToComboDetails: (id: Long?) -> Unit,
    navigateToWorkoutDetails: () -> Unit
) {
    val deleteDialogVisible by vm.deleteDialogVisible.collectAsStateWithLifecycle()
    val visibleItems by vm.comboList.collectAsStateWithLifecycle()
    val selectedItemsIdList by vm.selectedItemsIdList.collectAsStateWithLifecycle()
    val selectedItemsNames by vm.selectedItemsNames.collectAsStateWithLifecycle()
    val selectionMode by vm.selectionMode.collectAsStateWithLifecycle()
    val currentCombo by vm.currentCombo.collectAsStateWithLifecycle()
    val comboPreviewVisible by vm.comboPreviewDialogVisible.collectAsStateWithLifecycle()
    val currentColor by vm.techniqueColorString.collectAsStateWithLifecycle()

    val productionMode = vm.productionMode

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    ComboPreviewDialog(
        visible = comboPreviewVisible,
        onDismiss = vm::dismissComboPreviewDialog,
        onPlay = vm::playCombo,
        comboName = currentCombo.name,
        comboText = currentCombo.getTechniqueRepresentation(LocalUserPreferences.current.techniqueRepresentationFormat),
        techniqueColor = currentColor.toComposeColor()
    )

    ComboScreen(setSelectionModeValueGlobally = setSelectionModeValueGlobally,
        navigateToComboDetails = navigateToComboDetails,
        navigateToWorkoutDetails = navigateToWorkoutDetails,
        onComboClick = vm::onComboClick,
        productionMode = productionMode,
        selectionMode = selectionMode,
        selectedItemsIdList = selectedItemsIdList,
        selectedItemsNames = selectedItemsNames,
        exitSelectionMode = vm::exitSelectionMode,
        onLongPress = vm::onLongPress,
        onItemSelectionChange = vm::onItemSelectionChange,
        onDeselectItem = vm::deselectItem,
        selectAllItems = vm::selectAllItems,
        deselectAllItems = vm::deselectAllItems,
        setSelectedQuantity = vm::setSelectedQuantity,
        deleteDialogVisible = deleteDialogVisible,
        showDeleteDialogAndUpdateId = vm::showDeleteDialogAndUpdateId,
        setDeleteDialogVisibility = vm::setDeleteDialogVisibility,
        visibleCombos = visibleItems,
        deleteItem = {
            coroutineScope.launch {
                val deleteOperationSuccessful = vm.deleteItem()

                showSnackbar(context.getString(if (deleteOperationSuccessful) R.string.all_snackbar_delete_success_single else R.string.all_snackbar_delete_failed_single))
            }
        },
        deleteSelectedItems = {
            coroutineScope.launch {
                val deleteOperationSuccessful = vm.deleteSelectedItems()

                showSnackbar(context.getString(if (deleteOperationSuccessful) R.string.all_snackbar_delete_success_multiple else R.string.all_snackbar_delete_failed_multiple))
            }
        },
        onFabClick = { navigateToComboDetails(null) })
}

@Composable
private fun ComboScreen(
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    navigateToComboDetails: (Long) -> Unit,
    navigateToWorkoutDetails: () -> Unit,
    onComboClick: (Combo) -> Unit,
    productionMode: Boolean,
    selectionMode: Boolean,
    selectedItemsIdList: ImmutableList<Long>,
    selectedItemsNames: String,
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
    visibleCombos: ImmutableList<Combo>,
    deleteItem: () -> Unit,
    deleteSelectedItems: () -> Unit,
    onFabClick: () -> Unit
) = ListScreenLayout(
    productionMode = productionMode,
    selectionMode = selectionMode,
    exitSelectionMode = { setSelectionModeValueGlobally(false); exitSelectionMode() },
    deleteDialogVisible = deleteDialogVisible,
    dismissDeleteDialog = setDeleteDialogVisibility,
    onDeleteItem = deleteItem,
    onDeleteMultipleItems = deleteSelectedItems,
    onFabClick = onFabClick,
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
            onComboClick = onComboClick,
            onEdit = navigateToComboDetails,
            onDelete = showDeleteDialogAndUpdateId
        )
    },
    bottomSlot = {
        SelectionModeBottomSheet(
            visible = selectionMode,
            buttonsEnabled = true,
            previewText = selectedItemsNames,
            itemsSelectedText = stringResource(
                R.string.all_bottom_selection_bar_selected, selectedItemsIdList.size
            ),
            buttonText = stringResource(R.string.combo_details_add_to_workout),
            onButtonClick = { exitSelectionMode(); navigateToWorkoutDetails() },
            onSelectAll = selectAllItems,
            onDeselectAll = deselectAllItems,
            onDelete = { setDeleteDialogVisibility(true) },
        )
    })

private fun LazyListScope.comboList(
    visibleCombos: ImmutableList<Combo>,
    selectionMode: Boolean,
    onSelectionModeChange: (Boolean) -> Unit,
    onLongPress: (Long) -> Unit,
    selectedItemsIdList: ImmutableList<Long>,
    onSelectionChange: (Long, Boolean) -> Unit,
    onDeselectItem: (Long) -> Unit,
    setSelectedQuantity: (Long, Int) -> Unit,
    onComboClick: (Combo) -> Unit,
    onEdit: (id: Long) -> Unit,
    onDelete: (id: Long) -> Unit
) = if (selectionMode) items(items = visibleCombos,
    key = { it.id },
    contentType = { "SelectionModeComboItem" }) { combo ->
    TripleLineItemSelectionMode(
        itemId = combo.id,
        primaryText = combo.name,
        secondaryText = combo.desc,
        tertiaryText = combo.getTechniqueRepresentation(LocalUserPreferences.current.techniqueRepresentationFormat),
        onModeChange = { id, selectionMode ->
            onSelectionModeChange(selectionMode); onLongPress(id)
        },
        selected = selectedItemsIdList.contains(combo.id),
        onSelectionChange = onSelectionChange,
        onDeselectItem = onDeselectItem,
        selectedQuantity = selectedItemsIdList.count { id -> id == combo.id },
        setSelectedQuantity = setSelectedQuantity
    )
} else items(items = visibleCombos,
    key = { it.id },
    contentType = { "ViewingModeComboItem" }) { combo ->
    TripleLineItemViewingMode(
        itemId = combo.id,
        primaryText = combo.name,
        secondaryText = combo.desc,
        tertiaryText = combo.getTechniqueRepresentation(LocalUserPreferences.current.techniqueRepresentationFormat),
        onModeChange = { id, selectionMode ->
            onSelectionModeChange(selectionMode); onLongPress(id)
        },
        onClick = { onComboClick(combo) },
        onEdit = onEdit,
        onDelete = onDelete
    )
}