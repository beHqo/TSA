package com.github.tsa.data.local.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.github.tsa.LocalDatabase
import com.github.tsa.data.mapper.toDomainModel
import com.github.tsa.di.DefaultDispatcher
import com.github.tsa.di.IoDispatcher
import com.github.tsa.domain.model.Combo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import tables.GetTechnique
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComboDao @Inject constructor(
    private val db: LocalDatabase,
    @IoDispatcher private val ioDispatchers: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatchers: CoroutineDispatcher
) {
    val comboList: Flow<List<Combo>>
        get() = db.comboQueries.getComboList().asFlow().mapToList(ioDispatchers).map { list ->
            list.map innerMap@{ comboTable ->
                getComboWithTechniques(comboTable.comboId) ?: Combo()
            }
        }

    suspend fun getComboWithTechniques(comboId: Long): Combo? = withContext(ioDispatchers) {
        val comboTable =
            db.comboQueries.getCombo(comboId).executeAsOneOrNull() ?: return@withContext null

        val techniqueIdList = db.comboTechniqueRefQueries.getTechniqueIds(comboId).executeAsList()

        val techniqueList = mutableListOf<GetTechnique>()
        for (id in techniqueIdList) techniqueList += db.techniqueQueries.getTechnique(id)
            .executeAsOne()

        return@withContext withContext(defaultDispatchers) {
            comboTable.toDomainModel(techniqueList.map { it.toDomainModel() })
        }
    }

    suspend fun insert(combo: Combo, techniqueIdList: List<Long>): Long =
        withContext(ioDispatchers) {
            db.transactionWithResult {
                var comboId: Long = -1

                db.transaction {
                    db.comboQueries.insert(combo.name, combo.desc, combo.delayMillis)

                    comboId = db.comboQueries.lastInsertedRowId().executeAsOne()

                    insertComboTechniqueRefs(comboId, techniqueIdList)
                }

                return@transactionWithResult comboId
            }
        }

    suspend fun update(combo: Combo, techniqueIdList: List<Long>): Long =
        withContext(ioDispatchers) {
            db.transactionWithResult {
                var affectedRows = 0L

                db.transaction {
                    db.comboQueries.update(combo.name, combo.desc, combo.delayMillis, combo.id)

                    affectedRows = db.comboQueries.affectedRow().executeAsOne()
                    if (affectedRows == 0L) return@transaction

                    db.comboTechniqueRefQueries.delete(combo.id)

                    insertComboTechniqueRefs(combo.id, techniqueIdList)
                }

                return@transactionWithResult affectedRows
            }
        }

    private fun insertComboTechniqueRefs(comboId: Long, techniqueIdList: List<Long>): List<Long> {
        val insertedRowIdList = mutableListOf<Long>()

        for (techniqueId in techniqueIdList) {
            db.comboTechniqueRefQueries.insert(comboId, techniqueId)

            val recentlyInserted = db.comboTechniqueRefQueries.lastInsertedRowId().executeAsOne()

            insertedRowIdList += recentlyInserted
        }

        return insertedRowIdList
    }

    suspend fun delete(comboId: Long): Long = withContext(ioDispatchers) {
        db.transactionWithResult {
            db.comboQueries.delete(comboId)

            return@transactionWithResult db.comboTechniqueRefQueries.affectedRows().executeAsOne()
        }
    }

    suspend fun deleteAll(idList: List<Long>): Long = withContext(ioDispatchers) {
        db.transactionWithResult {
            db.comboQueries.deleteAll(idList)

            return@transactionWithResult db.comboQueries.affectedRow().executeAsOne()
        }
    }
}
