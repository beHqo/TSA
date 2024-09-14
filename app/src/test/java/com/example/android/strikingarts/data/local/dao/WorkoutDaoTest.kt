package com.example.android.strikingarts.data.local.dao

import app.cash.turbine.test
import com.example.android.strikingarts.data.listOfWorkouts
import com.example.android.strikingarts.data.local.BaseDatabaseTest
import com.example.android.strikingarts.data.local.util.InsertCombo
import com.example.android.strikingarts.data.local.util.assertWorkoutsAreEqual
import com.example.android.strikingarts.data.workout3NotInDB
import com.example.android.strikingarts.data.workout4NotInDB
import com.example.android.strikingarts.domain.model.Workout
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class WorkoutDaoTest : BaseDatabaseTest() {
    private val audioAttributesDao =
        AudioAttributesDao(database, ioDispatcherTest, defaultDispatcherTest)
    private val techniqueDao = TechniqueDao(database, ioDispatcherTest, defaultDispatcherTest)
    private val comboDao = ComboDao(database, ioDispatcherTest, defaultDispatcherTest)
    private val workoutDao = WorkoutDao(database, ioDispatcherTest, defaultDispatcherTest)
    private val lastInsertedRowId = listOfWorkouts.size + 1
    private val insertCombo = InsertCombo()

    @Before
    fun setup() {
        testScope.launch {
            listOfWorkouts.forEach { workout ->
                insert(workout, audioAttributesDao, techniqueDao, comboDao, workoutDao)
            }
        }
    }

    @Test
    fun `Given a flow, When new objects are inserted, Then flow should emit`() = testScope.runTest {
        val flow = workoutDao.workoutList

        flow.test {
            val toBeInserted = workout3NotInDB

            insert(toBeInserted, audioAttributesDao, techniqueDao, comboDao, workoutDao)

            assertWorkoutsAreEqual(awaitItem().last(), toBeInserted)
        }

        flow.test {
            val toBeInserted = workout4NotInDB

            insert(toBeInserted, audioAttributesDao, techniqueDao, comboDao, workoutDao)

            assertWorkoutsAreEqual(awaitItem().last(), toBeInserted)
        }
    }

    @Test
    fun `Given a Workout object, When it's inserted in the database, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            val toBeInserted = workout3NotInDB

            val id = insert(toBeInserted, audioAttributesDao, techniqueDao, comboDao, workoutDao)

            id shouldBe lastInsertedRowId

            val retrieved = workoutDao.getWorkout(id)

            assertWorkoutsAreEqual(retrieved, toBeInserted)
        }

    @Test
    fun `Given a Workout object that is in the database, When updated, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            val id = 1L
            val updatedName = "Updated Name"

            val workoutUpdated = workoutDao.getWorkout(id)!!.copy(name = updatedName)
            val affectedRows =
                workoutDao.update(workoutUpdated, workoutUpdated.comboList.map { it.id })

            affectedRows shouldBe 1L

            val retrieved = workoutDao.getWorkout(id)
            retrieved?.name shouldBe updatedName
        }

    @Test
    fun `Given an Workout object that is in the database, When its comboList is updated, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            val id = 1L
            val workout = workoutDao.getWorkout(id)!!

            val updatedTechniqueIdList = listOf<Long>()

            val affectedRows = workoutDao.update(workout, updatedTechniqueIdList)
            affectedRows shouldBe 1L

            val afterUpdate = workoutDao.getWorkout(id)
            afterUpdate?.comboList shouldBe updatedTechniqueIdList
        }

    @Test
    fun `Given a Workout object that is in the database, When deleted, Then retrieved and confirmed to be null`() =
        testScope.runTest {
            val id = 1L

            val affectedRows = workoutDao.delete(id)
            affectedRows shouldBe 1L

            val afterDelete = workoutDao.getWorkout(1L)
            afterDelete shouldBe null
        }

    @Test
    fun `Given several Workout objects that are in the database, When deleted, Then retrieved and confirmed to be null`() =
        testScope.runTest {
            val idList = listOf(1L, 2L)

            val affectedRows = workoutDao.deleteAll(idList)
            affectedRows shouldBe 2L

            idList.forEach { id -> workoutDao.getWorkout(id) shouldBe null }
        }

    private suspend fun insert(
        workout: Workout,
        audioAttributesDao: AudioAttributesDao,
        techniqueDao: TechniqueDao,
        comboDao: ComboDao,
        workoutDao: WorkoutDao
    ): Long {
        val comboIdList = mutableListOf<Long>()

        workout.comboList.forEach { combo ->
            comboIdList += insertCombo(
                combo, audioAttributesDao, techniqueDao, comboDao
            )
        }

        return workoutDao.insert(workout, comboIdList)
    }
}