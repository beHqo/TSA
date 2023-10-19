package com.example.android.strikingarts.domain.interfaces

import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.TrainingDay

interface TrainingDateCacheRepository {
    suspend fun getTrainingDaysInRange(
        fromEpochDay: Long,
        toEpochDay: Long
    ): ImmutableList<TrainingDay>

    suspend fun getTrainingDate(epochDay: Long): TrainingDay

    suspend fun insert(epochDay: Long)
}