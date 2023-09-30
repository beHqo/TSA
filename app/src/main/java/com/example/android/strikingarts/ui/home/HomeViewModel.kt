package com.example.android.strikingarts.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.WeekDay
import com.example.android.strikingarts.domain.usecase.home.GetWeekDaysForCurrentWeek
import com.example.android.strikingarts.domain.usecase.home.RetrieveLastExecutedWorkoutDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeekDaysForCurrentWeek: GetWeekDaysForCurrentWeek,
    private val retrieveLastExecutedDetails: RetrieveLastExecutedWorkoutDetails
) : ViewModel() {
    private val _loadingScreen = MutableStateFlow(true)

    val loadingScreen = _loadingScreen.asStateFlow()

    lateinit var weekDays: ImmutableList<WeekDay>
    var lastExecutedWorkoutId: Long = 0L
    lateinit var lastExecutedWorkoutName: String
    var elapsedDaysSinceLastExecutedWorkout: Long = 0L
    lateinit var lastExecutedWorkoutDisplayNameForDate: String

    init {
        viewModelScope.launch { initialUiUpdate() }
    }

    private suspend fun initialUiUpdate() {
        weekDays = getWeekDaysForCurrentWeek()
        lastExecutedWorkoutId = retrieveLastExecutedDetails.getWorkoutId()
        lastExecutedWorkoutName = retrieveLastExecutedDetails.getWorkoutName()
        elapsedDaysSinceLastExecutedWorkout =
            retrieveLastExecutedDetails.getElapsedDaysSinceLastExecutedWorkout()
        lastExecutedWorkoutDisplayNameForDate = retrieveLastExecutedDetails.getDisplayNameForDate()

        _loadingScreen.update { false }
    }
}