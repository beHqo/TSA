package com.example.android.strikingarts.domain.usecase.winners

import com.example.android.strikingarts.domain.interfaces.WorkoutResultCacheRepository
import com.example.android.strikingarts.domain.model.WorkoutResult
import com.example.android.strikingarts.domain.usecase.javatime.GetEpochDayForToday
import javax.inject.Inject

class InsertWorkoutConclusionUseCase @Inject constructor(
    private val workoutConclusionRepository: WorkoutResultCacheRepository,
    private val getEpochDayForToday: GetEpochDayForToday
) {
    suspend operator fun invoke(workoutId: Long, workoutName: String, isWorkoutAborted: Boolean) {
        val todayEpochDay = getEpochDayForToday()

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