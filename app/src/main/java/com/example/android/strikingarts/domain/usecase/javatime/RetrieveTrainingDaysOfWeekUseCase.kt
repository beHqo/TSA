package com.example.android.strikingarts.domain.usecase.javatime

import com.example.android.strikingarts.domain.interfaces.TrainingDateCacheRepository
import com.example.android.strikingarts.domain.model.ImmutableList
import javax.inject.Inject

class RetrieveTrainingDaysOfWeekUseCase @Inject constructor(
    private val repository: TrainingDateCacheRepository,
    private val getCurrentWeekBoundsEpochDay: GetCurrentWeekBoundsEpochDay
) {
    suspend operator fun invoke(): ImmutableList<Long> {
        val range = getCurrentWeekBoundsEpochDay()

        return repository.getEpochDaysOfTrainingDatesInRange(range.first, range.second)
    }
}