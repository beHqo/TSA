package com.github.tsa.data.local.dao

import com.github.tsa.data.local.BaseDatabaseTest
import com.github.tsa.data.local.util.assertWorkoutResultsAreEqual
import com.github.tsa.data.workoutResultFailure1
import com.github.tsa.data.workoutResultList
import com.github.tsa.data.workoutResultRedeemedNotInDB
import com.github.tsa.data.workoutResultSuccess1
import com.github.tsa.domain.model.WorkoutConclusion
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
    fun `Insert WorkoutResult`() = testScope.runTest {
        val id = workoutResultDao.insert(workoutResultRedeemedNotInDB)

        id shouldBe lastInsertedRow
    }

    @Test
    fun `Retrieve the most recent successful WorkoutResult if there exists one`() =
        testScope.runTest {
            val retrieved = workoutResultDao.getLastSuccessfulWorkoutResult()

            assertWorkoutResultsAreEqual(retrieved, workoutResultSuccess1)
        }

    @Test
    fun `Retrieve the most recently failed WorkoutResult if there exists one`() =
        testScope.runTest {
            val retrieved = workoutResultDao.getLastFailedWorkoutResult()

            assertWorkoutResultsAreEqual(retrieved, workoutResultFailure1)
        }

    @Test
    fun `Retrieve the WorkoutResults of a certain date, if there exists any`() = testScope.runTest {
        val epochDay = workoutResultSuccess1.epochDay

        val list = workoutResultDao.getWorkoutResultsByDate(epochDay)

        workoutResultList.filter { it.epochDay == epochDay }.forEachIndexed { i, wr ->
            assertWorkoutResultsAreEqual(wr, list[i])
        }
    }

    @Test
    fun `Retrieve WorkoutResults within the given date, if there exists any`() = testScope.runTest {
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
    fun `If the provided WorkoutResult is already saved in the database, update its WorkoutConclusion`() =
        testScope.runTest {
            val toBeUpdated = workoutResultDao.getLastFailedWorkoutResult()!!
                .copy(conclusion = WorkoutConclusion.Aborted(true))
            val affectedRows =
                workoutResultDao.update(toBeUpdated.workoutId, toBeUpdated.conclusion)

            affectedRows shouldBe 1L
        }

    @Test
    fun `If the provided WorkoutResult does not refer to any objects within the database, do nothing`() =
        testScope.runTest {
            val affectedRows = workoutResultDao.update(
                workoutResultRedeemedNotInDB.workoutId, workoutResultRedeemedNotInDB.conclusion
            )

            affectedRows shouldBe 0L
        }
}