package com.thestrikingarts.domain.workout

import com.thestrikingarts.data.local.util.assertWorkoutsAreEqual
import com.thestrikingarts.data.repository.FakeWorkoutRepository
import com.thestrikingarts.data.workout2
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RetrieveWorkoutUseCaseTest {
    private val repository = FakeWorkoutRepository()
    private val useCase = RetrieveWorkoutUseCase(repository)

    @Test
    fun `If the provided id refers to a Workout in the database, retrieve it`() =
        runTest {
            val workout = workout2

            val retrieved = useCase(workout.id)

            assertWorkoutsAreEqual(retrieved, workout)
        }

    @Test
    fun `If the provided id does not refer to a Workout in the database, return null`() =
        runTest {
            val retrieved = useCase(77)

            retrieved shouldBe null
        }
}