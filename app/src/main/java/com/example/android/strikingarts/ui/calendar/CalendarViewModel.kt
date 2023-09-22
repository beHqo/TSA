package com.example.android.strikingarts.ui.calendar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.ImmutableMap
import com.example.android.strikingarts.domain.model.toImmutableList
import com.example.android.strikingarts.domain.usecase.calendar.CalendarUseCase
import com.example.android.strikingarts.domain.usecase.calendar.RetrieveTrainingDatesOfMonthUseCase
import com.example.android.strikingarts.domain.usecase.calendar.SetMonthBoundsUseCase
import com.example.android.strikingarts.domain.usecase.workout.RetrieveWorkoutNamesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    retrieveTrainingDateOfMonthUseCase: RetrieveTrainingDatesOfMonthUseCase,
    private val calendarUseCase: CalendarUseCase,
    private val setMonthBoundsUseCase: SetMonthBoundsUseCase,
    private val retrieveWorkoutNamesUseCase: RetrieveWorkoutNamesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var beforeOrAfterMonth = savedStateHandle[BEFORE_OR_AFTER] ?: 0L

    init {
        setMonthBoundsUseCase(beforeOrAfterMonth)
    }

    val trainingDatesByMonthMap = retrieveTrainingDateOfMonthUseCase.trainingDateMapByMonth.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), ImmutableMap()
    )

    val weekDays = calendarUseCase.weekDays.toImmutableList()

    private val _month = MutableStateFlow(calendarUseCase.getCurrentMonth())
    private val _workoutNames = MutableStateFlow(ImmutableList<Pair<Long, String>>())

    val month = _month.asStateFlow()
    val workoutNames = _workoutNames.asStateFlow()

    fun getNextMonth() {
        setMonthBoundsUseCase(++beforeOrAfterMonth)

        _month.update { calendarUseCase.getNextMonth() }
    }

    fun getPreviousMonth() {
        setMonthBoundsUseCase(--beforeOrAfterMonth)

        _month.update { calendarUseCase.getPreviousMonth() }
    }

    fun retrieveWorkoutNames(idList: ImmutableList<Long>?) {
        if (idList == null) return

        viewModelScope.launch {
            val workoutNames = mutableListOf<Pair<Long, String>>()
            val workoutNameList = retrieveWorkoutNamesUseCase(idList)

            for (i in idList.indices) {
                workoutNames += Pair(idList.getOrNull(i) ?: 0L, workoutNameList.getOrNull(i) ?: "")
            }

            _workoutNames.update { workoutNames.toImmutableList() }
        }
    }

    fun surviveProcessDeath() {
        savedStateHandle[BEFORE_OR_AFTER] = beforeOrAfterMonth
    }

    companion object {
        const val BEFORE_OR_AFTER = "before_or_after"
    }
}