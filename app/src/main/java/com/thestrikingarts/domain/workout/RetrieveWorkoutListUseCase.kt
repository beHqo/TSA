package com.thestrikingarts.domain.workout

import javax.inject.Inject

class RetrieveWorkoutListUseCase @Inject constructor(repository: WorkoutCacheRepository) {
    val workoutList = repository.workoutList
}