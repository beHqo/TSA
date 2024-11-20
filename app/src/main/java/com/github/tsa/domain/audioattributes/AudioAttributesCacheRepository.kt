package com.github.tsa.domain.audioattributes

import com.github.tsa.domain.model.AudioAttributes
import com.github.tsa.domain.model.UriAudioAttributes

interface AudioAttributesCacheRepository {
    suspend fun insert(audioAttributes: AudioAttributes): Long
    suspend fun update(audioAttributes: UriAudioAttributes)
    suspend fun getAudioAttributesByPath(path: String): AudioAttributes?
}