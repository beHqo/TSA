package com.example.android.strikingarts.data.local.dao

import com.example.android.strikingarts.data.local.BaseDatabaseTest
import com.example.android.strikingarts.data.local.util.assertWorkoutResultsAreEqual
import com.example.android.strikingarts.data.workoutResultFailure1
import com.example.android.strikingarts.data.workoutResultList
import com.example.android.strikingarts.data.workoutResultRedeemedNotInDB
import com.example.android.strikingarts.data.workoutResultSuccess1
import com.example.android.strikingarts.domain.model.WorkoutConclusion
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class WorkoutResultDaoTest : BaseDatabaseTest() {
    private val workoutResultDao =
        WorkoutResultDao(database, ioDispatcherTest, defaultDispatcherTest)
    private val lastInsertedRow: Long = (workoutResultList.size + 1).toLong()

    @Before
    fun setup() {
        testScope.launch {
            workoutResultList.forEach { workoutRes -> workoutResultDao.insert(workoutRes) }
        }
    }

    @Test
    fun `Given a WorkoutResult object, When inserted, Then confirm the operation has successfully concluded`() =
        testScope.runTest {
            val id = workoutResultDao.insert(workoutResultRedeemedNotInDB)

            id shouldBe lastInsertedRow
        }

    @Test
    fun `Given a database with aborted and successful WorkoutResult objects, When its last successful WorkoutResult is retrieved, Then confirm its correctness`() =
        testScope.runTest {
            val retrieved = workoutResultDao.getLastSuccessfulWorkoutResult()

            assertWorkoutResultsAreEqual(retrieved, workoutResultSuccess1)
        }

    @Test
    fun `Given a database with aborted and successful WorkoutResult objects, When its last failed WorkoutResult is retrieved, Then confirm its correctness`() =
        testScope.runTest {
            val retrieved = workoutResultDao.getLastFailedWorkoutResult()

            assertWorkoutResultsAreEqual(retrieved, workoutResultFailure1)
        }

    @Test
    fun `Given a database with several WorkoutResult objects, When the objects of a certain date is retrieved, Then confirm their correctness`() =
        testScope.runTest {
            val list = workoutResultDao.getWorkoutResultsByDate(workoutResultSuccess1.epochDay)

            assertWorkoutResultsAreEqual(list[0], workoutResultSuccess1)
            assertWorkoutResultsAreEqual(list[1], workoutResultFailure1)
        }

    @Test
    fun `Given a database with several WorkoutResult objects, When the content within a given date is retrieved, Then confirm their correctness`() =
        testScope.runTest {
            val date = LocalDate.now().minusDays(2).toEpochDay()

            val actualList = workoutResultDao.getWorkoutResultsInRange(
                date, LocalDate.now().toEpochDay()
            )
            val expectedList = workoutResultList.filter { it.epochDay == date }

            for (i in expectedList.indices) assertWorkoutResultsAreEqual(
                actualList[i], expectedList[i]
            )
        }

    @Test
    fun `Given a database with several WorkoutResult objects, When the id of a WorkoutResult that already exists in the database is supplied, Then the WorkoutConclusion is updated to the given value`() =
        testScope.runTest {
            val toBeUpdated = workoutResultDao.getLastFailedWorkoutResult()!!
                .copy(conclusion = WorkoutConclusion.Aborted(true))
            val affectedRows =
                workoutResultDao.update(toBeUpdated.workoutId, toBeUpdated.conclusion)

            affectedRows shouldBe 1L
        }

    @Test
    fun `Given a database with several WorkoutResult objects, When attempting to update an object that does not exist in the database, Then nothing should happen`() =
        testScope.runTest {
            val affectedRows = workoutResultDao.update(
                workoutResultRedeemedNotInDB.workoutId, workoutResultRedeemedNotInDB.conclusion
            )

            affectedRows shouldBe 0L
        }
}