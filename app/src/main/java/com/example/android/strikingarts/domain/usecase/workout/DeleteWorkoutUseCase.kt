package com.example.android.strikingarts.domain.usecase.workout

import com.example.android.strikingarts.domain.repository.WorkoutCacheRepository
import javax.inject.Inject

class DeleteWorkoutUseCase @Inject constructor(private val repository: WorkoutCacheRepository) {

    suspend operator fun invoke(id: Long) {
        repository.delete(id)
    }

    suspend operator fun invoke(idList: List<Long>) {
        repository.deleteAll(idList)
    }
}