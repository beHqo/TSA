package com.example.android.strikingarts.domain.interfaces

import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.WorkoutResult

interface WorkoutConclusionCacheRepository {
    suspend fun lastSuccessfulWorkoutResult(): WorkoutResult?
    suspend fun lastFailedWorkoutResult(): WorkoutResult?
    suspend fun getWorkoutResultsInRange(
        fromEpochDay: Long, toEpochDay: Long
    ): ImmutableList<WorkoutResult>

    suspend fun getWorkoutResultsByDate(epochDay: Long): ImmutableList<WorkoutResult>
    suspend fun insert(workoutResult: WorkoutResult)
}