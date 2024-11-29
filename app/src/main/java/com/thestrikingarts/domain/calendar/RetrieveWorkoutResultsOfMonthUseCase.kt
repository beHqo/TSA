package com.thestrikingarts.domain.calendar

import com.thestrikingarts.domain.model.WorkoutResult
import com.thestrikingarts.domain.workoutresult.WorkoutResultCacheRepository
import com.thestrikingarts.domainandroid.usecase.javatime.RetrieveEpochDayForFirstAndLastDayOfMonth
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