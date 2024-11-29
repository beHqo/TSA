package com.thestrikingarts.data.repository

import com.thestrikingarts.data.audioAttributesList
import com.thestrikingarts.domain.audioattributes.AudioAttributesCacheRepository
import com.thestrikingarts.domain.model.AudioAttributes
import com.thestrikingarts.domain.model.UriAudioAttributes
import kotlin.random.Random

class FakeAudioAttributesRepo : AudioAttributesCacheRepository {
    private val data = audioAttributesList

    override suspend fun insert(audioAttributes: AudioAttributes): Long =
        if (audioAttributes.id !in data.map { it.id }) Random(7).nextLong()
        else -1L

    override suspend fun update(audioAttributes: UriAudioAttributes) {

    }

    override suspend fun getAudioAttributesByPath(path: String): AudioAttributes? =
        data.firstOrNull { it.audioString == path }
}