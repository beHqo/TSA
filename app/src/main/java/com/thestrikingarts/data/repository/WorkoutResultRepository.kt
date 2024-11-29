package com.thestrikingarts.data.repository

import android.util.Log
import com.thestrikingarts.data.local.dao.WorkoutResultDao
import com.thestrikingarts.domain.logger.DataLogger
import com.thestrikingarts.domain.model.WorkoutConclusion
import com.thestrikingarts.domain.model.WorkoutResult
import com.thestrikingarts.domain.workoutresult.WorkoutResultCacheRepository
import javax.inject.Inject

private const val TAG = "WorkoutResultRepository"

class WorkoutResultRepository @Inject constructor(private val workoutResultDao: WorkoutResultDao) :
    WorkoutResultCacheRepository {
    private val logger = DataLogger(TAG)

    override suspend fun lastSuccessfulWorkoutResult(): WorkoutResult? =
        workoutResultDao.getLastSuccessfulWorkoutResult()

    override suspend fun lastFailedWorkoutResult(): WorkoutResult? =
        workoutResultDao.getLastFailedWorkoutResult()

    override suspend fun insert(workoutResult: WorkoutResult) {
        val id = workoutResultDao.insert(workoutResult)

        logger.logInsertOperation(id, workoutResult)
    }

    override suspend fun getWorkoutResultsInRange(
        fromEpochDay: Long, toEpochDay: Long
    ): List<WorkoutResult> {
        val workoutResults = workoutResultDao.getWorkoutResultsInRange(fromEpochDay, toEpochDay)

        return workoutResults.ifEmpty {
            Log.e(
                TAG,
                "getWorkoutResultsInRange: Failed to retrieve any objects in the given range:\nFrom $fromEpochDay, To $toEpochDay"
            )
            emptyList()
        }
    }

    override suspend fun getWorkoutResultsByDate(epochDay: Long): List<WorkoutResult> {
        val workoutResults = workoutResultDao.getWorkoutResultsByDate(epochDay)

        return workoutResults.ifEmpty {
            logger.logRetrieveOperation(epochDay, "getWorkoutResultsByDate")
            emptyList()
        }
    }

    override suspend fun update(workoutResultId: Long, workoutConclusion: WorkoutConclusion): Long {
        val affectedRows = workoutResultDao.update(workoutResultId, workoutConclusion)

        logger.logUpdateOperation(affectedRows, workoutResultId, null)

        return affectedRows
    }
}