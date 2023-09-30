package com.example.android.strikingarts.domain.interfaces

import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.ImmutableMap
import kotlinx.coroutines.flow.Flow

interface TrainingDateCacheRepository {
    val trainingDatesInRange: Flow<ImmutableMap<Long, ImmutableList<Long>>>

    suspend fun insert(trainingDatePair: Pair<Long, ImmutableList<Long>>)

    suspend fun update(trainingDatePair: Pair<Long, ImmutableList<Long>>)

    suspend fun getEpochDaysOfTrainingDatesInRange(
        fromEpochDay: Long, toEpochDay: Long
    ): ImmutableList<Long>

    suspend fun retrieveLastTrainingDate(): Pair<Long, ImmutableList<Long>>

    fun setDateBounds(dateBounds: Pair<Long, Long>)
}