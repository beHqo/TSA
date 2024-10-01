package com.example.android.strikingarts.domainandroid.usecase.javatime

import com.example.android.strikingarts.domain.model.WorkoutResult
import com.example.android.strikingarts.domain.workoutresult.WorkoutResultCacheRepository
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