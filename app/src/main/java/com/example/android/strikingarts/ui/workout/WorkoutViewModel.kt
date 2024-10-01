package com.example.android.strikingarts.ui.workout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.selection.SelectionUseCase
import com.example.android.strikingarts.domain.workout.DeleteWorkoutUseCase
import com.example.android.strikingarts.domain.workout.RetrieveWorkoutListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    retrieveWorkoutListUseCase: RetrieveWorkoutListUseCase,
    private val deleteWorkoutUseCase: DeleteWorkoutUseCase,
    private val selectionUseCase: SelectionUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val initialSelectionMode = savedStateHandle[SELECTION_MODE] ?: false

    val workoutList = retrieveWorkoutListUseCase.workoutList.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val selectedItemsIdList = selectionUseCase.selectedItemsIdList

    private val _selectionMode = MutableStateFlow(initialSelectionMode)
    private val _deleteDialogVisible = MutableStateFlow(false)

    val selectionMode = _selectionMode.asStateFlow()
    val deleteDialogVisible = _deleteDialogVisible.asStateFlow()

    private val itemId = MutableStateFlow(0L)

    init {
        _selectionMode.update { initialSelectionMode }
    }

    fun onItemSelectionChange(id: Long, newSelectedValue: Boolean) {
        selectionUseCase.onItemSelectionChange(id, newSelectedValue)
    }

    fun onLongPress(id: Long) {
        if (_selectionMode.value) exitSelectionMode() else {
            selectionUseCase.deselectAllItems()
            _selectionMode.update { true }
            selectionUseCase.onItemSelectionChange(id, true)
        }
    }

    fun selectAllItems() {
        selectionUseCase.selectAllItems(workoutList.value.map { it.id })
    }

    fun deselectAllItems() {
        selectionUseCase.deselectAllItems()
    }

    fun exitSelectionMode() {
        _selectionMode.update { false }
    }

    fun setDeleteDialogVisibility(visible: Boolean) {
        _deleteDialogVisible.update { visible }
    }

    fun showDeleteDialogAndUpdateId(id: Long) {
        _deleteDialogVisible.update { true }
        itemId.update { id }
    }

    suspend fun deleteItem(): Boolean = viewModelScope.async {
        val affectedRows = deleteWorkoutUseCase(itemId.value)

        val deleteOperationSuccessful = handleDeleteOperationResult(affectedRows)

        _deleteDialogVisible.update { false }

        return@async deleteOperationSuccessful
    }.await()

    suspend fun deleteSelectedItems(): Boolean = viewModelScope.async {
        val affectedRows = deleteWorkoutUseCase(selectedItemsIdList.value.toList())

        val deleteOperationSuccessful = handleDeleteOperationResult(affectedRows)

        _deleteDialogVisible.update { false }

        return@async deleteOperationSuccessful
    }.await()

    private fun handleDeleteOperationResult(affectedRows: Long): Boolean = affectedRows != 0L

    fun surviveProcessDeath() {
        savedStateHandle[SELECTION_MODE] = _selectionMode.value
    }

    companion object {
        const val SELECTION_MODE = "workout_selection_mode"
    }
}