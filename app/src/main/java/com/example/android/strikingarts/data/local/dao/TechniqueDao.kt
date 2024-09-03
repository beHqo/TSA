package com.example.android.strikingarts.data.local.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.android.strikingarts.LocalDatabase
import com.example.android.strikingarts.data.local.mapper.toDomainModel
import com.example.android.strikingarts.domain.common.constants.transparentHexCode
import com.example.android.strikingarts.domain.model.MovementType
import com.example.android.strikingarts.domain.model.Technique
import com.example.android.strikingarts.hilt.module.DefaultDispatcher
import com.example.android.strikingarts.hilt.module.IoDispatcher
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

    suspend fun insert(techniqueListItem: Technique, audioAttributesId: Long?): Long =
        withContext(ioDispatchers) {
            db.transactionWithResult {
                techniqueQueries.insert(name = techniqueListItem.name,
                    num = techniqueListItem.num,
                    isOffense = techniqueListItem.movementType == MovementType.OFFENSE,
                    techniqueTypeName = techniqueListItem.techniqueType.name,
                    audioAttributes = audioAttributesId,
                    color = techniqueListItem.color.let { if (it == transparentHexCode) null else it })

                return@transactionWithResult techniqueQueries.lastInsertedRowId().executeAsOne()
            }
        }

    suspend fun update(technique: Technique, audioAttributesId: Long?): Long =
        withContext(ioDispatchers) {
            db.transactionWithResult {
                techniqueQueries.update(techniqueId = technique.id,
                    name = technique.name,
                    number = technique.num,
                    movementType = technique.movementType == MovementType.OFFENSE,
                    techniqueTypeName = technique.techniqueType.name,
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