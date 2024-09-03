package com.example.android.strikingarts.domain.interfaces

import com.example.android.strikingarts.domain.model.WorkoutResult

interface WorkoutResultCacheRepository {
    suspend fun lastSuccessfulWorkoutResult(): WorkoutResult?
    suspend fun lastFailedWorkoutResult(): WorkoutResult?
    suspend fun getWorkoutResultsInRange(
        fromEpochDay: Long, toEpochDay: Long
    ): List<WorkoutResult>

    suspend fun getWorkoutResultsByDate(epochDay: Long): List<WorkoutResult>
    suspend fun insert(workoutResult: WorkoutResult)
}