package com.example.android.strikingarts.data.local.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.android.strikingarts.LocalDatabase
import com.example.android.strikingarts.data.local.mapper.toDomainModel
import com.example.android.strikingarts.di.DefaultDispatcher
import com.example.android.strikingarts.di.IoDispatcher
import com.example.android.strikingarts.domain.model.Workout
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import tables.ComboTable
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
                getWorkout(workoutTable.workoutId) ?: Workout()
            }
        }

    suspend fun getWorkout(workoutId: Long): Workout? = withContext(ioDispatchers) {
        val workoutTable =
            db.workoutQueries.getWorkout(workoutId).executeAsOneOrNull() ?: return@withContext null

        val comboIdList = db.workoutComboRefQueries.getComboIds(workoutId).executeAsList()

        if (comboIdList.isEmpty()) return@withContext workoutTable.toDomainModel()

        val comboTableList = mutableListOf<ComboTable>()
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
            val combo = List(comboTableList.size) { index ->
                comboTableList[index].toDomainModel(techniqueTableList[index].map { it.toDomainModel() }
                )
            }

            workoutTable.toDomainModel(combo)
        }
    }

    suspend fun insert(workout: Workout, comboIdList: List<Long>): Long =
        withContext(ioDispatchers) {
            db.transactionWithResult {
                var insertedRowId: Long = -1

                db.transaction {
                    db.workoutQueries.insert(
                        name = workout.name,
                        rounds = workout.rounds,
                        roundLengthSeconds = workout.roundLengthSeconds,
                        restLengthSeconds = workout.restLengthSeconds,
                        subRounds = workout.subRounds
                    )

                    insertedRowId = db.workoutQueries.lastInsertedRowId().executeAsOne()

                    insertWorkoutComboRefs(insertedRowId, comboIdList)
                }

                return@transactionWithResult insertedRowId
            }
        }

    suspend fun update(workout: Workout, comboIdList: List<Long>): Long =
        withContext(ioDispatchers) {
            db.transactionWithResult {
                var affectedRows = 0L

                db.transaction {
                    db.workoutQueries.update(
                        id = workout.id,
                        name = workout.name,
                        rounds = workout.rounds,
                        roundLengthSeconds = workout.roundLengthSeconds,
                        restLengthSeconds = workout.restLengthSeconds,
                        subRounds = workout.subRounds
                    )

                    affectedRows =
                        db.workoutQueries.affectedRow().executeAsOneOrNull() ?: return@transaction

                    db.workoutComboRefQueries.delete(workout.id)

                    insertWorkoutComboRefs(workout.id, comboIdList)
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