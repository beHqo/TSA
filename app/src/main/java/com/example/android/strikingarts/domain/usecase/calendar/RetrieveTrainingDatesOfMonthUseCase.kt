package com.example.android.strikingarts.domain.usecase.calendar

import com.example.android.strikingarts.domain.interfaces.TrainingDateCacheRepository
import javax.inject.Inject

class RetrieveTrainingDatesOfMonthUseCase @Inject constructor(
    repository: TrainingDateCacheRepository
) {
    val trainingDateMapByMonth = repository.trainingDatesInRange
}