package com.example.android.strikingarts.ui.workoutpreview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.mapper.getAudioStringList
import com.example.android.strikingarts.domain.model.Combo
import com.example.android.strikingarts.domain.model.Workout
import com.example.android.strikingarts.domain.usecase.training.ComboVisualPlayerUseCase
import com.example.android.strikingarts.domain.usecase.workout.DeleteWorkoutUseCase
import com.example.android.strikingarts.domain.usecase.workout.RetrieveWorkoutUseCase
import com.example.android.strikingarts.ui.audioplayers.mediaplayer.ComboAudioPlayerImpl
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.WORKOUT_PREVIEW_WORKOUT_ID
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

    lateinit var workoutListItem: Workout

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
        if (workoutId != 0L) workoutListItem = retrieveWorkoutUseCase(workoutId)

        _loadingScreen.update { false }
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