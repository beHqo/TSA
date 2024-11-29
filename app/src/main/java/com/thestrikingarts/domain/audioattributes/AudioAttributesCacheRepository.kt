package com.thestrikingarts.domain.audioattributes

import com.thestrikingarts.domain.model.AudioAttributes
import com.thestrikingarts.domain.model.UriAudioAttributes

interface AudioAttributesCacheRepository {
    suspend fun insert(audioAttributes: AudioAttributes): Long
    suspend fun update(audioAttributes: UriAudioAttributes)
    suspend fun getAudioAttributesByPath(path: String): AudioAttributes?
}