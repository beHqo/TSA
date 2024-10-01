package com.example.android.strikingarts.domain.workoutresult

import com.example.android.strikingarts.data.local.util.assertWorkoutResultsAreEqual
import com.example.android.strikingarts.data.repository.FakeWorkoutResultRepository
import com.example.android.strikingarts.data.workoutResultFailure3NotInDB
import com.example.android.strikingarts.data.workoutResultSuccessNotInDB
import com.example.android.strikingarts.domain.model.WorkoutConclusion
import com.example.android.strikingarts.domainandroid.usecase.javatime.GetEpochDayForToday
import kotlinx.coroutines.test.runTest
import org.junit.Test

class InsertWorkoutResultUseCaseTest {
    private val repository = FakeWorkoutResultRepository()
    private val retrieveLastExecutedWorkoutResultUseCase =
        RetrieveLastExecutedWorkoutResultUseCase(repository)
    private val updateWorkoutResult = UpdateWorkoutResult(repository)
    private val useCase = InsertWorkoutResultUseCase(
        repository,
        retrieveLastExecutedWorkoutResultUseCase,
        updateWorkoutResult,
        GetEpochDayForToday()
    )

    @Test
    fun `Insert WorkoutResult with Successful conclusion in the database`() = runTest {
        val workoutResult = workoutResultSuccessNotInDB

        useCase(
            workoutId = workoutResult.workoutId,
            workoutName = workoutResult.workoutName,
            isWorkoutAborted = workoutResult.conclusion.isWorkoutFailed()
        )

        assertWorkoutResultsAreEqual(repository.getLastInsertedOrDefault(), workoutResult)
    }

    @Test
    fun `Insert WorkoutResult with Aborted conclusion in the database`() = runTest {
        val workoutResult = workoutResultFailure3NotInDB

        useCase(
            workoutId = workoutResult.workoutId,
            workoutName = workoutResult.workoutName,
            isWorkoutAborted = workoutResult.conclusion.isWorkoutFailed()
        )

        assertWorkoutResultsAreEqual(repository.getLastInsertedOrDefault(), workoutResult)
    }

    @Test
    fun `Insert WorkoutResult with Redeemed conclusion in the database`() = runTest {
        val workoutResult = workoutResultFailure3NotInDB

        useCase(
            workoutId = workoutResult.workoutId,
            workoutName = workoutResult.workoutName,
            isWorkoutAborted = workoutResult.conclusion.isWorkoutFailed()
        )

        assertWorkoutResultsAreEqual(repository.getLastInsertedOrDefault(), workoutResult)
        assertWorkoutResultsAreEqual(repository.lastFailedWorkoutResult(), workoutResult)

        val updated = workoutResult.copy(conclusion = WorkoutConclusion.Aborted(true))

        useCase(
            workoutId = updated.workoutId,
            workoutName = updated.workoutName,
            isWorkoutAborted = updated.conclusion.isWorkoutFailed()
        )

        assertWorkoutResultsAreEqual(repository.lastFailedWorkoutResult(), workoutResult)
    }
}