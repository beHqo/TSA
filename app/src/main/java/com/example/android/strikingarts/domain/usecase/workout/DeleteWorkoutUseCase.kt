package com.example.android.strikingarts.domain.usecase.workout

import com.example.android.strikingarts.domain.interfaces.WorkoutCacheRepository
import javax.inject.Inject

class DeleteWorkoutUseCase @Inject constructor(private val repository: WorkoutCacheRepository) {

    suspend operator fun invoke(id: Long): Long = repository.delete(id)

    suspend operator fun invoke(idList: List<Long>): Long = repository.deleteAll(idList)
}