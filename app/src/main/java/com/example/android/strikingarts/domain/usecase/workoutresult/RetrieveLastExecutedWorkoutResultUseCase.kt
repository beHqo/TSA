package com.example.android.strikingarts.domain.usecase.workoutresult

import com.example.android.strikingarts.domain.interfaces.WorkoutResultCacheRepository
import com.example.android.strikingarts.domain.model.WorkoutResult
import javax.inject.Inject

class RetrieveLastExecutedWorkoutResultUseCase @Inject constructor(
    private val workoutConclusionRepository: WorkoutResultCacheRepository
) {
    suspend fun successful(): WorkoutResult? =
        workoutConclusionRepository.lastSuccessfulWorkoutResult()

    suspend fun failed(): WorkoutResult? = workoutConclusionRepository.lastFailedWorkoutResult()
}