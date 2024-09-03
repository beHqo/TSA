package com.example.android.strikingarts.domain.usecase.javatime

import com.example.android.strikingarts.domain.interfaces.WorkoutResultCacheRepository
import com.example.android.strikingarts.domain.model.WorkoutResult
import javax.inject.Inject

class RetrieveWorkoutResultsOfWeekUseCase @Inject constructor(
    private val repository: WorkoutResultCacheRepository,
    private val getCurrentWeekBoundsEpochDay: GetCurrentWeekBoundsEpochDay
) {
    suspend operator fun invoke(): List<WorkoutResult> {
        val range = getCurrentWeekBoundsEpochDay()

        return repository.getWorkoutResultsInRange(range.first, range.second)
    }
}