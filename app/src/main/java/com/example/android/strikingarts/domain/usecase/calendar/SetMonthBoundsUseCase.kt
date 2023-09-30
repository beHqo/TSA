package com.example.android.strikingarts.domain.usecase.calendar

import com.example.android.strikingarts.domain.interfaces.TrainingDateCacheRepository
import javax.inject.Inject

class SetMonthBoundsUseCase @Inject constructor(
    private val repository: TrainingDateCacheRepository,
    private val retrieveEpochDayForFirstAndLastDayOfMonth: RetrieveEpochDayForFirstAndLastDayOfMonth
) {
    operator fun invoke(beforeOrAfter: Long) {
        val monthDates = retrieveEpochDayForFirstAndLastDayOfMonth(beforeOrAfter)

        repository.setDateBounds(monthDates)
    }
}