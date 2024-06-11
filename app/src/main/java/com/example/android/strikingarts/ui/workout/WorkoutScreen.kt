package com.example.android.strikingarts.ui.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.R
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.Workout
import com.example.android.strikingarts.ui.components.SecondaryText
import com.example.android.strikingarts.ui.components.SelectionModeBottomSheet
import com.example.android.strikingarts.ui.components.listitem.BaseItemWithMultipleLinesSelectionMode
import com.example.android.strikingarts.ui.components.listitem.BaseItemWithMultipleLinesViewingMode
import com.example.android.strikingarts.ui.components.util.SurviveProcessDeath
import com.example.android.strikingarts.ui.model.toTime
import com.example.android.strikingarts.ui.parentlayouts.ListScreenLayout
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager

@Composable
fun WorkoutScreen(
    model: WorkoutViewModel = hiltViewModel(),
    navigateToWorkoutDetails: (Long?) -> Unit,
    navigateToWorkoutPreviewScreen: (id: Long) -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit,
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
        onWorkoutItemClick = navigateToWorkoutPreviewScreen,
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
        onFabClick = { navigateToWorkoutDetails(null) },
        selectionButtonsEnabled = selectionButtonsEnabled,
    )

    SurviveProcessDeath(onStop = model::surviveProcessDeath)
}

@Composable
private fun WorkoutScreen(
    workoutList: ImmutableList<Workout>,
    navigateToWorkoutDetails: (Long) -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    onWorkoutItemClick: (Long) -> Unit,
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
    onFabClick: () -> Unit,
    selectionButtonsEnabled: Boolean,
) = ListScreenLayout(productionMode = false,
    selectionMode = selectionMode,
    exitSelectionMode = { exitSelectionMode(); setSelectionModeValueGlobally(false) },
    deleteDialogVisible = deleteDialogVisible,
    dismissDeleteDialog = setDeleteDialogVisibility,
    onDeleteItem = deleteItem,
    onDeleteMultipleItems = deleteSelectedItems,
    onFabClick = onFabClick,
    lazyColumnContent = {
        workoutList(
            workoutList = workoutList,
            selectionMode = selectionMode,
            onSelectionModeChange = setSelectionModeValueGlobally,
            onLongPress = onLongPress,
            selectedWorkouts = selectedItemsIdList,
            onSelectionChange = onItemSelectionChange,
            onClick = onWorkoutItemClick,
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
    })

private fun LazyListScope.workoutList(
    workoutList: ImmutableList<Workout>,
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
private fun WorkoutItemContent(workout: Workout) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(end = PaddingManager.Medium),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
) {
    SecondaryText(
        text = "${
            pluralStringResource(
                id = R.plurals.workout_total_rounds, count = workout.rounds, workout.rounds
            )
        }\n${
            pluralStringResource(
                id = R.plurals.workout_total_combos,
                count = workout.comboList.size,
                workout.comboList.size
            )
        }", textAlign = TextAlign.Center, maxLines = 2
    )

    SecondaryText(
        text = stringResource(
            R.string.workout_round_time, workout.roundLengthSeconds.toTime().asString()
        ), textAlign = TextAlign.Center, maxLines = 2
    )

    SecondaryText(
        text = stringResource(
            R.string.workout_rest_time, workout.restLengthSeconds.toTime().asString()
        ), textAlign = TextAlign.Center, maxLines = 2
    )
}