package com.example.android.strikingarts.data.local.dao

import app.cash.turbine.test
import com.example.android.strikingarts.data.leadHighKick
import com.example.android.strikingarts.data.local.BaseDatabaseTest
import com.example.android.strikingarts.data.local.assertWorkoutsAreEqual
import com.example.android.strikingarts.data.rearHighKick
import com.example.android.strikingarts.data.rearHighKickStepForwardSlashingElbow
import com.example.android.strikingarts.data.slashingElbow
import com.example.android.strikingarts.data.spearElbow
import com.example.android.strikingarts.data.stepBack
import com.example.android.strikingarts.data.stepBackLeadHighKick
import com.example.android.strikingarts.data.stepForward
import com.example.android.strikingarts.data.stepForwardSpearElbow
import com.example.android.strikingarts.data.workout1
import com.example.android.strikingarts.data.workout2
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class WorkoutDaoTest : BaseDatabaseTest() {
    private val techniqueDao = TechniqueDao(database, testDispatcher, testDispatcher)
    private val comboDao = ComboDao(database, testDispatcher, testDispatcher)
    private val workoutDao = WorkoutDao(database, testDispatcher, testDispatcher)

    @Test
    fun `Given a flow, When new objects are inserted, Then flow should emit`() = testScope.runTest {
        val flow = workoutDao.workoutList

        flow.test {
            val stepBackId = techniqueDao.insert(stepBack, null)
            val leadHighKickId = techniqueDao.insert(leadHighKick, null)
            val comboId = comboDao.insert(stepBackLeadHighKick, listOf(stepBackId, leadHighKickId))
            workoutDao.insert(workout1, listOf(comboId))

            assertWorkoutsAreEqual(awaitItem()[0], workout1)
        }

        flow.test {
            val rearHighKickId = techniqueDao.insert(rearHighKick, null)
            val stepForwardId = techniqueDao.insert(stepForward, null)
            val slashingElbowId = techniqueDao.insert(slashingElbow, null)
            val spearElbowId = techniqueDao.insert(spearElbow, null)

            val comboId1 =
                comboDao.insert(stepForwardSpearElbow, listOf(stepForwardId, spearElbowId))
            val comboId2 = comboDao.insert(
                rearHighKickStepForwardSlashingElbow,
                listOf(rearHighKickId, stepForwardId, slashingElbowId)
            )

            workoutDao.insert(workout2, listOf(comboId1, comboId2))

            assertWorkoutsAreEqual(awaitItem()[1], workout2)
        }
    }

    @Test
    fun `Given a Workout object, When it's inserted in the database, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            val stepBackId = techniqueDao.insert(stepBack, null)
            val leadHighKickId = techniqueDao.insert(leadHighKick, null)

            val comboId = comboDao.insert(stepBackLeadHighKick, listOf(stepBackId, leadHighKickId))
            val workoutId = workoutDao.insert(workout1, listOf(comboId))
            workoutId shouldBe 1

            val retrieved = workoutDao.getWorkoutListItem(workoutId)
            assertWorkoutsAreEqual(retrieved, workout1)
        }

    @Test
    fun `Given a Workout object that is in the database, When updated, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            val stepBackId = techniqueDao.insert(stepBack, null)
            val leadHighKickId = techniqueDao.insert(leadHighKick, null)

            val comboId = comboDao.insert(stepBackLeadHighKick, listOf(stepBackId, leadHighKickId))
            val workoutId = workoutDao.insert(workout1, listOf(comboId))

            val retrieved = workoutDao.getWorkoutListItem(workoutId)

            val updatedName = "Updated"
            val affectedRows =
                workoutDao.update(retrieved!!.copy(name = updatedName), listOf(comboId))
            affectedRows shouldBe 1

            val updated = workoutDao.getWorkoutListItem(workoutId)
            updated!!.name shouldBe updatedName
        }

    @Test
    fun `Given an Workout object that is in the database, When its comboList is updated, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            val rearHighKickId = techniqueDao.insert(rearHighKick, null)
            val stepForwardId = techniqueDao.insert(stepForward, null)
            val slashingElbowId = techniqueDao.insert(slashingElbow, null)
            val spearElbowId = techniqueDao.insert(spearElbow, null)

            val comboId1 = comboDao.insert(
                rearHighKickStepForwardSlashingElbow,
                listOf(rearHighKickId, stepForwardId, slashingElbowId)
            )
            val comboId2 =
                comboDao.insert(stepForwardSpearElbow, listOf(stepForwardId, spearElbowId))

            val workoutId = workoutDao.insert(workout2, listOf(comboId1, comboId2))

            val retrieved = workoutDao.getWorkoutListItem(workoutId)
            val affectedRows = workoutDao.update(retrieved!!, listOf(comboId1))
            affectedRows shouldBe 1

            val updated = workoutDao.getWorkoutListItem(workoutId)
            updated!!.comboList.map { it.id } shouldBe listOf(comboId1)
        }

    @Test
    fun `Given a Workout object that is in the database, When deleted, Then retrieved and confirmed to be null`() =
        testScope.runTest {
            val stepBackId = techniqueDao.insert(stepBack, null)
            val leadHighKickId = techniqueDao.insert(leadHighKick, null)

            val comboId = comboDao.insert(stepBackLeadHighKick, listOf(stepBackId, leadHighKickId))
            val workoutId = workoutDao.insert(workout1, listOf(comboId))

            val affectedRows = workoutDao.delete(workoutId)
            affectedRows shouldBe 1

            val retrieved = workoutDao.getWorkoutListItem(workoutId)
            retrieved shouldBe null
        }

    @Test
    fun `Given several Workout objects that are in the database, When deleted, Then retrieved and confirmed to be null`() =
        testScope.runTest {
            val stepBackId = techniqueDao.insert(stepBack, null)
            val leadHighKickId = techniqueDao.insert(leadHighKick, null)
            val rearHighKickId = techniqueDao.insert(rearHighKick, null)
            val stepForwardId = techniqueDao.insert(stepForward, null)
            val slashingElbowId = techniqueDao.insert(slashingElbow, null)
            val spearElbowId = techniqueDao.insert(spearElbow, null)

            val comboId = comboDao.insert(stepBackLeadHighKick, listOf(stepBackId, leadHighKickId))
            val comboId1 =
                comboDao.insert(stepForwardSpearElbow, listOf(stepForwardId, spearElbowId))
            val comboId2 = comboDao.insert(
                rearHighKickStepForwardSlashingElbow,
                listOf(rearHighKickId, stepForwardId, slashingElbowId)
            )

            val workoutId1 = workoutDao.insert(workout1, listOf(comboId))
            val workoutId2 = workoutDao.insert(workout2, listOf(comboId1, comboId2))

            val affectedRows = workoutDao.deleteAll(listOf(workoutId1, workoutId2))
            affectedRows shouldBe 2

            val retrieved1 = workoutDao.getWorkoutListItem(workoutId1)
            retrieved1 shouldBe null

            val retrieved2 = workoutDao.getWorkoutListItem(workoutId2)
            retrieved2 shouldBe null
        }
}