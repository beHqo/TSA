package com.example.android.strikingarts.ui.workout

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.R
import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.domain.model.WorkoutListItem
import com.example.android.strikingarts.ui.components.SecondaryText
import com.example.android.strikingarts.ui.components.SelectionModeBottomSheet
import com.example.android.strikingarts.ui.components.listitem.BaseItemWithMultipleLinesSelectionMode
import com.example.android.strikingarts.ui.components.listitem.BaseItemWithMultipleLinesViewingMode
import com.example.android.strikingarts.ui.model.toTime
import com.example.android.strikingarts.ui.parentlayouts.ListScreenLayout

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
            onClick = {/* TODO: Make workout do something when it's tapped on! */ },
            onEdit = navigateToWorkoutDetails,
            onDelete = showDeleteDialogAndUpdateId
        )
    },
    bottomSlot = {
        SelectionModeBottomSheet(visible = selectionMode,
            buttonsEnabled = selectionButtonsEnabled,
            onSelectAll = selectAllItems,
            onDeselectAll = deselectAllItems,
            onDelete = { setDeleteDialogVisibility(true) })
    }
)

private fun LazyListScope.workoutList(
    workoutList: ImmutableList<WorkoutListItem>,
    selectionMode: Boolean,
    onSelectionModeChange: (Boolean) -> Unit,
    onLongPress: (Long) -> Unit,
    selectedWorkouts: ImmutableList<Long>,
    onSelectionChange: (Long, Boolean) -> Unit,
    onClick: (Long) -> Unit,
    onEdit: (Long) -> Unit,
    onDelete: (Long) -> Unit,
) = if (selectionMode) items(items = workoutList,
    key = { it.id },
    contentType = { "WorkoutItemSelectionMode" }) {
    BaseItemWithMultipleLinesSelectionMode(itemId = it.id,
        primaryText = it.name,
        onModeChange = { id, selectionMode ->
            onSelectionModeChange(selectionMode); onLongPress(id)
        },
        selected = selectedWorkouts.contains(it.id),
        onSelectionChange = onSelectionChange,
        content = { WorkoutItemContent(it) })
} else items(items = workoutList, key = { it.id }, contentType = { "WorkoutItemViewingMode" }) {
    BaseItemWithMultipleLinesViewingMode(itemId = it.id,
        primaryText = it.name,
        onModeChange = { id, selectionMode ->
            onSelectionModeChange(selectionMode); onLongPress(id)
        },
        onClick = onClick,
        onEdit = onEdit,
        onDelete = onDelete,
        content = { WorkoutItemContent(it) })
}

@Composable
private fun WorkoutItemContent(workout: WorkoutListItem) = Row {
    SecondaryText(
        stringResource(
            R.string.workout_total_rounds_combos, workout.rounds, workout.comboList.size
        ), textAlign = TextAlign.Center, maxLines = 2
    )

    SecondaryText(
        text = "|",
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .align(Alignment.CenterVertically)
    )

    SecondaryText(
        stringResource(R.string.workout_round_time, workout.roundLengthMilli.toTime()),
        textAlign = TextAlign.Center,
        maxLines = 2
    )

    SecondaryText(
        text = "|",
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .align(Alignment.CenterVertically)
    )

    SecondaryText(
        stringResource(R.string.workout_rest_time, workout.restLengthMilli.toTime()),
        textAlign = TextAlign.Center,
        maxLines = 2
    )
}