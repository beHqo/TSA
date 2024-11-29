package com.thestrikingarts.data.local.dao

import com.thestrikingarts.LocalDatabase
import com.thestrikingarts.data.mapper.toDomainModel
import com.thestrikingarts.di.DefaultDispatcher
import com.thestrikingarts.di.IoDispatcher
import com.thestrikingarts.domain.model.WorkoutConclusion
import com.thestrikingarts.domain.model.WorkoutResult
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
                isWorkoutAborted = workoutResult.conclusion,
                trainingDateEpochDay = workoutResult.epochDay
            )

            return@transactionWithResult queries.lastInsertedRowId().executeAsOne()
        }
    }

    suspend fun getLastSuccessfulWorkoutResult(): WorkoutResult? = withContext(ioDispatchers) {
        val workoutResultTable =
            queries.getLastSuccessfulWorkoutResult().executeAsOneOrNull() ?: return@withContext null

        return@withContext withContext(defaultDispatcher) { workoutResultTable.toDomainModel() }
    }

    suspend fun getLastFailedWorkoutResult(): WorkoutResult? = withContext(ioDispatchers) {
        val workoutResultTable =
            queries.getLastFailedWorkoutResult().executeAsOneOrNull() ?: return@withContext null

        return@withContext withContext(defaultDispatcher) { workoutResultTable.toDomainModel() }
    }

    suspend fun getWorkoutResultsByDate(epochDay: Long): List<WorkoutResult> =
        withContext(ioDispatchers) {
            val workoutResultTableList = queries.getWorkoutResultsByDate(epochDay).executeAsList()

            if (workoutResultTableList.isEmpty()) return@withContext emptyList()

            return@withContext withContext(defaultDispatcher) {
                workoutResultTableList.map { it.toDomainModel() }
            }
        }

    suspend fun getWorkoutResultsInRange(
        fromEpochDate: Long, toEpochDate: Long
    ): List<WorkoutResult> = withContext(ioDispatchers) {
        val workoutResultTableList =
            queries.getWorkoutResultsInRange(fromEpochDate, toEpochDate).executeAsList()

        if (workoutResultTableList.isEmpty()) return@withContext emptyList()

        return@withContext withContext(defaultDispatcher) {
            workoutResultTableList.map { it.toDomainModel() }
        }
    }

    suspend fun update(
        workoutResultId: Long, workoutConclusion: WorkoutConclusion
    ): Long = withContext(ioDispatchers) {
        queries.transactionWithResult {
            queries.update(workoutConclusion, workoutResultId)

            return@transactionWithResult queries.affectedRows().executeAsOne()
        }
    }
}