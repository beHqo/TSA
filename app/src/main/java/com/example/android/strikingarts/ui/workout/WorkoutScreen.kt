package com.example.android.strikingarts.ui.workout

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

    ListScreenLayout(selectionMode = selectionMode,
        exitSelectionMode = model::exitSelectionMode,
        deleteDialogVisible = deleteDialogVisible,
        dismissDeleteDialog = model::setDeleteDialogVisibility,
        onDeleteItem = model::deleteItem,
        onDeleteMultipleItems = model::deleteSelectedItems,
        lazyColumnContent = {
            workoutList(
                workoutList = workoutList,
                selectionMode = selectionMode,
                onSelectionModeChange = setSelectionModeValueGlobally,
                onLongPress = model::onLongPress,
                selectedWorkouts = selectedItemsIdList,
                onSelectionChange = model::onItemSelectionChange,
                onClick = {},
                navigateToWorkoutDetails = navigateToWorkoutDetails,
                onShowDeleteDialog = model::showDeleteDialogAndUpdateId
            )
        },
        bottomSlot = {
            SelectionModeBottomSheet(visible = selectionMode,
                previewText = stringResource(
                    R.string.all_bottom_selection_bar_selected, selectedItemsIdList.size
                ),
                buttonsEnabled = selectionButtonsEnabled,
                buttonText = stringResource(R.string.technique_create_combo),
                onButtonClick = { model.exitSelectionMode(); /*TODO: What should happen when pressing this button?*/ },
                onSelectAll = model::selectAllItems,
                onDeselectAll = model::deselectAllItems,
                onDelete = { model.setDeleteDialogVisibility(true) })
        })
}

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