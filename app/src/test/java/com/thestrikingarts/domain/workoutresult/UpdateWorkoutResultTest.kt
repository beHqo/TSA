package com.thestrikingarts.domain.workoutresult

import com.thestrikingarts.data.repository.FakeWorkoutResultRepository
import com.thestrikingarts.data.workoutResultFailure1
import com.thestrikingarts.domain.model.WorkoutConclusion
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UpdateWorkoutResultTest {
    private val repository = FakeWorkoutResultRepository() //
    private val useCase = UpdateWorkoutResult(repository)

    @Test
    fun `Update conclusion of an object already saved in the database`() = runTest {
        val toBeUpdated = workoutResultFailure1

        val affectedRows = useCase(toBeUpdated.id, WorkoutConclusion.Aborted(true))
        affectedRows shouldBe 1L

        repository.lastFailedWorkoutResult() shouldNotBe toBeUpdated
    }
}