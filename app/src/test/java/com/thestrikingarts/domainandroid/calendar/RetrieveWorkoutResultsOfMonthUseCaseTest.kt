package com.thestrikingarts.domainandroid.calendar

import com.thestrikingarts.data.repository.FakeWorkoutResultRepository
import com.thestrikingarts.data.workoutResultList
import com.thestrikingarts.domain.calendar.RetrieveWorkoutResultsOfMonthUseCase
import com.thestrikingarts.domainandroid.usecase.javatime.RetrieveEpochDayForFirstAndLastDayOfMonth
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