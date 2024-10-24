package com.example.android.strikingarts.domainandroid.calendar

import com.example.android.strikingarts.data.repository.FakeWorkoutResultRepository
import com.example.android.strikingarts.data.workoutResultList
import com.example.android.strikingarts.domain.calendar.RetrieveWorkoutResultsOfMonthUseCase
import com.example.android.strikingarts.domainandroid.usecase.javatime.RetrieveEpochDayForFirstAndLastDayOfMonth
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