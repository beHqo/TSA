package com.thestrikingarts.domain.workout

import com.thestrikingarts.domain.model.Workout
import javax.inject.Inject

class UpsertWorkoutUseCase @Inject constructor(private val repository: WorkoutCacheRepository) {

    suspend operator fun invoke(workout: Workout, comboIdList: List<Long>) {
        if (workout.id == 0L) repository.insert(workout, comboIdList)
        else repository.update(workout, comboIdList)
    }
}