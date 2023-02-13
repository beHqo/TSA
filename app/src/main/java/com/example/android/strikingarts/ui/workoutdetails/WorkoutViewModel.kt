package com.example.android.strikingarts.ui.workoutdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.database.dao.WorkoutDao
import com.example.android.strikingarts.database.entity.WorkoutWithCombos
import com.example.android.strikingarts.ui.parentlayouts.ListScreenViewModel
import com.example.android.strikingarts.utils.ImmutableList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle, workoutDao: WorkoutDao) :
    ListScreenViewModel() {
    override val initialSelectionMode = savedStateHandle[SELECTION_MODE] ?: false

    private val workoutList = workoutDao.getWorkoutList().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _visibleWorkouts = MutableStateFlow(ImmutableList<WorkoutWithCombos>())

    val visibleWorkouts = _visibleWorkouts.asStateFlow()

    init {
        viewModelScope.launch { retrieveWorkoutListAndUpdateState() }
        mLoadingScreen.update { false }
    }

    private suspend fun retrieveWorkoutListAndUpdateState() {
        workoutList.collectLatest { workouts -> initialUiUpdate(workouts) }
    }

    private fun initialUiUpdate(workouts: List<WorkoutWithCombos>) {
        _visibleWorkouts.update { ImmutableList(workouts) }

//        mLoadingScreen.update { false }
    }

    override fun onLongPress(id: Long) {
        super.onLongPress(id)
        savedStateHandle[SELECTION_MODE] = mSelectionMode.value
    }

    companion object {
        const val SELECTION_MODE = "workout_selection_mode"
    }
}