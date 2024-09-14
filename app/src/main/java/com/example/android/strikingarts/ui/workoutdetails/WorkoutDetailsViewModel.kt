package com.example.android.strikingarts.ui.workoutdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.model.Workout
import com.example.android.strikingarts.domain.usecase.selection.RetrieveSelectedItemsIdList
import com.example.android.strikingarts.domain.usecase.selection.UpdateSelectedItemsIdList
import com.example.android.strikingarts.domain.usecase.workout.RetrieveWorkoutUseCase
import com.example.android.strikingarts.domain.usecase.workout.UpsertWorkoutUseCase
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.example.android.strikingarts.ui.model.Time
import com.example.android.strikingarts.ui.model.toTime
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.WORKOUT_DETAILS_WORKOUT_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutDetailsViewModel @Inject constructor(
    retrieveSelectedItemsIdList: RetrieveSelectedItemsIdList,
    private val retrieveWorkoutUseCase: RetrieveWorkoutUseCase,
    private val updateSelectedItemsIdList: UpdateSelectedItemsIdList,
    private val upsertWorkoutUseCase: UpsertWorkoutUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val workoutId = savedStateHandle[WORKOUT_DETAILS_WORKOUT_ID] ?: 0L

    private lateinit var workout: Workout
    var isWorkoutNew = true; private set

    val selectedItemsIdList = retrieveSelectedItemsIdList()

    private val _loadingScreen = MutableStateFlow(true)
    private val _name = MutableStateFlow("")
    private val _rounds = MutableStateFlow(0)
    private val _roundLength = MutableStateFlow(Time())
    private val _restLength = MutableStateFlow(Time())
    private val _subRound = MutableStateFlow(0)

    val loadingScreen = _loadingScreen.asStateFlow()
    val name = _name.asStateFlow()
    val rounds = _rounds.asStateFlow()
    val roundLength = _roundLength.asStateFlow()
    val restLength = _restLength.asStateFlow()
    val subRound = _subRound.asStateFlow()

    init {
        viewModelScope.launch { initialUiUpdate() }
    }

    private suspend fun initialUiUpdate() {
        if (workoutId != 0L) {
            workout = retrieveWorkoutUseCase(workoutId)
            isWorkoutNew = false

            updateSelectedItemsIdList(workout.comboList.map { it.id })
        } else {
            workout = Workout()
            updateSelectedItemsIdList(listOf())
        }

        _name.update { savedStateHandle[NAME] ?: workout.name }
        _rounds.update { savedStateHandle[ROUNDS] ?: workout.rounds }
        _roundLength.update {
            savedStateHandle[ROUND_LENGTH] ?: workout.roundLengthSeconds.toTime()
        }
        _restLength.update {
            savedStateHandle[REST_LENGTH] ?: workout.restLengthSeconds.toTime()
        }
        _subRound.update {
            savedStateHandle[SUB_ROUNDS] ?: workout.subRounds
        }

        _loadingScreen.update { false }
    }

    fun onNameChange(value: String) {
        if (value.length <= TEXTFIELD_NAME_MAX_CHARS + 1) _name.update { value.trim() }
    }

    fun onRoundsChange(value: Int) {
        _rounds.update { value }
    }

    fun onRoundDurationChange(value: Time) {
        _roundLength.update { value }
    }

    fun onRestDurationChange(value: Time) {
        _restLength.update { value }
    }

    fun onSubRoundsChange(value: Int) {
        _subRound.update { value }
    }

    fun insertOrUpdateItem() {
        viewModelScope.launch {
            upsertWorkoutUseCase(
                Workout(
                    id = workoutId,
                    name = _name.value,
                    rounds = _rounds.value,
                    roundLengthSeconds = _roundLength.value.toSeconds(),
                    restLengthSeconds = _restLength.value.toSeconds(),
                    subRounds = _subRound.value
                ), selectedItemsIdList.value
            )
        }
    }

    fun surviveProcessDeath() {
        savedStateHandle[NAME] = _name.value
        savedStateHandle[ROUNDS] = _rounds.value
        savedStateHandle[ROUND_LENGTH] = _roundLength.value
        savedStateHandle[REST_LENGTH] = _restLength.value
        savedStateHandle[SUB_ROUNDS] = _subRound.value
    }

    private companion object {
        const val NAME = "name"
        const val ROUNDS = "rounds"
        const val ROUND_LENGTH = "roundLength"
        const val REST_LENGTH = "restLength"
        const val SUB_ROUNDS = "breakpoints"
    }
}