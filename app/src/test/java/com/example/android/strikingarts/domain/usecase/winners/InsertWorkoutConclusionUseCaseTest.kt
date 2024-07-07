package com.example.android.strikingarts.domain.usecase.winners

import com.example.android.strikingarts.data.repository.FakeWorkoutResultRepository
import com.example.android.strikingarts.data.workoutResultFailure1
import com.example.android.strikingarts.domain.usecase.javatime.GetEpochDayForToday
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class InsertWorkoutConclusionUseCaseTest {
    private val repository = FakeWorkoutResultRepository()
    private val useCase = InsertWorkoutConclusionUseCase(repository, GetEpochDayForToday())

    @Test
    fun `Object is inserted in the database`() = runTest {
        val workoutResult = workoutResultFailure1

        useCase(
            workoutId = workoutResult.workoutId,
            workoutName = workoutResult.workoutName,
            isWorkoutAborted = workoutResult.isWorkoutAborted
        )

        repository.getLastInsertedOrDefault() shouldBe workoutResult
    }
}