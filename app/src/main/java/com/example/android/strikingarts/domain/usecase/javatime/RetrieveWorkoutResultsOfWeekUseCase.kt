package com.example.android.strikingarts.domain.usecase.javatime

import com.example.android.strikingarts.domain.interfaces.WorkoutConclusionCacheRepository
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.WorkoutResult
import javax.inject.Inject

class RetrieveWorkoutResultsOfWeekUseCase @Inject constructor(
    private val repository: WorkoutConclusionCacheRepository,
    private val getCurrentWeekBoundsEpochDay: GetCurrentWeekBoundsEpochDay
) {
    suspend operator fun invoke(): ImmutableList<WorkoutResult> {
        val range = getCurrentWeekBoundsEpochDay()

        return repository.getWorkoutResultsInRange(range.first, range.second)
    }
}