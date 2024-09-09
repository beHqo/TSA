package com.example.android.strikingarts.domain.usecase.workoutresult

import com.example.android.strikingarts.data.repository.FakeWorkoutResultRepository
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RetrieveLastExecutedWorkoutResultUseCaseTest {
    private val repository = FakeWorkoutResultRepository()
    private val useCase = RetrieveLastExecutedWorkoutResultUseCase(repository)

    @Test
    fun `Given a database populated with WorkoutResult objects, When the operator function of SUT is invoked, Then the last successful WorkoutResult should be retrieved`() =
        runTest {
            repository.lastSuccessfulWorkoutResult() shouldBe useCase.successful()
        }

    @Test
    fun `Given a database populated with WorkoutResult objects, When the operator function of SUT is invoked, Then the last aborted WorkoutResult should be retrieved`() =
        runTest {
            repository.lastFailedWorkoutResult() shouldBe useCase.failed()
        }
}