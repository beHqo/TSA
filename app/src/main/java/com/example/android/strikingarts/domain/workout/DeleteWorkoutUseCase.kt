package com.example.android.strikingarts.domain.workout

import javax.inject.Inject

class DeleteWorkoutUseCase @Inject constructor(private val repository: WorkoutCacheRepository) {

    suspend operator fun invoke(id: Long): Long = repository.delete(id)

    suspend operator fun invoke(idList: List<Long>): Long = repository.deleteAll(idList)
}