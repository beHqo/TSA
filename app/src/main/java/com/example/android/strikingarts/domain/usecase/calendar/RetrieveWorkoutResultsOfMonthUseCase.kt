package com.example.android.strikingarts.domain.usecase.calendar

import com.example.android.strikingarts.domain.interfaces.WorkoutResultCacheRepository
import com.example.android.strikingarts.domain.model.WorkoutResult
import javax.inject.Inject

class RetrieveWorkoutResultsOfMonthUseCase @Inject constructor(
    private val retrieveEpochDayForFirstAndLastDayOfMonth: RetrieveEpochDayForFirstAndLastDayOfMonth,
    private val repository: WorkoutResultCacheRepository
) {
    suspend operator fun invoke(beforeOrAfterCurrentMonth: Long): List<WorkoutResult> {
        val monthBounds = retrieveEpochDayForFirstAndLastDayOfMonth(beforeOrAfterCurrentMonth)

        return repository.getWorkoutResultsInRange(monthBounds.first, monthBounds.second)
    }
}