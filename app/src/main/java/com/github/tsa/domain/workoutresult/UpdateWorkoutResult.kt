package com.github.tsa.domain.workoutresult

import com.github.tsa.domain.model.WorkoutConclusion
import javax.inject.Inject

class UpdateWorkoutResult @Inject constructor(private val repository: WorkoutResultCacheRepository) {
    suspend operator fun invoke(workoutResultId: Long, workoutConclusion: WorkoutConclusion): Long =
        repository.update(workoutResultId, workoutConclusion)
}