package com.thestrikingarts.domain.workout

import com.thestrikingarts.data.local.util.assertWorkoutsAreEqual
import com.thestrikingarts.data.repository.FakeWorkoutRepository
import com.thestrikingarts.data.workout3NotInDB
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UpsertWorkoutUseCaseTest {
    private val repository = FakeWorkoutRepository()
    private val useCase = UpsertWorkoutUseCase(repository)

    @Test
    fun `If the provided Workout is not already saved in the database, insert it`() =
        runTest {
            val workout = workout3NotInDB.copy(id = 0)

            useCase(workout, emptyList())

            assertWorkoutsAreEqual(repository.getLastInsertedWorkout(), workout)
        }

    @Test
    fun `If the provided Workout is not already saved in the database, update it`() =
        runTest {
            val newName = "WorkoutNameCopied"
            val workout = repository.getLastInsertedWorkout().copy(name = newName)

            useCase(workout, emptyList())

            repository.getWorkout(workout.id)?.name shouldBe newName
        }
}