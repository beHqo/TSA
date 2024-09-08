package com.example.android.strikingarts.domain.usecase.workoutresult

import com.example.android.strikingarts.domain.interfaces.WorkoutResultCacheRepository
import com.example.android.strikingarts.domain.model.WorkoutConclusion
import javax.inject.Inject

class UpdateWorkoutResult @Inject constructor(private val repository: WorkoutResultCacheRepository) {
    suspend operator fun invoke(workoutResultId: Long, workoutConclusion: WorkoutConclusion): Long =
        repository.update(workoutResultId, workoutConclusion)
}