package com.github.tsa.data.local.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.github.tsa.LocalDatabase
import com.github.tsa.data.mapper.toDomainModel
import com.github.tsa.di.DefaultDispatcher
import com.github.tsa.di.IoDispatcher
import com.github.tsa.domain.constant.transparentHexCode
import com.github.tsa.domain.model.Technique
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TechniqueDao @Inject constructor(
    private val db: LocalDatabase,
    @IoDispatcher private val ioDispatchers: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatchers: CoroutineDispatcher
) {
    private val techniqueQueries = db.techniqueQueries

    val getTechniqueList: Flow<List<Technique>> =
        techniqueQueries.getTechniqueList().asFlow().mapToList(ioDispatchers).map { selectedItems ->
            withContext(defaultDispatchers) {
                selectedItems.map { selected -> selected.toDomainModel() }
            }
        }

    suspend fun getTechnique(id: Long): Technique? = withContext(ioDispatchers) {
        val selected =
            techniqueQueries.getTechnique(id).executeAsOneOrNull() ?: return@withContext null

        return@withContext withContext(defaultDispatchers) { selected.toDomainModel() }
    }

    suspend fun insert(technique: Technique, audioAttributesId: Long?): Long =
        withContext(ioDispatchers) {
            db.transactionWithResult {
                techniqueQueries.insert(name = technique.name,
                    num = technique.num,
                    isOffense = technique.movementType,
                    techniqueTypeName = technique.techniqueType,
                    audioAttributes = audioAttributesId,
                    color = technique.color.let { if (it == transparentHexCode) null else it })

                return@transactionWithResult techniqueQueries.lastInsertedRowId().executeAsOne()
            }
        }

    suspend fun update(technique: Technique, audioAttributesId: Long?): Long =
        withContext(ioDispatchers) {
            db.transactionWithResult {
                techniqueQueries.update(techniqueId = technique.id,
                    name = technique.name,
                    number = technique.num,
                    movementType = technique.movementType,
                    techniqueTypeName = technique.techniqueType,
                    audioAttributesId = audioAttributesId,
                    color = technique.color.let { if (it == transparentHexCode) null else it })

                return@transactionWithResult techniqueQueries.affectedRow().executeAsOne()
            }
        }

    suspend fun delete(id: Long): Long = withContext(ioDispatchers) {
        db.transactionWithResult {
            techniqueQueries.delete(id)

            return@transactionWithResult techniqueQueries.affectedRow().executeAsOne()
        }
    }

    suspend fun deleteAll(idList: List<Long>): Long = withContext(ioDispatchers) {
        db.transactionWithResult {
            techniqueQueries.deleteAll(idList)

            return@transactionWithResult techniqueQueries.affectedRow().executeAsOne()
        }
    }
}