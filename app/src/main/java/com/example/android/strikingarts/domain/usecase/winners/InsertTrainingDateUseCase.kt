package com.example.android.strikingarts.domain.usecase.winners

import com.example.android.strikingarts.domain.interfaces.TrainingDateCacheRepository
import com.example.android.strikingarts.domain.interfaces.WorkoutConclusionCacheRepository
import com.example.android.strikingarts.domain.model.WorkoutResult
import com.example.android.strikingarts.domain.usecase.javatime.GetEpochDayForToday
import javax.inject.Inject

class InsertTrainingDateUseCase @Inject constructor(
    private val trainingDateRepository: TrainingDateCacheRepository,
    private val workoutConclusionRepository: WorkoutConclusionCacheRepository,
    private val getEpochDayForToday: GetEpochDayForToday
) {
    suspend operator fun invoke(workoutId: Long, workoutName: String, isWorkoutAborted: Boolean) {
        val todayEpochDay = getEpochDayForToday()
        val todayTrainingDayOrEmpty = trainingDateRepository.getTrainingDate(todayEpochDay)

        if (todayTrainingDayOrEmpty.epochDay == 0L) trainingDateRepository.insert(todayEpochDay)

        workoutConclusionRepository.insert(
            WorkoutResult(
                workoutId = workoutId,
                workoutName = workoutName,
                isWorkoutAborted = isWorkoutAborted,
                epochDay = todayEpochDay
            )
        )
    }
}