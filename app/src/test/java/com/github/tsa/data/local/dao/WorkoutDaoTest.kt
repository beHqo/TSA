package com.github.tsa.data.local.dao

import app.cash.turbine.test
import com.github.tsa.data.listOfWorkouts
import com.github.tsa.data.local.BaseDatabaseTest
import com.github.tsa.data.local.util.InsertCombo
import com.github.tsa.data.local.util.assertWorkoutsAreEqual
import com.github.tsa.data.workout3NotInDB
import com.github.tsa.data.workout4NotInDB
import com.github.tsa.domain.model.Workout
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
    fun `Flow should emit the most recently inserted Workout`() = testScope.runTest {
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
    fun `Insert Workout`() = testScope.runTest {
        val toBeInserted = workout3NotInDB

        val id = insert(toBeInserted, audioAttributesDao, techniqueDao, comboDao, workoutDao)

        id shouldBe lastInsertedRowId

        val retrieved = workoutDao.getWorkout(id)

        assertWorkoutsAreEqual(retrieved, toBeInserted)
    }

    @Test
    fun `If the provided Workout already is saved in the database, update it`() =
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
    fun `If the provided Workout already is saved in the database, update its comboList`() =
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
    fun `If the provided Workout already is saved in the database, delete it`() =
        testScope.runTest {
            val id = 1L

            val affectedRows = workoutDao.delete(id)
            affectedRows shouldBe 1L

            val afterDelete = workoutDao.getWorkout(1L)
            afterDelete shouldBe null
        }

    @Test
    fun `If the provided workouts already are saved in the database, delete them`() =
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