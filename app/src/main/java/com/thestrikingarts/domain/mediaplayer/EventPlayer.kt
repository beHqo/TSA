package com.thestrikingarts.domain.mediaplayer

interface EventPlayer {
    suspend fun play(audioString: String)
    suspend fun loadSoundString(str: String): Int
    fun release()
}