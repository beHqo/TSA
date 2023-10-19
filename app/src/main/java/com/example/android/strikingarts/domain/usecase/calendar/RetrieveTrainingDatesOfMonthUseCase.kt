package com.example.android.strikingarts.domain.usecase.calendar

import com.example.android.strikingarts.domain.interfaces.TrainingDateCacheRepository
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.TrainingDay
import javax.inject.Inject

class RetrieveTrainingDatesOfMonthUseCase @Inject constructor(
    private val retrieveEpochDayForFirstAndLastDayOfMonth: RetrieveEpochDayForFirstAndLastDayOfMonth,
    private val repository: TrainingDateCacheRepository
) {
    suspend operator fun invoke(beforeOrAfterCurrentMonth: Long): ImmutableList<TrainingDay> {
        val monthBounds = retrieveEpochDayForFirstAndLastDayOfMonth(beforeOrAfterCurrentMonth)

        return repository.getTrainingDaysInRange(monthBounds.first, monthBounds.second)
    }
}