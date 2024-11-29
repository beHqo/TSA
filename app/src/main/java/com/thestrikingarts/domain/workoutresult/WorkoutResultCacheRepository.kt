package com.thestrikingarts.domain.workoutresult

import com.thestrikingarts.domain.model.WorkoutConclusion
import com.thestrikingarts.domain.model.WorkoutResult

interface WorkoutResultCacheRepository {
    suspend fun lastSuccessfulWorkoutResult(): WorkoutResult?
    suspend fun lastFailedWorkoutResult(): WorkoutResult?
    suspend fun getWorkoutResultsInRange(
        fromEpochDay: Long, toEpochDay: Long
    ): List<WorkoutResult>
    suspend fun getWorkoutResultsByDate(epochDay: Long): List<WorkoutResult>
    suspend fun insert(workoutResult: WorkoutResult)
    suspend fun update(workoutResultId: Long, workoutConclusion: WorkoutConclusion): Long
}