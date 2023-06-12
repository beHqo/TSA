package com.example.android.strikingarts.domain.usecase.workout

import com.example.android.strikingarts.domain.interfaces.WorkoutCacheRepository
import com.example.android.strikingarts.domain.model.WorkoutListItem
import javax.inject.Inject

class UpsertWorkoutUseCase @Inject constructor(private val repository: WorkoutCacheRepository) {

    suspend operator fun invoke(workoutListItem: WorkoutListItem, comboIdList: List<Long>) {
        if (workoutListItem.id == 0L) repository.insertComboTechniqueCrossRef(
            workoutListItem, comboIdList
        ) else repository.updateComboTechniqueCrossRef(workoutListItem, comboIdList)
    }
}