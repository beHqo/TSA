package com.example.android.strikingarts.data.repository

import com.example.android.strikingarts.data.workoutResultList
import com.example.android.strikingarts.domain.interfaces.WorkoutResultCacheRepository
import com.example.android.strikingarts.domain.model.WorkoutConclusion
import com.example.android.strikingarts.domain.model.WorkoutResult

class FakeWorkoutResultRepository : WorkoutResultCacheRepository {
    private val data = workoutResultList.toMutableList()

    override suspend fun lastSuccessfulWorkoutResult(): WorkoutResult? =
        data.filter { it.conclusion is WorkoutConclusion.Successful }.maxByOrNull { it.epochDay }

    override suspend fun lastFailedWorkoutResult(): WorkoutResult? =
        data.filter { it.conclusion.isWorkoutFailed() }.maxByOrNull { it.epochDay }

    override suspend fun getWorkoutResultsInRange(
        fromEpochDay: Long, toEpochDay: Long
    ): List<WorkoutResult> =
        data.filter { it.epochDay in fromEpochDay..toEpochDay }

    override suspend fun getWorkoutResultsByDate(epochDay: Long): List<WorkoutResult> =
        data.filter { it.epochDay == epochDay }

    override suspend fun insert(workoutResult: WorkoutResult) {
        data += workoutResult
    }

    override suspend fun update(workoutResultId: Long, workoutConclusion: WorkoutConclusion): Long {
        var toBeUpdated = WorkoutResult()
        var index = -1

        for ((i, workoutResult) in data.withIndex())
            if (workoutResult.id == workoutResultId && workoutResult.conclusion == workoutConclusion) {
                index = i
                toBeUpdated = WorkoutResult()

                break
            }

        return if (index != -1) {
            data.remove(toBeUpdated)
            data.add(index, toBeUpdated.copy(conclusion = workoutConclusion))

            1
        } else 0
    }

    fun getLastInsertedOrDefault(): WorkoutResult = data.lastOrNull() ?: WorkoutResult()
}