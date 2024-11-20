package com.github.tsa.domain.workout

import com.github.tsa.domain.model.Workout
import javax.inject.Inject

class RetrieveWorkoutUseCase @Inject constructor(private val repository: WorkoutCacheRepository) {

    suspend operator fun invoke(id: Long): Workout? = repository.getWorkout(id)
}