package com.example.android.strikingarts.domain.usecase.winners

import com.example.android.strikingarts.domain.interfaces.TrainingDateCacheRepository
import com.example.android.strikingarts.domain.model.toImmutableList
import com.example.android.strikingarts.domain.usecase.javatime.GetEpochDayForToday
import javax.inject.Inject

class InsertTrainingDateUseCase @Inject constructor(
    private val repository: TrainingDateCacheRepository,
    private val getEpochDayForToday: GetEpochDayForToday
) {
    suspend operator fun invoke(workoutId: Long) {
        val todayEpochDay = getEpochDayForToday()
        val todayTrainingDateOrEmpty = repository.getTrainingDay(todayEpochDay)

        if (todayTrainingDateOrEmpty.first == 0L) repository.insert(
            Pair(todayEpochDay, listOf(workoutId).toImmutableList())
        )
        else repository.update(
            todayTrainingDateOrEmpty.copy(second = (todayTrainingDateOrEmpty.second + workoutId).toImmutableList())
        )
    }
}