package com.thestrikingarts.ui.losersscreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thestrikingarts.domain.mediaplayer.EventPlayer
import com.thestrikingarts.domain.model.Workout
import com.thestrikingarts.domain.workout.RetrieveWorkoutUseCase
import com.thestrikingarts.domain.workoutresult.UpsertWorkoutResultUseCase
import com.thestrikingarts.domainandroid.audioplayers.PlayerConstants
import com.thestrikingarts.ui.navigation.Screen.Arguments.LOSERS_WORKOUT_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LosersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val eventPlayer: EventPlayer,
    private val retrieveWorkoutUseCase: RetrieveWorkoutUseCase,
    private val upsertWorkoutResultUseCase: UpsertWorkoutResultUseCase
) : ViewModel() {
    private val workoutId: Long = savedStateHandle[LOSERS_WORKOUT_ID] ?: 0

    private lateinit var workout: Workout

    private val _loadingScreen = MutableStateFlow(false)
    val loadingScreen = _loadingScreen.asStateFlow()

    init {
        viewModelScope.launch { initialUiUpdate() }
    }

    private suspend fun initialUiUpdate() {
        fetchWorkout()

        insertAbortedWorkout()

        eventPlayer.play(QUIT_SOUND)

        _loadingScreen.update { false }
    }

    private suspend fun fetchWorkout() {
        if (workoutId != 0L) viewModelScope.launch {
            workout = retrieveWorkoutUseCase(workoutId) ?: Workout()
        }.join()
        else workout = Workout()
    }

    private suspend fun insertAbortedWorkout() {
        if (workoutId != 0L) viewModelScope.launch {
            upsertWorkoutResultUseCase(
                workoutId = workoutId, workoutName = workout.name, isWorkoutAborted = true
            )
        }.join()
    }

    override fun onCleared() {
        eventPlayer.release()
        super.onCleared()
    }

    companion object {
        private const val QUIT_SOUND = "${PlayerConstants.ASSET_SESSION_EVENT_PATH_PREFIX}/quit.wav"
    }
}