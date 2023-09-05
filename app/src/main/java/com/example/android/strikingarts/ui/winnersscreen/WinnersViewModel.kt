package com.example.android.strikingarts.ui.winnersscreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.model.WorkoutListItem
import com.example.android.strikingarts.domain.usecase.workout.RetrieveWorkoutUseCase
import com.example.android.strikingarts.ui.audioplayers.PlayerConstants.ASSET_SESSION_EVENT_PATH_PREFIX
import com.example.android.strikingarts.ui.audioplayers.soundpool.SoundPoolWrapper
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.WINNERS_WORKOUT_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WinnersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val retrieveWorkoutUseCase: RetrieveWorkoutUseCase,
    private val soundPoolWrapper: SoundPoolWrapper
) : ViewModel() {
    private val workoutId: Long = savedStateHandle[WINNERS_WORKOUT_ID] ?: 0L

    private val _workoutListItem = MutableStateFlow(WorkoutListItem())
    private val _loadingScreen = MutableStateFlow(true)

    val workoutListItem = _workoutListItem.asStateFlow()
    val loadingScreen = _loadingScreen.asStateFlow()

    init {
        viewModelScope.launch { initialUiUpdate() }
    }

    private suspend fun initialUiUpdate() {
        if (workoutId != 0L) _workoutListItem.update { retrieveWorkoutUseCase(workoutId) }

        _loadingScreen.update { false }

        soundPoolWrapper.play(SUCCESS_SOUND_EFFECT)
    }

    companion object {
        private const val SUCCESS_SOUND_EFFECT = ASSET_SESSION_EVENT_PATH_PREFIX + "success.wav"
    }
}