package com.example.android.strikingarts.ui.calendar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.model.WorkoutResult
import com.example.android.strikingarts.domain.usecase.calendar.CalendarUseCase
import com.example.android.strikingarts.domain.usecase.calendar.RetrieveWorkoutResultsOfMonthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val retrieveWorkoutResultsOfMonthUseCase: RetrieveWorkoutResultsOfMonthUseCase,
    private val calendarUseCase: CalendarUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var beforeOrAfterMonth = savedStateHandle[BEFORE_OR_AFTER] ?: 0L

    val weekDays = calendarUseCase.weekDays

    private val _workoutResultsOfMonth = MutableStateFlow(emptyList<WorkoutResult>())
    private val _month = MutableStateFlow(calendarUseCase.getCurrentMonth())
    private val _selectedEpochDay = MutableStateFlow(0L)

    val month = _month.asStateFlow()

    val workoutResults = _selectedEpochDay.map { epochDay ->
        _workoutResultsOfMonth.value.filter { workoutResult -> workoutResult.epochDay == epochDay }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val epochDaysOfTrainingDays = _workoutResultsOfMonth.map {
        it.map { workoutResult -> workoutResult.epochDay }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val epochDaysOfQuittingDays = _workoutResultsOfMonth.map {
        it.filter { workoutResults -> workoutResults.isWorkoutAborted }
            .map { workoutResult -> workoutResult.epochDay }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch { initialUiUpdate() }
    }

    private suspend fun initialUiUpdate() {
        _workoutResultsOfMonth.update { retrieveWorkoutResultsOfMonthUseCase(beforeOrAfterMonth) }
    }

    fun getNextMonth() {
        _month.update { calendarUseCase.getNextMonth() }
        viewModelScope.launch {
            _workoutResultsOfMonth.update { retrieveWorkoutResultsOfMonthUseCase(++beforeOrAfterMonth) }
        }
    }

    fun getPreviousMonth() {
        _month.update { calendarUseCase.getPreviousMonth() }
        viewModelScope.launch {
            _workoutResultsOfMonth.update { retrieveWorkoutResultsOfMonthUseCase(--beforeOrAfterMonth) }
        }
    }

    fun setSelectedDate(epochDay: Long) {
        _selectedEpochDay.update { epochDay }
    }

    fun surviveProcessDeath() {
        savedStateHandle[BEFORE_OR_AFTER] = beforeOrAfterMonth
    }

    companion object {
        const val BEFORE_OR_AFTER = "before_or_after"
    }
}