package com.example.android.strikingarts.data.local.dao

import com.example.android.strikingarts.LocalDatabase
import com.example.android.strikingarts.data.local.mapper.toDomainModel
import com.example.android.strikingarts.domain.model.WorkoutResult
import com.example.android.strikingarts.hilt.module.DefaultDispatcher
import com.example.android.strikingarts.hilt.module.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutResultDao @Inject constructor(
    private val db: LocalDatabase,
    @IoDispatcher private val ioDispatchers: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    private val queries = db.workoutResultQueries

    suspend fun insert(workoutResult: WorkoutResult): Long = withContext(ioDispatchers) {
        db.transactionWithResult {
            queries.insert(
                workoutId = workoutResult.workoutId,
                workoutName = workoutResult.workoutName,
                isWorkoutAborted = workoutResult.isWorkoutAborted,
                trainingDateEpochDay = workoutResult.epochDay
            )

            return@transactionWithResult queries.lastInsertedRowId().executeAsOne()
        }
    }

    suspend fun getLastSuccessfulWorkoutResult(): WorkoutResult? = withContext(ioDispatchers) {
        val workoutConclusionTable =
            queries.getLastSuccessfulWorkoutConclusion().executeAsOneOrNull()
                ?: return@withContext null

        return@withContext withContext(defaultDispatcher) { workoutConclusionTable.toDomainModel() }
    }

    suspend fun getLastFailedWorkoutResult(): WorkoutResult? = withContext(ioDispatchers) {
        val workoutConclusionTable =
            queries.getLastFailedWorkoutConclusion().executeAsOneOrNull() ?: return@withContext null

        return@withContext withContext(defaultDispatcher) { workoutConclusionTable.toDomainModel() }
    }

    suspend fun getWorkoutResultsByDate(epochDay: Long): List<WorkoutResult> =
        withContext(ioDispatchers) {
            val workoutConclusionTableList =
                queries.getWorkoutConclusionByDate(epochDay).executeAsList()

            if (workoutConclusionTableList.isEmpty()) return@withContext emptyList()

            return@withContext withContext(defaultDispatcher) {
                workoutConclusionTableList.map { it.toDomainModel() }
            }
        }

    suspend fun getWorkoutResultsInRange(
        fromEpochDate: Long, toEpochDate: Long
    ): List<WorkoutResult> = withContext(ioDispatchers) {
        val workoutConclusionTableList =
            queries.getWorkoutConclusionsInRange(fromEpochDate, toEpochDate).executeAsList()

        if (workoutConclusionTableList.isEmpty()) return@withContext emptyList()

        return@withContext withContext(defaultDispatcher) {
            workoutConclusionTableList.map { it.toDomainModel() }
        }
    }
}