package com.example.android.strikingarts.domain.usecase.workout

import com.example.android.strikingarts.domain.interfaces.WorkoutCacheRepository
import javax.inject.Inject

class RetrieveWorkoutListUseCase @Inject constructor(repository: WorkoutCacheRepository) {
    val workoutList = repository.workoutList
}