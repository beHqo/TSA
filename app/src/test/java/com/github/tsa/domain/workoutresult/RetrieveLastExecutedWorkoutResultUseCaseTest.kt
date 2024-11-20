package com.github.tsa.domain.workoutresult

import com.github.tsa.data.repository.FakeWorkoutResultRepository
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RetrieveLastExecutedWorkoutResultUseCaseTest {
    private val repository = FakeWorkoutResultRepository()
    private val useCase = RetrieveLastExecutedWorkoutResultUseCase(repository)

    @Test
    fun `Retrieve the most recent successful WorkoutResult`() = runTest {
        repository.lastSuccessfulWorkoutResult() shouldBe useCase.successful()
    }

    @Test
    fun `Retrieve the most recent failed WorkoutResult`() = runTest {
        repository.lastFailedWorkoutResult() shouldBe useCase.failed()
    }
}