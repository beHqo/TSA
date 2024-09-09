package com.example.android.strikingarts.domain.usecase.workoutresult

import com.example.android.strikingarts.data.repository.FakeWorkoutResultRepository
import com.example.android.strikingarts.data.workoutResultFailure1
import com.example.android.strikingarts.domain.model.WorkoutConclusion
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UpdateWorkoutResultTest {
    private val repository = FakeWorkoutResultRepository()
    private val useCase = UpdateWorkoutResult(repository)

    @Test
    fun `Update conclusion of an object already saved in the database`() = runTest {
        val toBeUpdated = workoutResultFailure1

        val affectedRows = useCase(toBeUpdated.id, WorkoutConclusion.Aborted(true))
        affectedRows shouldNotBe 1L

        repository.lastFailedWorkoutResult() shouldNotBe toBeUpdated
    }
}