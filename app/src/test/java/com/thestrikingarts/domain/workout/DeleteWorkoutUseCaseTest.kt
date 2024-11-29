package com.thestrikingarts.domain.workout

import com.thestrikingarts.data.repository.FakeWorkoutRepository
import com.thestrikingarts.data.workout1
import com.thestrikingarts.data.workout2
import com.thestrikingarts.data.workout3NotInDB
import com.thestrikingarts.data.workout4NotInDB
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteWorkoutUseCaseTest {
    private val repository = FakeWorkoutRepository()
    private val useCase = DeleteWorkoutUseCase(repository)

    @Test
    fun `Delete workout by id`() = runTest {
        val workoutId = workout1.id

        useCase(workoutId)

        repository.doesDatabaseContainWorkoutWithIdOf(workoutId) shouldBe false
    }

    @Test
    fun `Delete workouts by a list of ids`() = runTest {
        val list = listOf(workout1.id, workout2.id)

        useCase(list)

        list.forEach { id -> repository.doesDatabaseContainWorkoutWithIdOf(id) shouldBe false }
    }

    @Test
    fun `When provided id does not refer to any objects in the database, do nothing`() = runTest {
        val toBeDeleted = workout3NotInDB

        val affectedRows = useCase(toBeDeleted.id)
        affectedRows shouldBe 0L
    }

    @Test
    fun `When provided list of ids do not refer to any objects in the database, do nothing`() =
        runTest {
            val list = listOf(workout3NotInDB.id, workout4NotInDB.id)

            val affectedRows = useCase(list)
            affectedRows shouldBe 0L
        }
}