package com.thestrikingarts.ui.winnersscreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thestrikingarts.domain.mediaplayer.EventPlayer
import com.thestrikingarts.domain.model.MovementType
import com.thestrikingarts.domain.model.Technique
import com.thestrikingarts.domain.model.Workout
import com.thestrikingarts.domain.workout.RetrieveWorkoutUseCase
import com.thestrikingarts.domain.workoutresult.UpsertWorkoutResultUseCase
import com.thestrikingarts.domainandroid.audioplayers.PlayerConstants.ASSET_SESSION_EVENT_PATH_PREFIX
import com.thestrikingarts.ui.model.Time
import com.thestrikingarts.ui.model.toTime
import com.thestrikingarts.ui.navigation.Screen.Arguments.WINNERS_WORKOUT_ID
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
    private val upsertWorkoutResultUseCase: UpsertWorkoutResultUseCase,
    private val eventPlayer: EventPlayer
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
            upsertWorkoutResultUseCase(
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

        eventPlayer.play(SUCCESS_SOUND_EFFECT)
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
        private const val SUCCESS_SOUND_EFFECT = "${ASSET_SESSION_EVENT_PATH_PREFIX}/success.wav"
    }
}