package com.github.tsa.domain.calendar

import com.github.tsa.domain.model.WorkoutResult
import com.github.tsa.domain.workoutresult.WorkoutResultCacheRepository
import com.github.tsa.domainandroid.usecase.javatime.RetrieveEpochDayForFirstAndLastDayOfMonth
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