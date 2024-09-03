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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.R
import com.example.android.strikingarts.domain.model.Workout
import com.example.android.strikingarts.ui.components.SecondaryText
import com.example.android.strikingarts.ui.components.SelectionModeBottomSheet
import com.example.android.strikingarts.ui.components.listitem.BaseItemWithMultipleLinesSelectionMode
import com.example.android.strikingarts.ui.components.listitem.BaseItemWithMultipleLinesViewingMode
import com.example.android.strikingarts.ui.components.util.SurviveProcessDeath
import com.example.android.strikingarts.ui.model.toTime
import com.example.android.strikingarts.ui.parentlayouts.ListScreenLayout
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager
import kotlinx.coroutines.launch

@Composable
fun WorkoutScreen(
    vm: WorkoutViewModel = hiltViewModel(),
    showSnackbar: (String) -> Unit,
    navigateToWorkoutDetails: (Long?) -> Unit,
    navigateToWorkoutPreviewScreen: (id: Long) -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit,
) {
    val deleteDialogVisible by vm.deleteDialogVisible.collectAsStateWithLifecycle()
    val selectionMode by vm.selectionMode.collectAsStateWithLifecycle()
    val workoutList by vm.workoutList.collectAsStateWithLifecycle()
    val selectedItemsIdList by vm.selectedItemsIdList.collectAsStateWithLifecycle()

    val selectionButtonsEnabled by remember { derivedStateOf { selectedItemsIdList.size > 1 } }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    WorkoutScreen(
        navigateToWorkoutDetails = navigateToWorkoutDetails,
        workoutList = workoutList,
        setSelectionModeValueGlobally = setSelectionModeValueGlobally,
        selectedItemsIdList = selectedItemsIdList,
        onWorkoutItemClick = navigateToWorkoutPreviewScreen,
        selectionMode = selectionMode,
        exitSelectionMode = vm::exitSelectionMode,
        onLongPress = vm::onLongPress,
        onItemSelectionChange = vm::onItemSelectionChange,
        selectAllItems = vm::selectAllItems,
        deselectAllItems = vm::deselectAllItems,
        deleteDialogVisible = deleteDialogVisible,
        showDeleteDialogAndUpdateId = vm::showDeleteDialogAndUpdateId,
        setDeleteDialogVisibility = vm::setDeleteDialogVisibility,
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
        onFabClick = { navigateToWorkoutDetails(null) },
        selectionButtonsEnabled = selectionButtonsEnabled,
    )

    SurviveProcessDeath(onStop = vm::surviveProcessDeath)
}

@Composable
private fun WorkoutScreen(
    workoutList: List<Workout>,
    navigateToWorkoutDetails: (Long) -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    onWorkoutItemClick: (Long) -> Unit,
    selectedItemsIdList: List<Long>,
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
    workoutList: List<Workout>,
    selectionMode: Boolean,
    onSelectionModeChange: (Boolean) -> Unit,
    onLongPress: (Long) -> Unit,
    selectedWorkouts: List<Long>,
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