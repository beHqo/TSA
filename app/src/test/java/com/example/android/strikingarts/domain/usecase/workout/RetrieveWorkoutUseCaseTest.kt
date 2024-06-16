package com.example.android.strikingarts.domain.usecase.workout

import com.example.android.strikingarts.data.local.assertWorkoutsAreEqual
import com.example.android.strikingarts.data.repository.FakeWorkoutRepository
import com.example.android.strikingarts.data.workout2
import com.example.android.strikingarts.domain.model.Workout
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RetrieveWorkoutUseCaseTest {
    private val repository = FakeWorkoutRepository()
    private val useCase = RetrieveWorkoutUseCase(repository)

    @Test
    fun `Given a database pre-populated with Workout objects, When an id of a Workout that already exists in the database is supplied, Then it should be retrieved`() =
        runTest {
            val workout = workout2

            val retrieved = useCase(workout.id)

            assertWorkoutsAreEqual(retrieved, workout)
        }

    @Test
    fun `Given a database pre-populated with Workout objects, When an id of a Workout that does not exists in the database is supplied, Then a Workout with default values should be retrieved`() =
        runTest {
            val retrieved = useCase(77)

            assertWorkoutsAreEqual(retrieved, Workout())
        }
}