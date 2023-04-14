package com.example.android.strikingarts.domain.usecase.workout

import com.example.android.strikingarts.domain.model.WorkoutListItem
import com.example.android.strikingarts.domain.repository.WorkoutCacheRepository
import javax.inject.Inject

class RetrieveWorkoutUseCase @Inject constructor(private val repository: WorkoutCacheRepository) {

    suspend operator fun invoke(id: Long): WorkoutListItem = repository.getWorkout(id)
}