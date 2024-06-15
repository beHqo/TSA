package com.example.android.strikingarts.data.repository

import com.example.android.strikingarts.data.workoutResultList
import com.example.android.strikingarts.domain.interfaces.WorkoutResultCacheRepository
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.WorkoutResult
import com.example.android.strikingarts.domain.model.toImmutableList

class FakeWorkoutResultRepository : WorkoutResultCacheRepository {
    private val data = workoutResultList.toMutableList()

    override suspend fun lastSuccessfulWorkoutResult(): WorkoutResult? =
        data.filter { !it.isWorkoutAborted }.maxByOrNull { it.epochDay }


    override suspend fun lastFailedWorkoutResult(): WorkoutResult? =
        data.filter { it.isWorkoutAborted }.maxByOrNull { it.epochDay }

    override suspend fun getWorkoutResultsInRange(
        fromEpochDay: Long,
        toEpochDay: Long
    ): ImmutableList<WorkoutResult> =
        data.filter { it.epochDay in fromEpochDay..toEpochDay }.toImmutableList()

    override suspend fun getWorkoutResultsByDate(epochDay: Long): ImmutableList<WorkoutResult> =
        data.filter { it.epochDay == epochDay }.toImmutableList()

    override suspend fun insert(workoutResult: WorkoutResult) {
        data += workoutResult
    }
}