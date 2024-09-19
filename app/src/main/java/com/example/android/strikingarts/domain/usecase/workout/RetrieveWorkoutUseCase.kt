package com.example.android.strikingarts.domain.usecase.workout

import com.example.android.strikingarts.domain.interfaces.WorkoutCacheRepository
import com.example.android.strikingarts.domain.model.Workout
import javax.inject.Inject

class RetrieveWorkoutUseCase @Inject constructor(private val repository: WorkoutCacheRepository) {

    suspend operator fun invoke(id: Long): Workout? = repository.getWorkout(id)
}