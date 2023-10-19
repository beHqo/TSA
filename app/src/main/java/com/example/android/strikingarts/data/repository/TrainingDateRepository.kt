package com.example.android.strikingarts.data.repository

import android.util.Log
import com.example.android.strikingarts.data.local.room.dao.TrainingDateDao
import com.example.android.strikingarts.data.local.room.model.TrainingDate
import com.example.android.strikingarts.domain.common.logger.DataLogger
import com.example.android.strikingarts.domain.interfaces.TrainingDateCacheRepository
import com.example.android.strikingarts.domain.mapper.toDomainModel
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.TrainingDay
import com.example.android.strikingarts.domain.model.toImmutableList
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "TrainingDateRepository"

@Singleton
class TrainingDateRepository @Inject constructor(private val trainingDateDao: TrainingDateDao) :
    TrainingDateCacheRepository {
    private val logger = DataLogger(TAG)

    override suspend fun getTrainingDaysInRange(
        fromEpochDay: Long, toEpochDay: Long
    ): ImmutableList<TrainingDay> {
        val list =
            trainingDateDao.getTrainingDatesWithWorkoutConclusionsInRange(fromEpochDay, toEpochDay)

        if (list.isEmpty()) Log.e(
            TAG,
            "trainingDaysInRange: Failed to retrieve any objects in the given range:\nFrom $fromEpochDay, To $toEpochDay"
        )

        return list.map { it.toDomainModel() }.toImmutableList()
    }

    override suspend fun getTrainingDate(epochDay: Long): TrainingDay {
        val trainingDateWithWorkoutConclusion =
            trainingDateDao.getTrainingDateWithWorkoutConclusions(epochDay)

        return if (trainingDateWithWorkoutConclusion == null) {
            logger.logRetrieveOperation(epochDay, "getTrainingDay")
            TrainingDay()
        } else trainingDateWithWorkoutConclusion.toDomainModel()
    }

    override suspend fun insert(epochDay: Long) {
        val id = trainingDateDao.insert(TrainingDate(epochDay))

        logger.logInsertOperation(id, epochDay)
    }
}