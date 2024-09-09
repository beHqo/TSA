package com.example.android.strikingarts.domain.usecase.winners

import com.example.android.strikingarts.data.repository.FakeWorkoutResultRepository
import com.example.android.strikingarts.data.workoutResultFailure3NotInDB
import com.example.android.strikingarts.data.workoutResultSuccessNotInDB
import com.example.android.strikingarts.domain.model.WorkoutConclusion
import com.example.android.strikingarts.domain.usecase.javatime.GetEpochDayForToday
import com.example.android.strikingarts.domain.usecase.workoutresult.RetrieveLastExecutedWorkoutResultUseCase
import com.example.android.strikingarts.domain.usecase.workoutresult.UpdateWorkoutResult
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
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

        repository.getLastInsertedOrDefault() shouldBe workoutResult
    }

    @Test
    fun `Insert WorkoutResult with Aborted conclusion in the database`() = runTest {
        val workoutResult = workoutResultFailure3NotInDB

        useCase(
            workoutId = workoutResult.workoutId,
            workoutName = workoutResult.workoutName,
            isWorkoutAborted = workoutResult.conclusion.isWorkoutFailed()
        )

        repository.getLastInsertedOrDefault() shouldBe workoutResult
    }

    @Test
    fun `Insert WorkoutResult with Redeemed conclusion in the database`() = runTest {
        val workoutResult = workoutResultFailure3NotInDB

        useCase(
            workoutId = workoutResult.workoutId,
            workoutName = workoutResult.workoutName,
            isWorkoutAborted = workoutResult.conclusion.isWorkoutFailed()
        )

        repository.getLastInsertedOrDefault() shouldBe workoutResult
        repository.lastFailedWorkoutResult() shouldBe workoutResult

        val updated = workoutResult.copy(conclusion = WorkoutConclusion.Aborted(true))

        useCase(
            workoutId = updated.workoutId,
            workoutName = updated.workoutName,
            isWorkoutAborted = updated.conclusion.isWorkoutFailed()
        )

        repository.lastFailedWorkoutResult() shouldNotBe updated
    }
}