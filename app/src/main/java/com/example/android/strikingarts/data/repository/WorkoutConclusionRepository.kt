package com.example.android.strikingarts.data.repository

import com.example.android.strikingarts.data.local.room.dao.WorkoutConclusionDao
import com.example.android.strikingarts.domain.common.logger.DataLogger
import com.example.android.strikingarts.domain.interfaces.WorkoutConclusionCacheRepository
import com.example.android.strikingarts.domain.mapper.toDataModel
import com.example.android.strikingarts.domain.mapper.toDomainModel
import com.example.android.strikingarts.domain.model.WorkoutResult
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
}