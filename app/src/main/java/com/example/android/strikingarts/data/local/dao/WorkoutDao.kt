package com.example.android.strikingarts.data.local.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.android.strikingarts.LocalDatabase
import com.example.android.strikingarts.data.local.mapper.toDomainModel
import com.example.android.strikingarts.domain.model.Workout
import com.example.android.strikingarts.hilt.module.DefaultDispatcher
import com.example.android.strikingarts.hilt.module.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import tables.Combo_table
import tables.GetTechnique
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutDao @Inject constructor(
    private val db: LocalDatabase,
    @IoDispatcher private val ioDispatchers: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatchers: CoroutineDispatcher
) {
    val workoutList: Flow<List<Workout>> =
        db.workoutQueries.getWorkoutList().asFlow().mapToList(ioDispatchers).map { list ->
            list.map innerMap@{ workoutTable ->
                getWorkoutListItem(workoutTable.workout_id) ?: Workout()
            }
        }

    suspend fun getWorkoutListItem(workoutId: Long): Workout? = withContext(ioDispatchers) {
        val workoutTable =
            db.workoutQueries.getWorkout(workoutId).executeAsOneOrNull() ?: return@withContext null

        val comboIdList = db.workoutComboRefQueries.getComboIds(workoutId).executeAsList()

        if (comboIdList.isEmpty()) return@withContext workoutTable.toDomainModel()

        val comboTableList = mutableListOf<Combo_table>()
        for (comboId in comboIdList) comboTableList += db.comboQueries.getCombo(comboId)
            .executeAsOne()

        val techniqueTableList = List(comboIdList.size) { index ->
            val techniqueIdList =
                db.comboTechniqueRefQueries.getTechniqueIds(comboIdList[index]).executeAsList()

            val techniqueList = mutableListOf<GetTechnique>()
            for (id in techniqueIdList) techniqueList += db.techniqueQueries.getTechnique(id)
                .executeAsOne()

            return@List techniqueList
        }

        return@withContext withContext(defaultDispatchers) {
            val comboListItems = List(comboTableList.size) { index ->
                comboTableList[index].toDomainModel(techniqueTableList[index].map { it.toDomainModel() }
                )
            }

            workoutTable.toDomainModel(comboListItems)
        }
    }

    suspend fun insert(workoutListItem: Workout, comboIdList: List<Long>): Long =
        withContext(ioDispatchers) {
            db.transactionWithResult {
                var insertedRowId: Long = -1

                db.transaction {
                    db.workoutQueries.insert(
                        name = workoutListItem.name,
                        rounds = workoutListItem.rounds,
                        roundLengthSeconds = workoutListItem.roundLengthSeconds,
                        restLengthSeconds = workoutListItem.restLengthSeconds,
                        subRounds = workoutListItem.subRounds
                    )

                    insertedRowId = db.workoutQueries.lastInsertedRowId().executeAsOne()

                    insertWorkoutComboRefs(insertedRowId, comboIdList)
                }

                return@transactionWithResult insertedRowId
            }
        }

    suspend fun update(workoutListItem: Workout, comboIdList: List<Long>): Long =
        withContext(ioDispatchers) {
            db.transactionWithResult {
                var affectedRows = 0L

                db.transaction {
                    db.workoutQueries.update(
                        id = workoutListItem.id,
                        name = workoutListItem.name,
                        rounds = workoutListItem.rounds,
                        roundLengthSeconds = workoutListItem.roundLengthSeconds,
                        restLengthSeconds = workoutListItem.restLengthSeconds,
                        subRounds = workoutListItem.subRounds
                    )

                    affectedRows =
                        db.workoutQueries.affectedRow().executeAsOneOrNull() ?: return@transaction

                    db.workoutComboRefQueries.delete(workoutListItem.id)

                    insertWorkoutComboRefs(workoutListItem.id, comboIdList)
                }

                return@transactionWithResult affectedRows
            }
        }

    private fun insertWorkoutComboRefs(workoutId: Long, comboIdList: List<Long>): List<Long> {
        val insertedRowIdList = mutableListOf<Long>()

        for (comboId in comboIdList) {
            db.workoutComboRefQueries.insert(workoutId, comboId)

            insertedRowIdList += db.workoutComboRefQueries.lastInsertedRowId().executeAsOne()
        }

        return insertedRowIdList
    }

    suspend fun delete(workoutId: Long): Long = withContext(ioDispatchers) {
        var affectedRows = 0L

        db.transactionWithResult {
            db.workoutQueries.delete(workoutId)

            affectedRows = db.workoutQueries.affectedRow().executeAsOne()
                .also { if (it == 0L) return@transactionWithResult }
        }

        return@withContext affectedRows
    }

    suspend fun deleteAll(idList: List<Long>): Long = withContext(ioDispatchers) {
        var affectedRows = 0L

        db.transactionWithResult {
            db.workoutQueries.deleteAll(idList)

            affectedRows = db.workoutQueries.affectedRow().executeAsOne()
                .also { if (it == 0L) return@transactionWithResult }
        }

        return@withContext affectedRows
    }
}