package com.thestrikingarts.domainandroid.usecase.javatime

import com.thestrikingarts.domain.model.WorkoutResult
import com.thestrikingarts.domain.workoutresult.WorkoutResultCacheRepository
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