package com.example.android.strikingarts.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.model.TrainingWeekDay
import com.example.android.strikingarts.domain.model.WorkoutResult
import com.example.android.strikingarts.domain.usecase.home.GetTrainingWeekDaysForCurrentWeek
import com.example.android.strikingarts.domain.usecase.home.RetrieveLastExecutedWorkoutResultUseCase
import com.example.android.strikingarts.domain.usecase.javatime.GetDisplayNameForEpochDayUseCase
import com.example.android.strikingarts.domain.usecase.javatime.GetElapsedDaysSinceDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val retrieveLastExecutedWorkoutResultUseCase: RetrieveLastExecutedWorkoutResultUseCase,
    private val getWeekDaysForCurrentWeek: GetTrainingWeekDaysForCurrentWeek,
    private val getElapsedDaysSinceDateUseCase: GetElapsedDaysSinceDateUseCase,
    private val getDisplayNameForEpochDayUseCase: GetDisplayNameForEpochDayUseCase
) : ViewModel() {
    private val _loadingScreen = MutableStateFlow(true)
    val loadingScreen = _loadingScreen.asStateFlow()

    lateinit var weekDays: List<TrainingWeekDay>; private set

    lateinit var lastSuccessFullWorkout: WorkoutResult; private set
    var elapsedDaysSinceLastSuccessfulWorkout: Long = 0L; private set
    lateinit var lastSuccessfulWorkoutDisplayNameForDate: String; private set

    lateinit var lastFailedWorkout: WorkoutResult; private set
    var elapsedDaysSinceLastFailedWorkout: Long = 0L; private set
    lateinit var lastFailedWorkoutDisplayNameForDate: String; private set

    var isUserNew = false; private set

    init {
        viewModelScope.launch { initialUiUpdate() }
    }

    private suspend fun initialUiUpdate() {
        viewModelScope.launch { fetchLatestWorkoutData() }.join()

        _loadingScreen.update { false }
    }

    private suspend fun fetchLatestWorkoutData() {
        weekDays = getWeekDaysForCurrentWeek()

        viewModelScope.launch {
            lastSuccessFullWorkout = retrieveLastExecutedWorkoutResultUseCase.successful()
                ?: WorkoutResult().also { isUserNew = true; return@also }
        }.join()

        lastSuccessFullWorkout.let {
            if (it.workoutId != 0L) {
                elapsedDaysSinceLastSuccessfulWorkout = getElapsedDaysSinceDateUseCase(it.epochDay)
                lastSuccessfulWorkoutDisplayNameForDate =
                    getDisplayNameForEpochDayUseCase(it.epochDay)
            } else isUserNew = true
        }

        viewModelScope.launch {
            lastFailedWorkout = retrieveLastExecutedWorkoutResultUseCase.failed() ?: WorkoutResult()
        }.join()

        lastFailedWorkout.let {
            if (it.workoutId != 0L) {
                elapsedDaysSinceLastFailedWorkout = getElapsedDaysSinceDateUseCase(it.epochDay)
                lastFailedWorkoutDisplayNameForDate = getDisplayNameForEpochDayUseCase(it.epochDay)
            } else {
                elapsedDaysSinceLastFailedWorkout = 0
                lastFailedWorkoutDisplayNameForDate = ""
            }
        }
    }
}