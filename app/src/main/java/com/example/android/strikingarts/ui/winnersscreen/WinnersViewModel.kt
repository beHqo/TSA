package com.example.android.strikingarts.ui.winnersscreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.model.MovementType
import com.example.android.strikingarts.domain.model.Technique
import com.example.android.strikingarts.domain.model.Workout
import com.example.android.strikingarts.domain.workout.RetrieveWorkoutUseCase
import com.example.android.strikingarts.domain.workoutresult.InsertWorkoutResultUseCase
import com.example.android.strikingarts.domainandroid.audioplayers.PlayerConstants.ASSET_SESSION_EVENT_PATH_PREFIX
import com.example.android.strikingarts.domainandroid.audioplayers.soundpool.SoundPoolWrapper
import com.example.android.strikingarts.ui.model.Time
import com.example.android.strikingarts.ui.model.toTime
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
    private val insertWorkoutResultUseCase: InsertWorkoutResultUseCase,
    private val soundPoolWrapper: SoundPoolWrapper
) : ViewModel() {
    private val workoutId: Long = savedStateHandle[WINNERS_WORKOUT_ID] ?: 0L

    lateinit var workout: Workout; private set

    private val _loadingScreen = MutableStateFlow(true)
    val loadingScreen = _loadingScreen.asStateFlow()

    var comboListSize: Int = 0; private set
    private lateinit var techniqueList: List<Technique>
    var numberOfStrikes: Int = 0; private set
    var numberOfDefensiveTechniques: Int = 0; private set
    lateinit var mostRepeatedTechniqueOrEmpty: String; private set
    lateinit var workoutTime: Time; private set

    init {
        viewModelScope.launch { initialUiUpdate() }
    }

    private suspend fun insertWorkoutResult() {
        if (workoutId != 0L) viewModelScope.launch {
            insertWorkoutResultUseCase(
                workoutId = workoutId, workoutName = workout.name, isWorkoutAborted = false
            )
        }.join()
    }

    private suspend fun initialUiUpdate() {
        fetchWorkout()

        comboListSize = workout.comboList.size

        initializeSessionDetails()

        insertWorkoutResult()

        _loadingScreen.update { false }

        soundPoolWrapper.play(SUCCESS_SOUND_EFFECT)
    }

    private suspend fun fetchWorkout() {
        if (workoutId != 0L) viewModelScope.launch {
            workout = retrieveWorkoutUseCase(workoutId) ?: Workout()
        }.join()
        else workout = Workout()
    }

    private fun initializeSessionDetails() {
        if (comboListSize > 0) {
            techniqueList = workout.comboList.flatMap { it.techniqueList }

            techniqueList.forEach {
                if (it.movementType == MovementType.OFFENSE) numberOfStrikes++
                else numberOfDefensiveTechniques++
            }

            mostRepeatedTechniqueOrEmpty =
                techniqueList.groupingBy { it.name }.eachCount().maxBy { it.value }.key
        } else workoutTime = (workout.rounds * workout.roundLengthSeconds).toTime()
    }

    companion object {
        private const val SUCCESS_SOUND_EFFECT = ASSET_SESSION_EVENT_PATH_PREFIX + "success.wav"
    }
}