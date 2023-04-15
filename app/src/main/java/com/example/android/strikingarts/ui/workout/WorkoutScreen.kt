package com.example.android.strikingarts.ui.workout

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.domain.model.WorkoutListItem
import com.example.android.strikingarts.ui.components.SelectionModeBottomSheet
import com.example.android.strikingarts.ui.components.TripleLineItem
import com.example.android.strikingarts.ui.parentlayouts.ListScreenLayout
import com.example.android.strikingarts.ui.workoutdetails.WorkoutViewModel

@Composable
fun WorkoutScreen(
    model: WorkoutViewModel = hiltViewModel(),
    navigateToWorkoutDetails: (Long) -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit
) {
    val deleteDialogVisible by model.deleteDialogVisible.collectAsStateWithLifecycle()
    val selectionMode by model.selectionMode.collectAsStateWithLifecycle()
    val workoutList by model.workoutList.collectAsStateWithLifecycle()
    val selectedItemsIdList by model.selectedItemsIdList.collectAsStateWithLifecycle()

    val selectionButtonsEnabled by remember { derivedStateOf { selectedItemsIdList.size > 1 } }

    WorkoutScreen(
        navigateToWorkoutDetails = navigateToWorkoutDetails,
        workoutList = workoutList,
        setSelectionModeValueGlobally = setSelectionModeValueGlobally,
        selectedItemsIdList = selectedItemsIdList,
        selectionMode = selectionMode,
        exitSelectionMode = model::exitSelectionMode,
        onLongPress = model::onLongPress,
        onItemSelectionChange = model::onItemSelectionChange,
        selectAllItems = model::selectAllItems,
        deselectAllItems = model::deselectAllItems,
        deleteDialogVisible = deleteDialogVisible,
        showDeleteDialogAndUpdateId = model::showDeleteDialogAndUpdateId,
        setDeleteDialogVisibility = model::setDeleteDialogVisibility,
        deleteItem = model::deleteItem,
        deleteSelectedItems = model::deleteSelectedItems,
        selectionButtonsEnabled = selectionButtonsEnabled,
    )
}

@Composable
private fun WorkoutScreen(
    workoutList: ImmutableList<WorkoutListItem>,
    navigateToWorkoutDetails: (Long) -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    selectedItemsIdList: ImmutableList<Long>,
    selectionMode: Boolean,
    exitSelectionMode: () -> Unit,
    onLongPress: (Long) -> Unit,
    onItemSelectionChange: (Long, Boolean) -> Unit,
    selectAllItems: () -> Unit,
    deselectAllItems: () -> Unit,
    deleteDialogVisible: Boolean,
    showDeleteDialogAndUpdateId: (Long) -> Unit,
    setDeleteDialogVisibility: (Boolean) -> Unit,
    deleteItem: () -> Unit,
    deleteSelectedItems: () -> Unit,
    selectionButtonsEnabled: Boolean,
) = ListScreenLayout(selectionMode = selectionMode,
    exitSelectionMode = { exitSelectionMode(); setSelectionModeValueGlobally(false) },
    deleteDialogVisible = deleteDialogVisible,
    dismissDeleteDialog = setDeleteDialogVisibility,
    onDeleteItem = deleteItem,
    onDeleteMultipleItems = deleteSelectedItems,
    lazyColumnContent = {
        workoutList(
            workoutList = workoutList,
            selectionMode = selectionMode,
            onSelectionModeChange = setSelectionModeValueGlobally,
            onLongPress = onLongPress,
            selectedWorkouts = selectedItemsIdList,
            onSelectionChange = onItemSelectionChange,
            onClick = {},
            navigateToWorkoutDetails = navigateToWorkoutDetails,
            onShowDeleteDialog = showDeleteDialogAndUpdateId
        )
    },
    bottomSlot = {
        SelectionModeBottomSheet(visible = selectionMode,
            buttonsEnabled = selectionButtonsEnabled,
            onSelectAll = selectAllItems,
            onDeselectAll = deselectAllItems,
            onDelete = { setDeleteDialogVisibility(true) })
    })

private fun LazyListScope.workoutList(
    workoutList: ImmutableList<WorkoutListItem>,
    selectionMode: Boolean,
    onSelectionModeChange: (Boolean) -> Unit,
    onLongPress: (Long) -> Unit,
    selectedWorkouts: ImmutableList<Long>,
    onSelectionChange: (Long, Boolean) -> Unit,
    onClick: (Long) -> Unit,
    navigateToWorkoutDetails: (Long) -> Unit,
    onShowDeleteDialog: (Long) -> Unit,
) = items(items = workoutList, key = { it.id }, contentType = { "WorkoutItem" }) {
    TripleLineItem(
        itemId = it.id,
        primaryText = it.name,
        secondaryText = "${it.rounds} Rounds, ${it.roundDurationMilli} with ${it.restDurationMilli} rest",
        tertiaryText = "${it.comboList.size} Total Combos",
        selectionMode = selectionMode,
        onModeChange = { id, selectionMode ->
            onSelectionModeChange(selectionMode); onLongPress(id)
        },
        selected = selectedWorkouts.contains(it.id),
        onSelectionChange = onSelectionChange,
        onClick = onClick,
        onEdit = navigateToWorkoutDetails,
        onDelete = onShowDeleteDialog
    )
}