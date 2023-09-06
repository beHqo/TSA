package com.example.android.strikingarts.ui.training

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.mapper.toWorkoutDetails
import com.example.android.strikingarts.domain.model.WorkoutListItem
import com.example.android.strikingarts.domain.usecase.training.ComboPlayerUseCase
import com.example.android.strikingarts.domain.usecase.training.SubRoundCalculatorUseCase
import com.example.android.strikingarts.domain.usecase.training.TimerUseCase
import com.example.android.strikingarts.domain.usecase.training.TimerUseCase.Companion.INITIAL_COUNTDOWN_SECONDS
import com.example.android.strikingarts.domain.usecase.workout.RetrieveWorkoutUseCase
import com.example.android.strikingarts.hilt.module.DefaultDispatcher
import com.example.android.strikingarts.ui.audioplayers.PlayerConstants.ASSET_SESSION_EVENT_PATH_PREFIX
import com.example.android.strikingarts.ui.audioplayers.soundpool.SoundPoolWrapper
import com.example.android.strikingarts.ui.model.TimerState
import com.example.android.strikingarts.ui.model.TimerStatus
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.TRAINING_WORKOUT_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val retrieveWorkoutUseCase: RetrieveWorkoutUseCase,
    private val soundPoolWrapper: SoundPoolWrapper,
    private val timerUseCase: TimerUseCase,
    private val subRoundCalculatorUseCase: SubRoundCalculatorUseCase,
    private val comboPlayerUseCase: ComboPlayerUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val workoutId: Long = savedStateHandle[TRAINING_WORKOUT_ID] ?: 2L

    lateinit var workoutListItem: WorkoutListItem

    private val _currentComboIndex = MutableStateFlow(0)
    private val _onSessionComplete = MutableStateFlow<(Long) -> Unit> {}
    private val _loadingScreen = MutableStateFlow(true)

    val loadingScreen = _loadingScreen.asStateFlow()
    val currentComboIndex = _currentComboIndex.asStateFlow()

    private val subRoundDelayMap = MutableStateFlow(mapOf<Int, Long>())

    private val isRoundTimerActive = timerUseCase.isRoundTimerActive
    val currentRound = timerUseCase.currentRound
    val timerState = timerUseCase.timerState
    val timerFlow = timerUseCase.timerFlow.onEach { timeSeconds ->
        onSubRoundIntersectReached(timeSeconds, timerState.value.timerStatus.isTimerRunning())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), INITIAL_COUNTDOWN_SECONDS)

    val currentComboText = comboPlayerUseCase.currentComboText
    val currentColor = comboPlayerUseCase.currentColorString

    init {
        viewModelScope.launch { initialUiUpdate() }
    }

    private suspend fun initialUiUpdate() {
        workoutListItem =
            if (workoutId == 0L) WorkoutListItem() else retrieveWorkoutUseCase(workoutId)

        updateSoundPoolFiles()

        calculateSubRoundIntersects()

        _currentComboIndex.update { savedStateHandle[CURRENT_COMBO_INDEX] ?: 0 }

        _loadingScreen.update { false }

        collectRoundTimerActive()
        collectTimerState()

        initializeAndStartCountdown()
    }

    private suspend fun updateSoundPoolFiles() {
        val sessionEventAudioList = listOf(ROUND_COMPLETION_AUDIO, BREAKPOINT_AUDIO)

        for (audioString in sessionEventAudioList) soundPoolWrapper.loadSoundString(audioString)
    }

    private fun calculateSubRoundIntersects() {
        if (workoutListItem.subRounds > 1) subRoundDelayMap.update {
            subRoundCalculatorUseCase.calculateSubRoundIntersects(
                roundLengthSeconds = workoutListItem.roundLengthSeconds,
                subRounds = workoutListItem.subRounds
            )
        }
    }

    private fun initializeAndStartCountdown() = timerUseCase.initializeAndStart(
        workoutDetails = workoutListItem.toWorkoutDetails(),
        onSessionCompletion = { _onSessionComplete.value(workoutId) },
        roundTimerActive = savedStateHandle[ROUND_TIMER_ACTIVE],
        currentRound = savedStateHandle[CURRENT_ROUND],
        currentTimeSeconds = savedStateHandle[CURRENT_TIME_SECONDS]
    )

    private fun collectRoundTimerActive() {
        viewModelScope.launch {
            isRoundTimerActive.collectLatest { roundTimerActive ->
                if (!roundTimerActive) comboPlayerUseCase.pause()
            }
        }
    }

    private fun collectTimerState() {
        viewModelScope.launch {
            timerState.collectLatest { state ->
                withContext(defaultDispatcher) {
                    onRoundStartedNotification(state)

                    comboPlayerUseCase.startPlayback(
                        comboList = workoutListItem.comboList,
                        currentComboIndex = currentComboIndex,
                        timerState = timerState,
                        isRoundTimerActive = isRoundTimerActive
                    ) { _currentComboIndex.update { it + 1 } }
                }
            }
        }
    }

    private fun onSubRoundIntersectReached(currentTimeSeconds: Int, isTimerRunning: Boolean) {
        if (currentTimeSeconds != 0 && isTimerRunning && isRoundTimerActive.value) {
            val delay = subRoundDelayMap.value[currentTimeSeconds] ?: return

            viewModelScope.launch {
                if (delay != 0L) delay(delay)

                soundPoolWrapper.play(BREAKPOINT_AUDIO)
            }
        }
    }

    private fun onRoundStartedNotification(state: TimerState) {
        if (state.timerStatus == TimerStatus.PLAYING) {
            val restoredTimeSeconds: Int? = savedStateHandle[CURRENT_TIME_SECONDS]

            val restTimerActive =
                timerState.value.totalTimeSeconds == workoutListItem.restLengthSeconds

            if (isRoundTimerActive.value || restTimerActive || restoredTimeSeconds != null) viewModelScope.launch {
                soundPoolWrapper.play(ROUND_COMPLETION_AUDIO)
            }
        }
    }

    fun updateOnSessionComplete(sessionComplete: (Long) -> Unit) =
        _onSessionComplete.update { sessionComplete }


    fun resume() {
        timerUseCase.resume()
    }

    fun pause() {
        comboPlayerUseCase.pause()

        timerUseCase.pause()
    }

    fun stop() {
        timerUseCase.stop()

        comboPlayerUseCase.pause()

        _currentComboIndex.update { 0 }
    }

    fun onProcessDeath() {
        pause()

        savedStateHandle[CURRENT_COMBO_INDEX] = _currentComboIndex.value
        savedStateHandle[CURRENT_ROUND] = currentRound.value
        savedStateHandle[ROUND_TIMER_ACTIVE] = isRoundTimerActive.value
        savedStateHandle[CURRENT_TIME_SECONDS] = timerFlow.value
    }

    override fun onCleared() {
        stop()

        soundPoolWrapper.release()
        comboPlayerUseCase.release()

        super.onCleared()
    }

    companion object {
        private const val CURRENT_COMBO_INDEX = "current_combo_index"
        private const val ROUND_TIMER_ACTIVE = "round_timer_active"
        private const val CURRENT_ROUND = "current_round"
        private const val CURRENT_TIME_SECONDS = "current_time_seconds"

        private const val ROUND_COMPLETION_AUDIO = ASSET_SESSION_EVENT_PATH_PREFIX + "bell.wav"
        private const val BREAKPOINT_AUDIO = ASSET_SESSION_EVENT_PATH_PREFIX + "breakpoint.wav"
    }
}