package com.example.android.strikingarts.ui.workoutdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.model.WorkoutListItem
import com.example.android.strikingarts.domain.usecase.selection.RetrieveSelectedItemsIdList
import com.example.android.strikingarts.domain.usecase.selection.UpdateSelectedItemsIdList
import com.example.android.strikingarts.domain.usecase.workout.RetrieveWorkoutUseCase
import com.example.android.strikingarts.domain.usecase.workout.UpsertWorkoutUseCase
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.example.android.strikingarts.ui.model.Time
import com.example.android.strikingarts.ui.model.toTime
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.WORKOUT_ID
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
    private val workoutId = savedStateHandle[WORKOUT_ID] ?: 0L

    private val workoutListItem = MutableStateFlow(WorkoutListItem())

    val selectedItemsIdList = retrieveSelectedItemsIdList()

    private val _loadingScreen = MutableStateFlow(true)
    private val _name = MutableStateFlow("")
    private val _rounds = MutableStateFlow("3")
    private val _roundLength = MutableStateFlow(Time())
    private val _restLength = MutableStateFlow(Time())
    private val notificationIntervals = MutableStateFlow("0")

    val loadingScreen = _loadingScreen.asStateFlow()
    val name = _name.asStateFlow()
    val rounds = _rounds.asStateFlow()
    val roundLength = _roundLength.asStateFlow()
    val restLength = _restLength.asStateFlow()
    val breakpoints = notificationIntervals.asStateFlow()

    init {
        viewModelScope.launch { initialUiUpdate() }
    }

    private suspend fun initialUiUpdate() {
        if (workoutId != 0L) {
            workoutListItem.update { retrieveWorkoutUseCase(workoutId) }
        }

        updateSelectedItemsIdList(workoutListItem.value.comboList.map { it.id })

        _name.update { savedStateHandle[NAME] ?: workoutListItem.value.name }
        _rounds.update { savedStateHandle[ROUNDS] ?: workoutListItem.value.rounds }
        _roundLength.update {
            savedStateHandle[ROUND_LENGTH] ?: workoutListItem.value.roundLengthMilli.toTime()
        }
        _restLength.update {
            savedStateHandle[REST_LENGTH] ?: workoutListItem.value.restLengthMilli.toTime()
        }
        notificationIntervals.update {
            savedStateHandle[NOTIFICATION_INTERVALS] ?: workoutListItem.value.notificationIntervals
        }

        _loadingScreen.update { false }
    }

    fun onNameChange(value: String) {
        if (value.length <= TEXTFIELD_NAME_MAX_CHARS + 1) {
            _name.update { value }; savedStateHandle[NAME] = value
        }
    }

    fun onRoundsChange(value: String) {
        _rounds.update { value }
        savedStateHandle[ROUNDS] = value
    }

    fun onRoundDurationChange(value: Time) {
        _roundLength.update { value }
        savedStateHandle[ROUND_LENGTH] = value
    }

    fun onRestDurationChange(value: Time) {
        _restLength.update { value }
        savedStateHandle[REST_LENGTH] = value
    }

    fun onNotificationIntervalsChange(value: String) {
        notificationIntervals.update { value }
        savedStateHandle[NOTIFICATION_INTERVALS] = value
    }

    fun insertOrUpdateItem() {
        viewModelScope.launch {
            upsertWorkoutUseCase(
                WorkoutListItem(
                    id = workoutId,
                    name = _name.value,
                    rounds = _rounds.value,
                    roundLengthMilli = _roundLength.value.toMillieSeconds(),
                    restLengthMilli = _restLength.value.toMillieSeconds(),
                    notificationIntervals = notificationIntervals.value
                ), selectedItemsIdList.value
            )
        }
    }

    private companion object {
        const val NAME = "name"
        const val ROUNDS = "rounds"
        const val ROUND_LENGTH = "roundLength"
        const val REST_LENGTH = "restLength"
        const val NOTIFICATION_INTERVALS = "breakpoints"
    }
}