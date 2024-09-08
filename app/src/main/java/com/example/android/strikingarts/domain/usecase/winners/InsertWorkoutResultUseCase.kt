package com.example.android.strikingarts.domain.usecase.winners

import com.example.android.strikingarts.domain.interfaces.WorkoutResultCacheRepository
import com.example.android.strikingarts.domain.model.WorkoutConclusion
import com.example.android.strikingarts.domain.model.WorkoutResult
import com.example.android.strikingarts.domain.usecase.javatime.GetEpochDayForToday
import com.example.android.strikingarts.domain.usecase.workoutresult.RetrieveLastExecutedWorkoutResultUseCase
import com.example.android.strikingarts.domain.usecase.workoutresult.UpdateWorkoutResult
import javax.inject.Inject

class InsertWorkoutResultUseCase @Inject constructor(
    private val workoutResultRepository: WorkoutResultCacheRepository,
    private val retrieveLastExecutedWorkoutResultUseCase: RetrieveLastExecutedWorkoutResultUseCase,
    private val updateWorkoutResult: UpdateWorkoutResult,
    private val getEpochDayForToday: GetEpochDayForToday
) {
    suspend operator fun invoke(workoutId: Long, workoutName: String, isWorkoutAborted: Boolean) {
        val workoutConclusion = if (isWorkoutAborted) WorkoutConclusion.Aborted(false)
        else WorkoutConclusion.Successful

        if (!isWorkoutAborted) {
            val previouslyFailed = retrieveLastExecutedWorkoutResultUseCase.failed()

            if (previouslyFailed?.workoutId == workoutId) {
                updateWorkoutResult(previouslyFailed.id, WorkoutConclusion.Aborted(true))
            }
        }

        workoutResultRepository.insert(
            WorkoutResult(
                workoutId = workoutId,
                workoutName = workoutName,
                conclusion = workoutConclusion,
                epochDay = getEpochDayForToday()
            )
        )
    }
}