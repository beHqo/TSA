package com.example.android.strikingarts.data.local.dao

import com.example.android.strikingarts.data.local.BaseDatabaseTest
import com.example.android.strikingarts.data.local.assertWorkoutResultsAreEqual
import com.example.android.strikingarts.data.workoutResultFailure1
import com.example.android.strikingarts.data.workoutResultFailure2
import com.example.android.strikingarts.data.workoutResultSuccess1
import com.example.android.strikingarts.data.workoutResultSuccess2
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDate

class WorkoutResultDaoTest : BaseDatabaseTest() {
    private val workoutResultDao = WorkoutResultDao(database, testDispatcher, testDispatcher)

    @Test
    fun `Given a WorkoutResult object, When inserted, Then confirm the operation has successfully concluded`() =
        testScope.runTest {
            val id = workoutResultDao.insert(workoutResultSuccess1)

            id shouldBe 1
        }

    @Test
    fun `Given a database with aborted and successful WorkoutResult objects, When its last successful WorkoutResult is retrieved, Then confirm its correctness`() =
        testScope.runTest {
            workoutResultDao.insert(workoutResultSuccess1)
            workoutResultDao.insert(workoutResultFailure1)

            val retrieved = workoutResultDao.getLastSuccessfulWorkoutResult()
            assertWorkoutResultsAreEqual(retrieved, workoutResultSuccess1)
        }

    @Test
    fun `Given a database with aborted and successful WorkoutResult objects, When its last failed WorkoutResult is retrieved, Then confirm its correctness`() =
        testScope.runTest {
            workoutResultDao.insert(workoutResultSuccess1)
            workoutResultDao.insert(workoutResultFailure1)

            val retrieved = workoutResultDao.getLastFailedWorkoutResult()
            assertWorkoutResultsAreEqual(retrieved, workoutResultFailure1)
        }

    @Test
    fun `Given a database with several WorkoutResult objects, When the objects of a certain date is retrieved, Then confirm their correctness`() =
        testScope.runTest {
            workoutResultDao.insert(workoutResultSuccess1)
            workoutResultDao.insert(workoutResultFailure1)

            val list = workoutResultDao.getWorkoutResultsByDate(workoutResultSuccess1.epochDay)

            assertWorkoutResultsAreEqual(list[0], workoutResultSuccess1)
            assertWorkoutResultsAreEqual(list[1], workoutResultFailure1)
        }

    @Test
    fun `Given a database with several WorkoutResult objects, When the content within a given date is retrieved, Then confirm their correctness`() =
        testScope.runTest {
            workoutResultDao.insert(workoutResultSuccess1)
            workoutResultDao.insert(workoutResultSuccess2)
            workoutResultDao.insert(workoutResultFailure1)
            workoutResultDao.insert(workoutResultFailure2)

            val list = workoutResultDao.getWorkoutResultsInRange(
                LocalDate.now().minusDays(2).toEpochDay(), LocalDate.now().toEpochDay()
            )

            assertWorkoutResultsAreEqual(list[0], workoutResultFailure2)
            assertWorkoutResultsAreEqual(list[1], workoutResultSuccess2)
            assertWorkoutResultsAreEqual(list[2], workoutResultSuccess1)
            assertWorkoutResultsAreEqual(list[3], workoutResultFailure1)
        }
}