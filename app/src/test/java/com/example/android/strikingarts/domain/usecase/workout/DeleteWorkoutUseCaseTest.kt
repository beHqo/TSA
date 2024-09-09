package com.example.android.strikingarts.domain.usecase.workout

import com.example.android.strikingarts.data.repository.FakeWorkoutRepository
import com.example.android.strikingarts.data.workout1
import com.example.android.strikingarts.data.workout2
import com.example.android.strikingarts.data.workout3NotInDB
import com.example.android.strikingarts.data.workout4NotInDB
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteWorkoutUseCaseTest {
    private val repository = FakeWorkoutRepository()
    private val useCase = DeleteWorkoutUseCase(repository)

    @Test
    fun `Given a database pre-populated with Workout objects, When an id of a Workout that already exists in the database is supplied, Then it should be removed`() =
        runTest {
            val workoutId = workout1.id

            useCase(workoutId)

            repository.doesDatabaseContainWorkoutWithIdOf(workoutId) shouldBe false
        }

    @Test
    fun `Given a database pre-populated with Workout objects, When a list of ids of Workouts that already exists in the database is supplied, Then all the Workouts should be removed`() =
        runTest {
            val list = listOf(workout1.id, workout2.id)

            useCase(list)

            list.forEach { id -> repository.doesDatabaseContainWorkoutWithIdOf(id) shouldBe false }
        }

    @Test
    fun `Given a database pre-populated with Combo objects, When an id of a combo that does not exist in the database is supplied, Then it should be removed`() =
        runTest {
            val toBeDeleted = workout3NotInDB

            val affectedRows = useCase(toBeDeleted.id)
            affectedRows shouldBe 0L
        }

    @Test
    fun `Given a database pre-populated with Combo objects, When a list of ids of combos that do not exist in the database is supplied, Then all the combos should be removed`() =
        runTest {
            val list = listOf(workout3NotInDB.id, workout4NotInDB.id)

            val affectedRows = useCase(list)
            affectedRows shouldBe 0L
        }
}