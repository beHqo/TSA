package com.example.android.strikingarts.domain.workout

import app.cash.turbine.test
import com.example.android.strikingarts.data.local.util.assertWorkoutsAreEqual
import com.example.android.strikingarts.data.repository.FakeWorkoutRepository
import com.example.android.strikingarts.data.workout3NotInDB
import com.example.android.strikingarts.data.workout4NotInDB
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