package com.github.tsa.data.repository

import com.github.tsa.data.local.dao.AudioAttributesDao
import com.github.tsa.domain.audioattributes.AudioAttributesCacheRepository
import com.github.tsa.domain.logger.DataLogger
import com.github.tsa.domain.model.AudioAttributes
import com.github.tsa.domain.model.UriAudioAttributes
import javax.inject.Inject

private const val TAG = "AudioAttributesRepo"

class AudioAttributesRepository @Inject constructor(
    private val audioAttributesDao: AudioAttributesDao
) : AudioAttributesCacheRepository {
    private val logger = DataLogger(TAG)

    override suspend fun insert(audioAttributes: AudioAttributes): Long {
        val id = audioAttributesDao.insert(audioAttributes)

        logger.logInsertOperation(id, audioAttributes)

        return id
    }

    override suspend fun update(audioAttributes: UriAudioAttributes) {
        val affectedRows = audioAttributesDao.update(audioAttributes)

        logger.logUpdateOperation(affectedRows, audioAttributes.id, audioAttributes)
    }

    override suspend fun getAudioAttributesByPath(path: String) =
        audioAttributesDao.getAudioAttributesByPath(path)
}