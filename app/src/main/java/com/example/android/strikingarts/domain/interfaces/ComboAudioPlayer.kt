package com.example.android.strikingarts.domain.interfaces

import com.example.android.strikingarts.domain.model.ImmutableList
import kotlinx.coroutines.flow.StateFlow

interface ComboAudioPlayer {
    val isPlaying: StateFlow<Boolean>

    suspend fun setupMediaPlayers(comboId: Long, audioStringList: ImmutableList<String>)

    suspend fun play(comboDurationMillis: Long = 1)
    fun pause()
    fun release()
}