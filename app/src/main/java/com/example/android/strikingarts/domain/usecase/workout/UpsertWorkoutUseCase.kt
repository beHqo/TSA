package com.example.android.strikingarts.domain.usecase.workout

import com.example.android.strikingarts.domain.interfaces.WorkoutCacheRepository
import com.example.android.strikingarts.domain.model.Workout
import javax.inject.Inject

class UpsertWorkoutUseCase @Inject constructor(private val repository: WorkoutCacheRepository) {

    suspend operator fun invoke(workoutListItem: Workout, comboIdList: List<Long>) {
        if (workoutListItem.id == 0L) repository.insert(workoutListItem, comboIdList)
        else repository.update(workoutListItem, comboIdList)
    }
}