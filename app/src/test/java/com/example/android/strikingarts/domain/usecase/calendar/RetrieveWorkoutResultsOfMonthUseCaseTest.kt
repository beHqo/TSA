package com.example.android.strikingarts.domain.usecase.calendar

import com.example.android.strikingarts.data.repository.FakeWorkoutResultRepository
import com.example.android.strikingarts.data.workoutResultList
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RetrieveWorkoutResultsOfMonthUseCaseTest {
    private val workoutResultRepository = FakeWorkoutResultRepository()
    private val useCase = RetrieveWorkoutResultsOfMonthUseCase(
        RetrieveEpochDayForFirstAndLastDayOfMonth(), workoutResultRepository
    )

    @Test
    fun `Given a beforeOrAfterCurrentMonth value of 0, When there exists WorkoutResults in the database that are from the current month, Then confirm the correctness of values`() =
        runTest {
            val retrieved = useCase(0)

            retrieved shouldBe workoutResultList
        }
}