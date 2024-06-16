package com.example.android.strikingarts.domain.usecase.workout

import app.cash.turbine.test
import com.example.android.strikingarts.data.local.assertWorkoutsAreEqual
import com.example.android.strikingarts.data.repository.FakeWorkoutRepository
import com.example.android.strikingarts.data.workout3
import com.example.android.strikingarts.data.workout4
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RetrieveWorkoutListUseCaseTest {
    private val repository = FakeWorkoutRepository()
    private val useCase = RetrieveWorkoutListUseCase(repository)

    @Test
    fun `Given the workoutList Flow, When objects are inserted in the database, Then Flow should emit`() =
        runTest {
            val flow = useCase.workoutList

            flow.test {
                repository.insert(workout3, emptyList())
                assertWorkoutsAreEqual(awaitItem().last(), workout3)
                awaitComplete()
            }

            flow.test {
                repository.insert(workout4, emptyList())
                assertWorkoutsAreEqual(awaitItem().last(), workout4)
                awaitComplete()
            }
        }
}