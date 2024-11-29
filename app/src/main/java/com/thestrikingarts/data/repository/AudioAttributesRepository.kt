package com.thestrikingarts.data.repository

import com.thestrikingarts.data.local.dao.AudioAttributesDao
import com.thestrikingarts.domain.audioattributes.AudioAttributesCacheRepository
import com.thestrikingarts.domain.logger.DataLogger
import com.thestrikingarts.domain.model.AudioAttributes
import com.thestrikingarts.domain.model.UriAudioAttributes
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