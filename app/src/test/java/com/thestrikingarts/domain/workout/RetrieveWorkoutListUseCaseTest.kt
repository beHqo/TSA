package com.thestrikingarts.domain.workout

import app.cash.turbine.test
import com.thestrikingarts.data.local.util.assertWorkoutsAreEqual
import com.thestrikingarts.data.repository.FakeWorkoutRepository
import com.thestrikingarts.data.workout3NotInDB
import com.thestrikingarts.data.workout4NotInDB
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