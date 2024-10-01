package com.example.android.strikingarts.domain.mediaplayer

import kotlinx.coroutines.flow.StateFlow

interface ComboAudioPlayer {
    val isPlaying: StateFlow<Boolean>

    suspend fun setupMediaPlayers(comboId: Long, audioStringList: List<String>)

    suspend fun play(comboDurationMillis: Long = 1)
    fun pause()
    fun release()
}