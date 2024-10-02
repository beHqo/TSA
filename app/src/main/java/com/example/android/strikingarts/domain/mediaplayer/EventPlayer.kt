package com.example.android.strikingarts.domain.mediaplayer

interface EventPlayer {
    suspend fun play(audioString: String)
    suspend fun loadSoundString(str: String): Int
    fun release()
}