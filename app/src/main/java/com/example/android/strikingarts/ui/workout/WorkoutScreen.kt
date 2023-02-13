package com.example.android.strikingarts.ui.workout

import android.util.Log
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.database.entity.WorkoutWithCombos
import com.example.android.strikingarts.ui.components.ProgressBar
import com.example.android.strikingarts.ui.components.TripleLineItem
import com.example.android.strikingarts.ui.parentlayouts.ListScreenLayout
import com.example.android.strikingarts.ui.workoutdetails.WorkoutViewModel
import com.example.android.strikingarts.utils.ImmutableList

private const val TAG = "WorkoutScreen"

@Composable
fun WorkoutScreen(
    model: WorkoutViewModel = hiltViewModel(),
    navigateToWorkoutDetails: (Long) -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit
) {
    val loadingScreen by model.loadingScreen.collectAsState()

    if (loadingScreen) ProgressBar() else {
        val deleteDialogVisible by model.deleteDialogVisible.collectAsState()
        val selectionMode by model.selectionMode.collectAsState()
        val visibleWorkouts by model.visibleWorkouts.collectAsState()
        val selectedItemIdList by model.selectedItemsIdList.collectAsState()

        visibleWorkouts.forEach { workoutWithCombos ->
            Log.d(
                TAG,
                "WorkoutScreen: ${workoutWithCombos.workout.name} = ${workoutWithCombos.combos}"
            )
        }

        ListScreenLayout(selectionMode = selectionMode,
            exitSelectionMode = model::exitSelectionMode,
            deleteDialogVisible = deleteDialogVisible,
            dismissDeleteDialog = model::setDeleteDialogVisibility,
            onDeleteItem = { /*TODO*/ },
            onDeleteMultipleItems = { /*TODO*/ },
            lazyColumnContent = {
                workoutList(
                    visibleWorkouts = visibleWorkouts,
                    selectionMode = selectionMode,
                    onSelectionModeChange = setSelectionModeValueGlobally,
                    onLongPress = model::onLongPress,
                    selectedWorkouts = selectedItemIdList,
                    onSelectionChange = model::onItemSelectionChange,
                    onClick = {},
                    navigateToWorkoutDetails = navigateToWorkoutDetails,
                    onShowDeleteDialog = model::showDeleteDialogAndUpdateId
                )
            },
            bottomSlot = { })
    }
}

@Composable
private fun WorkoutItem(
    itemId: Long,
    name: String,
    numberOfRounds: String,
    roundDuration: String,
    restDuration: String,
    numberOfCombos: Int,
    selectionMode: Boolean,
    onModeChange: (Long, Boolean) -> Unit,
    selected: Boolean,
    onSelectionChange: (Long, Boolean) -> Unit,
    onClick: (Long) -> Unit,
    onEdit: (Long) -> Unit,
    onDelete: (Long) -> Unit
) {
    TripleLineItem(itemId = itemId,
        primaryText = name,
        secondaryText = "$numberOfRounds Rounds, $roundDuration with $restDuration rest",
        tertiaryText = "$numberOfCombos Total Combos",
        selectionMode = selectionMode,
        onModeChange = onModeChange,
        selected = selected,
        onSelectionChange = onSelectionChange,
        onClick = onClick,
        onEdit = { onEdit(itemId) },
        onDelete = { onDelete(itemId) })
}

private fun LazyListScope.workoutList(
    visibleWorkouts: ImmutableList<WorkoutWithCombos>,
    selectionMode: Boolean,
    onSelectionModeChange: (Boolean) -> Unit,
    onLongPress: (Long) -> Unit,
    selectedWorkouts: ImmutableList<Long>,
    onSelectionChange: (Long, Boolean) -> Unit,
    onClick: (Long) -> Unit,
    navigateToWorkoutDetails: (Long) -> Unit,
    onShowDeleteDialog: (Long) -> Unit,
) {
    items(items = visibleWorkouts,
        key = { it.workout.workoutId },
        contentType = { "workoutItem" }) {
        WorkoutItem(
            itemId = it.workout.workoutId,
            name = it.workout.name,
            numberOfRounds = it.workout.numberOfRounds.toString(),
            roundDuration = it.workout.roundsDurationMilli.toString(),
            restDuration = it.workout.restsDurationMilli.toString(),
            numberOfCombos = it.combos.size,
            selectionMode = selectionMode,
            onModeChange = { id, selectionMode ->
                onSelectionModeChange(selectionMode); onLongPress(id)
            },
            selected = selectedWorkouts.contains(it.workout.workoutId),
            onSelectionChange = onSelectionChange,
            onClick = onClick,
            onEdit = navigateToWorkoutDetails,
            onDelete = onShowDeleteDialog
        )
    }
}
