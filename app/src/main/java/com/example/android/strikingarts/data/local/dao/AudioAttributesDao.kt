package com.example.android.strikingarts.data.local.dao

import com.example.android.strikingarts.LocalDatabase
import com.example.android.strikingarts.data.local.mapper.toDomainModel
import com.example.android.strikingarts.di.DefaultDispatcher
import com.example.android.strikingarts.di.IoDispatcher
import com.example.android.strikingarts.domain.model.AudioAttributes
import com.example.android.strikingarts.domain.model.UriAudioAttributes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AudioAttributesDao @Inject constructor(
    private val db: LocalDatabase,
    @IoDispatcher private val ioDispatchers: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatchers: CoroutineDispatcher
) {
    private val queries = db.audioAttributesQueries

    suspend fun insert(audioAttributes: AudioAttributes): Long = withContext(ioDispatchers) {
        db.transactionWithResult {
            queries.insert(
                name = audioAttributes.name,
                durationMillis = audioAttributes.durationMillis,
                path = audioAttributes.audioString,
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

    suspend fun getAudioAttributesByPath(path: String): AudioAttributes? =
        withContext(ioDispatchers) {
            val selected = queries.getAudioAttributesByPath(path).executeAsOneOrNull()
                ?: return@withContext null

            return@withContext withContext(defaultDispatchers) { selected.toDomainModel() }
        }

    suspend fun getAudioAttributesById(id: Long): AudioAttributes? = withContext(ioDispatchers) {
        val selected =
            queries.getAudioAttributesById(id).executeAsOneOrNull() ?: return@withContext null

        return@withContext withContext(defaultDispatchers) { selected.toDomainModel() }
    }
}