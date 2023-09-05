package com.example.android.strikingarts.domain.interfaces

import com.example.android.strikingarts.domain.model.ImmutableList
import kotlinx.coroutines.flow.StateFlow

interface ComboAudioPlayer {
    val isPlaying: StateFlow<Boolean>

    suspend fun play(
        audioStringList: ImmutableList<String>,
        comboId: Long = Long.MAX_VALUE,
        comboDurationMillis: Long = 0,
        comboDelayMillis: Long = 0
    )

    fun pause()
    fun release()
}