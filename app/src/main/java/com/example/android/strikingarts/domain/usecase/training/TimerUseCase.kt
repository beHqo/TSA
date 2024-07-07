package com.example.android.strikingarts.domain.usecase.training

import com.example.android.strikingarts.domain.model.TimerState
import com.example.android.strikingarts.domain.model.TimerStatus
import com.example.android.strikingarts.domain.model.WorkoutDetails
import com.example.android.strikingarts.domain.timer.CountdownTimer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class TimerUseCase @Inject constructor(countdownTimer: CountdownTimer) {
    private val workoutDetails = MutableStateFlow(WorkoutDetails())
    private val preparationPeriodSeconds = MutableStateFlow(5)
    private val onSessionComplete = MutableStateFlow {}
    private val _currentRound = MutableStateFlow(1)
    private val _isRoundTimerActive = MutableStateFlow(false)

    private val _timerState = MutableStateFlow(TimerState())

    private var initialTimerState = TimerState(timerStatus = TimerStatus.STOPPED)
    private var restTimerState = TimerState(timerStatus = TimerStatus.PLAYING)
    private var roundTimerState = TimerState(timerStatus = TimerStatus.PLAYING)
    private var finalTimerState = TimerState(timerStatus = TimerStatus.STOPPED)

    val currentRound = _currentRound.asStateFlow()
    val timerState = _timerState.asStateFlow()
    val isRoundTimerActive = _isRoundTimerActive.asStateFlow()

    val timerFlow = countdownTimer.timerFlow(_timerState)

    fun initializeAndStart(
        workoutDetails: WorkoutDetails,
        preparationPeriodSeconds: Int,
        onSessionCompletion: () -> Unit,
        roundTimerActive: Boolean?,
        currentRound: Int?,
        currentTimeSeconds: Int?
    ) {
        this.workoutDetails.update { workoutDetails }

        this.preparationPeriodSeconds.update { preparationPeriodSeconds }

        this.onSessionComplete.update { onSessionCompletion }

        initializeTimerStates()

        if (roundTimerActive != null) _isRoundTimerActive.update { roundTimerActive } else {
            _timerState.update { initialTimerState }
            start()
            return
        }

        if (currentRound != null) this._currentRound.update { currentRound }

        if (currentTimeSeconds != null) restoreTimerState(currentTimeSeconds)

        resume()
    }

    private fun initializeTimerStates() {
        initialTimerState = TimerState(
            timerStatus = TimerStatus.STOPPED,
            totalTimeSeconds = preparationPeriodSeconds.value,
            onTimerFinished = ::onRestFinished
        )

        roundTimerState = TimerState(
            timerStatus = TimerStatus.PLAYING,
            totalTimeSeconds = workoutDetails.value.roundLengthSeconds,
            onTimerFinished = ::onRoundFinished
        )

        restTimerState = TimerState(
            timerStatus = TimerStatus.PLAYING,
            totalTimeSeconds = workoutDetails.value.restLengthSeconds,
            onTimerFinished = ::onRestFinished
        )

        finalTimerState = TimerState(
            timerStatus = TimerStatus.STOPPED, onTimerFinished = onSessionComplete.value
        )
    }

    private fun restoreTimerState(currentTimeSeconds: Int) = _timerState.update {
        TimerState(
            timerStatus = TimerStatus.STOPPED,
            totalTimeSeconds = currentTimeSeconds,
            onTimerFinished = if (_isRoundTimerActive.value) ::onRoundFinished else ::onRestFinished
        )
    }

    private fun onRoundFinished() = if (_currentRound.value < workoutDetails.value.rounds) {
        _isRoundTimerActive.update { false }

        _timerState.update { restTimerState }

        _currentRound.update { it + 1 }
    } else _timerState.update { finalTimerState }


    private fun onRestFinished() {
        _isRoundTimerActive.update { true }

        _timerState.update { roundTimerState }
    }

    fun start() = _timerState.update { it.copy(timerStatus = TimerStatus.PLAYING) }
    fun resume() = _timerState.update { it.copy(timerStatus = TimerStatus.RESUMED) }
    fun pause() = _timerState.update { it.copy(timerStatus = TimerStatus.PAUSED) }
    fun stop() = _timerState.update { it.copy(timerStatus = TimerStatus.STOPPED) }
}