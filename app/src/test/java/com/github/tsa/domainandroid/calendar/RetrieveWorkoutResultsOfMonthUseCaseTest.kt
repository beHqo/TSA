package com.github.tsa.domainandroid.calendar

import com.github.tsa.data.repository.FakeWorkoutResultRepository
import com.github.tsa.data.workoutResultList
import com.github.tsa.domain.calendar.RetrieveWorkoutResultsOfMonthUseCase
import com.github.tsa.domainandroid.usecase.javatime.RetrieveEpochDayForFirstAndLastDayOfMonth
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RetrieveWorkoutResultsOfMonthUseCaseTest {
    private val workoutResultRepository = FakeWorkoutResultRepository()
    private val useCase = RetrieveWorkoutResultsOfMonthUseCase(
        RetrieveEpochDayForFirstAndLastDayOfMonth(), workoutResultRepository
    )

    @Test
    fun `Retrieve the WorkoutResults inserted in the database within this month`() =
        runTest {
            val retrieved = useCase(0)

            retrieved shouldBe workoutResultList
        }
}