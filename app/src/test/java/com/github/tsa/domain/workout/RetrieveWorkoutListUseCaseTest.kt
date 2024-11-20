package com.github.tsa.domain.workout

import app.cash.turbine.test
import com.github.tsa.data.local.util.assertWorkoutsAreEqual
import com.github.tsa.data.repository.FakeWorkoutRepository
import com.github.tsa.data.workout3NotInDB
import com.github.tsa.data.workout4NotInDB
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RetrieveWorkoutListUseCaseTest {
    private val repository = FakeWorkoutRepository()
    private val useCase = RetrieveWorkoutListUseCase(repository)

    @Test
    fun `Flow should emit the most recently inserted Workout`() =
        runTest {
            val flow = useCase.workoutList

            flow.test {
                repository.insert(workout3NotInDB, emptyList())
                assertWorkoutsAreEqual(awaitItem().last(), workout3NotInDB)
                awaitComplete()
            }

            flow.test {
                repository.insert(workout4NotInDB, emptyList())
                assertWorkoutsAreEqual(awaitItem().last(), workout4NotInDB)
                awaitComplete()
            }
        }
}