package com.example.android.strikingarts.data.local.dao

import com.example.android.strikingarts.LocalDatabase
import com.example.android.strikingarts.data.local.mapper.toDomainModel
import com.example.android.strikingarts.domain.model.AudioAttributes
import com.example.android.strikingarts.domain.model.UriAudioAttributes
import com.example.android.strikingarts.hilt.module.DefaultDispatcher
import com.example.android.strikingarts.hilt.module.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AudioAttributesDao @Inject constructor(
    private val db: LocalDatabase,
    @IoDispatcher private val ioDispatchers: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatchers: CoroutineDispatcher
) {
    private val queries = db.audioAttributeQueries

    suspend fun insert(audioAttributes: UriAudioAttributes): Long = withContext(ioDispatchers) {
        db.transactionWithResult {
            queries.insert(
                name = audioAttributes.name,
                duration_millis = audioAttributes.durationMillis,
                path = audioAttributes.audioString
            )

            return@transactionWithResult queries.lastInsertedRowId().executeAsOne()
        }
    }

    suspend fun update(audioAttributes: UriAudioAttributes): Long = withContext(ioDispatchers) {
        db.transactionWithResult {
            queries.update(
                audioAttributesId = audioAttributes.id,
                name = audioAttributes.name,
                durationMillies = audioAttributes.durationMillis,
                path = audioAttributes.audioString
            )

            return@transactionWithResult queries.affectedRow().executeAsOne()
        }
    }

    suspend fun getAudioAttributesById(id: Long): AudioAttributes? = withContext(ioDispatchers) {
        val selected =
            queries.getAudioAttributesById(id).executeAsOneOrNull() ?: return@withContext null

        return@withContext withContext(defaultDispatchers) { selected.toDomainModel() }
    }

    suspend fun getAudioAttributesByPath(path: String): AudioAttributes? =
        withContext(ioDispatchers) {
            val selected = queries.getAudioAttributesByPath(path).executeAsOneOrNull()
                ?: return@withContext null

            return@withContext withContext(defaultDispatchers) { selected.toDomainModel() }
        }
}