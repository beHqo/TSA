package com.thestrikingarts.domain.workoutresult

import com.thestrikingarts.domain.model.WorkoutResult
import javax.inject.Inject

class RetrieveLastExecutedWorkoutResultUseCase @Inject constructor(
    private val workoutConclusionRepository: WorkoutResultCacheRepository
) {
    suspend fun successful(): WorkoutResult? =
        workoutConclusionRepository.lastSuccessfulWorkoutResult()

    suspend fun failed(): WorkoutResult? = workoutConclusionRepository.lastFailedWorkoutResult()
}