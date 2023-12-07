package com.example.android.strikingarts.data.repository

import android.util.Log
import com.example.android.strikingarts.data.local.room.dao.WorkoutConclusionDao
import com.example.android.strikingarts.domain.common.logger.DataLogger
import com.example.android.strikingarts.domain.interfaces.WorkoutConclusionCacheRepository
import com.example.android.strikingarts.domain.mapper.toDataModel
import com.example.android.strikingarts.domain.mapper.toDomainModel
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.WorkoutResult
import com.example.android.strikingarts.domain.model.toImmutableList
import javax.inject.Inject

private const val TAG = "WorkoutConclusionRepo"

class WorkoutConclusionRepository @Inject constructor(private val workoutConclusionDao: WorkoutConclusionDao) :
    WorkoutConclusionCacheRepository {
    private val logger = DataLogger(TAG)

    override suspend fun lastSuccessfulWorkoutResult(): WorkoutResult? =
        workoutConclusionDao.getLastSuccessfulWorkoutConclusion()?.toDomainModel()

    override suspend fun lastFailedWorkoutResult(): WorkoutResult? =
        workoutConclusionDao.getLastFailedWorkoutConclusion()?.toDomainModel()

    override suspend fun insert(workoutResult: WorkoutResult) {
        val id = workoutConclusionDao.insert(workoutResult.toDataModel())

        logger.logInsertOperation(id, workoutResult)
    }

    override suspend fun getWorkoutResultsInRange(
        fromEpochDay: Long, toEpochDay: Long
    ): ImmutableList<WorkoutResult> {
        val workoutResults =
            workoutConclusionDao.getWorkoutConclusionsInRange(fromEpochDay, toEpochDay)

        return if (workoutResults.isEmpty()) {
            Log.e(
                TAG,
                "getWorkoutResultsInRange: Failed to retrieve any objects in the given range:\nFrom $fromEpochDay, To $toEpochDay"
            )
            ImmutableList()
        } else workoutResults.map { it.toDomainModel() }.toImmutableList()
    }

    override suspend fun getWorkoutResultsByDate(epochDay: Long): ImmutableList<WorkoutResult> {
        val workoutResults = workoutConclusionDao.getWorkoutConclusionByDate(epochDay)

        return if (workoutResults.isEmpty()) {
            logger.logRetrieveOperation(epochDay, "getWorkoutResultsByDate")
            ImmutableList()
        } else workoutResults.map { it.toDomainModel() }.toImmutableList()
    }
}