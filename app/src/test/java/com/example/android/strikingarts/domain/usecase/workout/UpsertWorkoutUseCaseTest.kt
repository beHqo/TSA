package com.example.android.strikingarts.domain.usecase.workout

import com.example.android.strikingarts.data.local.assertWorkoutsAreEqual
import com.example.android.strikingarts.data.repository.FakeWorkoutRepository
import com.example.android.strikingarts.data.workout1
import com.example.android.strikingarts.data.workout3
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UpsertWorkoutUseCaseTest {
    private val repository = FakeWorkoutRepository()
    private val useCase = UpsertWorkoutUseCase(repository)

    @Test
    fun `Given a database populated with Workout objects, When a new Workout is supplied, Then it should be inserted`() =
        runTest {
            val workout = workout3.copy(id = 0)

            useCase(workout, emptyList())

            assertWorkoutsAreEqual(repository.getLastInsertedWorkout(), workout)
        }

    @Test
    fun `Given a database populated with Workout objects, When a Workout that is already in the database is supplied, Then it should be updated`() =
        runTest {
            val newName = "WorkoutNameCopied"
            val workout = workout1.copy(name = newName)

            useCase(workout, emptyList())

            repository.getWorkout(workout.id).name shouldBe newName
        }
}