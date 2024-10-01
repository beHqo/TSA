package com.example.android.strikingarts.domain.calendar

import com.example.android.strikingarts.domain.model.WorkoutResult
import com.example.android.strikingarts.domain.workoutresult.WorkoutResultCacheRepository
import com.example.android.strikingarts.domainandroid.usecase.javatime.RetrieveEpochDayForFirstAndLastDayOfMonth
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