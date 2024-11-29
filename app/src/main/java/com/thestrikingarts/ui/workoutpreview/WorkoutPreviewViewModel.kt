package com.thestrikingarts.ui.workoutpreview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thestrikingarts.domain.mapper.getAudioStringList
import com.thestrikingarts.domain.model.Combo
import com.thestrikingarts.domain.model.Workout
import com.thestrikingarts.domain.training.ComboVisualPlayerUseCase
import com.thestrikingarts.domain.workout.DeleteWorkoutUseCase
import com.thestrikingarts.domain.workout.RetrieveWorkoutUseCase
import com.thestrikingarts.domainandroid.audioplayers.mediaplayer.ComboAudioPlayerImpl
import com.thestrikingarts.ui.navigation.Screen.Arguments.WORKOUT_PREVIEW_WORKOUT_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutPreviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val retrieveWorkoutUseCase: RetrieveWorkoutUseCase,
    private val comboAudioPlayer: ComboAudioPlayerImpl,
    private val comboDisplayUseCase: ComboVisualPlayerUseCase,
    private val deleteWorkoutUseCase: DeleteWorkoutUseCase
) : ViewModel() {
    private val workoutId: Long = savedStateHandle[WORKOUT_PREVIEW_WORKOUT_ID] ?: 0L

    lateinit var workout: Workout
    var isWorkoutRemoved = false

    private val _currentCombo = MutableStateFlow(Combo())
    private val _deleteDialogVisible = MutableStateFlow(false)
    private val _comboPreviewDialogVisible = MutableStateFlow(false)
    private val _loadingScreen = MutableStateFlow(true)

    val currentCombo = _currentCombo.asStateFlow()
    val deleteDialogVisible = _deleteDialogVisible.asStateFlow()
    val comboPreviewDialogVisible = _comboPreviewDialogVisible.asStateFlow()
    val loadingScreen = _loadingScreen.asStateFlow()

    val currentColor = comboDisplayUseCase.currentColorString

    init {
        viewModelScope.launch { initialUiUpdate() }
    }

    private suspend fun initialUiUpdate() {
        fetchWorkout()

        _loadingScreen.update { false }
    }

    private suspend fun fetchWorkout() {
        if (workoutId != 0L) viewModelScope.launch {
            workout = retrieveWorkoutUseCase(workoutId) ?: Workout().also {
                isWorkoutRemoved = true
            }
        }.join()
        else workout = Workout()
    }

    fun playComboPreview() {
        viewModelScope.launch {
            val combo = _currentCombo.value

            comboAudioPlayer.setupMediaPlayers(combo.id, combo.getAudioStringList())
            comboAudioPlayer.play()

            comboDisplayUseCase.display(combo)
        }
    }

    private fun pauseComboPreview() {
        viewModelScope.launch {
            comboAudioPlayer.pause()
            comboDisplayUseCase.pause()
        }
    }

    fun dismissComboPreviewDialog() {
        _comboPreviewDialogVisible.update { false }

        pauseComboPreview()
    }

    fun onComboClick(playableCombo: Combo) {
        _currentCombo.update { playableCombo }

        _comboPreviewDialogVisible.update { true }

        playComboPreview()
    }

    fun setDeleteDialogVisibility(value: Boolean) {
        _deleteDialogVisible.update { value }
    }

    fun onDelete(id: Long) {
        viewModelScope.launch { deleteWorkoutUseCase.invoke(id) }
    }
}