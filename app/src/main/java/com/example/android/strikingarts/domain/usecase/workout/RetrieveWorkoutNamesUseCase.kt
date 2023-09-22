package com.example.android.strikingarts.domain.usecase.workout

import com.example.android.strikingarts.domain.interfaces.WorkoutCacheRepository
import com.example.android.strikingarts.domain.model.ImmutableList
import javax.inject.Inject

class RetrieveWorkoutNamesUseCase @Inject constructor(private val repository: WorkoutCacheRepository) {
    suspend operator fun invoke(idList: List<Long>): ImmutableList<String> =
        repository.getWorkoutNames(idList)
}