package com.example.android.strikingarts.domain.interfaces

import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.ImmutableMap
import kotlinx.coroutines.flow.Flow

interface TrainingDateCacheRepository {
    val trainingDateByMonthMap: Flow<ImmutableMap<Long, ImmutableList<Long>>>

    suspend fun insert(trainingDatePair: Pair<Long, ImmutableList<Long>>)

    suspend fun update(trainingDatePair: Pair<Long, ImmutableList<Long>>)

    fun setMonthBounds(monthBounds: Pair<Long, Long>)
}