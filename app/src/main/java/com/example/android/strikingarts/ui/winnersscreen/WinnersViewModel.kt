package com.example.android.strikingarts.ui.winnersscreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.TechniqueCategory.DEFENSE
import com.example.android.strikingarts.domain.model.TechniqueCategory.OFFENSE
import com.example.android.strikingarts.domain.model.TechniqueListItem
import com.example.android.strikingarts.domain.model.WorkoutListItem
import com.example.android.strikingarts.domain.model.toImmutableList
import com.example.android.strikingarts.domain.usecase.winners.InsertTrainingDateUseCase
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
    private val insertTrainingDateUseCase: InsertTrainingDateUseCase,
    private val soundPoolWrapper: SoundPoolWrapper
) : ViewModel() {
    private val workoutId: Long = savedStateHandle[WINNERS_WORKOUT_ID] ?: 0L

    private val _workoutListItem = MutableStateFlow(WorkoutListItem())
    private val _loadingScreen = MutableStateFlow(true)

    val workoutListItem = _workoutListItem.asStateFlow()
    val loadingScreen = _loadingScreen.asStateFlow()

    var comboListSize: Int = 0
    private lateinit var techniqueList: ImmutableList<TechniqueListItem>
    var numberOfStrikes: Int = 0
    var numberOfDefensiveTechniques: Int = 0
    lateinit var mostRepeatedTechniqueOrEmpty: String

    init {
        viewModelScope.launch { initialUiUpdate() }
        insertOrUpdateTrainingDate()
    }

    private fun insertOrUpdateTrainingDate() {
        if (workoutId != 0L) viewModelScope.launch { insertTrainingDateUseCase.invoke(workoutId) }
    }

    private suspend fun initialUiUpdate() {
        if (workoutId != 0L) _workoutListItem.update { retrieveWorkoutUseCase(workoutId) }

        comboListSize = _workoutListItem.value.comboList.size

        initializeSessionDetails()

        _loadingScreen.update { false }

        soundPoolWrapper.play(SUCCESS_SOUND_EFFECT)
    }

    private fun initializeSessionDetails() {
        if (comboListSize > 0) {
            techniqueList =
                _workoutListItem.value.comboList.flatMap { it.techniqueList }.toImmutableList()

            numberOfStrikes = techniqueList.map { it.movementType == OFFENSE }.size

            numberOfDefensiveTechniques = techniqueList.map { it.movementType == DEFENSE }.size

            mostRepeatedTechniqueOrEmpty = getTheMostRepeatedTechniqueOrEmpty(techniqueList)
        }
    }

    private fun getTheMostRepeatedTechniqueOrEmpty(techniqueList: ImmutableList<TechniqueListItem>): String =
        techniqueList.maxByOrNull { it.id }?.name ?: ""

    companion object {
        private const val SUCCESS_SOUND_EFFECT = ASSET_SESSION_EVENT_PATH_PREFIX + "success.wav"
    }
}