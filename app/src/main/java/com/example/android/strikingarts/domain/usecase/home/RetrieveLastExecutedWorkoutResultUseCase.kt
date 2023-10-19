package com.example.android.strikingarts.domain.usecase.home

import com.example.android.strikingarts.domain.interfaces.WorkoutConclusionCacheRepository
import com.example.android.strikingarts.domain.model.WorkoutResult
import javax.inject.Inject

class RetrieveLastExecutedWorkoutResultUseCase @Inject constructor(
    private val workoutConclusionRepository: WorkoutConclusionCacheRepository
) {
    suspend fun successful(): WorkoutResult? =
        workoutConclusionRepository.lastSuccessfulWorkoutResult()

    suspend fun failed(): WorkoutResult? = workoutConclusionRepository.lastFailedWorkoutResult()
}