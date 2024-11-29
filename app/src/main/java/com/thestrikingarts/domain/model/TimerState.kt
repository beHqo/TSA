package com.thestrikingarts.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class TimerStatus {
    PLAYING, RESUMED, PAUSED, STOPPED;

    fun isTimerRunning(): Boolean = this == PLAYING || this == RESUMED
}

@Parcelize
data class TimerState(
    val timerStatus: TimerStatus = TimerStatus.STOPPED,
    val totalTimeSeconds: Int = 0,
    val onTimerFinished: () -> Unit = {}
) : Parcelable