package com.example.android.strikingarts.data.repository

import com.example.android.strikingarts.data.local.dao.AudioAttributesDao
import com.example.android.strikingarts.domain.common.logger.DataLogger
import com.example.android.strikingarts.domain.interfaces.AudioAttributesCacheRepository
import com.example.android.strikingarts.domain.model.UriAudioAttributes
import javax.inject.Inject

private const val TAG = "AudioAttributesRepo"

class AudioAttributesRepository @Inject constructor(
    private val audioAttributesDao: AudioAttributesDao
) : AudioAttributesCacheRepository {
    private val logger = DataLogger(TAG)

    override suspend fun insert(audioAttributes: UriAudioAttributes): Long {
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