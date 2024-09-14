package com.example.android.strikingarts.domain.interfaces

import com.example.android.strikingarts.domain.model.AudioAttributes
import com.example.android.strikingarts.domain.model.UriAudioAttributes

interface AudioAttributesCacheRepository {
    suspend fun insert(audioAttributes: AudioAttributes): Long
    suspend fun update(audioAttributes: UriAudioAttributes)
    suspend fun getAudioAttributesByPath(path: String): AudioAttributes?
}