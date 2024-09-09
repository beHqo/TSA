package com.example.android.strikingarts.data.local.dao

import app.cash.turbine.test
import com.example.android.strikingarts.data.audioAttributesList
import com.example.android.strikingarts.data.listOfCombos
import com.example.android.strikingarts.data.listOfTechniques
import com.example.android.strikingarts.data.local.BaseDatabaseTest
import com.example.android.strikingarts.data.local.util.InsertCombo
import com.example.android.strikingarts.data.local.util.assertCombosAreEqual
import com.example.android.strikingarts.data.longComboNotInDB
import com.example.android.strikingarts.data.rearHighKickStepForwardSlashingElbowNotInDB
import com.example.android.strikingarts.data.stepForwardSpearElbowNotInDB
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ComboDaoTest : BaseDatabaseTest() {
    private val audioAttributesDao =
        AudioAttributesDao(database, ioDispatcherTest, defaultDispatcherTest)
    private val techniqueDao = TechniqueDao(database, ioDispatcherTest, defaultDispatcherTest)
    private val comboDao = ComboDao(database, ioDispatcherTest, defaultDispatcherTest)
    private val lastInsertedRowId = listOfCombos.size + 1
    private val insertCombo = InsertCombo()

    @Before
    fun setup() {
        testScope.launch {
            audioAttributesList.forEach { audioAttributesDao.insert(it) }
            listOfTechniques.forEach { techniqueDao.insert(it, it.audioAttributes.id) }
            listOfCombos.forEach { comboDao.insert(it, it.techniqueList.map { tech -> tech.id }) }
        }
    }

    @Test
    fun `Given a flow, When new objects are inserted, Then flow should emit`() = testScope.runTest {
        val flow = comboDao.comboList

        flow.test {
            val toBeInserted1 = stepForwardSpearElbowNotInDB
            insertCombo(toBeInserted1, audioAttributesDao, techniqueDao, comboDao)
            assertCombosAreEqual(awaitItem().last(), toBeInserted1)
        }

        flow.test {
            val toBeInserted2 = rearHighKickStepForwardSlashingElbowNotInDB
            insertCombo(toBeInserted2, audioAttributesDao, techniqueDao, comboDao)
            assertCombosAreEqual(awaitItem().last(), toBeInserted2)
        }
    }

    @Test
    fun `Given a Combo object, When it's inserted in the database, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            val toBeInserted = stepForwardSpearElbowNotInDB

            val comboId = insertCombo(toBeInserted, audioAttributesDao, techniqueDao, comboDao)
            comboId shouldBe lastInsertedRowId

            val retrieved = comboDao.getComboWithTechniques(comboId)
            assertCombosAreEqual(retrieved, toBeInserted)
        }

    @Test
    fun `Given a Combo object that is in the database, When updated, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            val comboId = 1L

            val retrieved = comboDao.getComboWithTechniques(comboId)
            retrieved shouldNotBe null

            val updatedName = "Name updated"
            val affectedRows = comboDao.update(retrieved!!.copy(name = updatedName),
                retrieved.techniqueList.map { it.id })
            affectedRows shouldBe 1

            val updated = comboDao.getComboWithTechniques(comboId)
            updated?.name shouldBe updatedName
        }

    @Test
    fun `Given a Combo object that is in the database, When its techniqueList is updated, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            val toBeInserted = longComboNotInDB

            val comboId = insertCombo(toBeInserted, audioAttributesDao, techniqueDao, comboDao)

            val retrieved = comboDao.getComboWithTechniques(comboId)

            val updatedTechniqueIdList = retrieved!!.techniqueList.filter { it.id % 2L == 0L }

            val affectedRows = comboDao.update(retrieved, updatedTechniqueIdList.map { it.id })
            affectedRows shouldBe 1

            val updated = comboDao.getComboWithTechniques(comboId)
            updated!!.techniqueList shouldBe updatedTechniqueIdList
        }

    @Test
    fun `Given a Combo object that is in the database, When deleted, Then retrieved and confirmed to be null`() =
        testScope.runTest {
            val comboId = 1L

            val affectedRows = comboDao.delete(comboId)
            affectedRows shouldBe 1

            val retrieved = comboDao.getComboWithTechniques(comboId)
            retrieved shouldBe null
        }

    @Test
    fun `Given several Combo objects that are in the database, When deleted, Then retrieved and confirmed to be null`() =
        testScope.runTest {
            val comboIds = (1L..3L).toList()

            val affectedRows = comboDao.deleteAll(comboIds)

            affectedRows shouldBe comboIds.size

            comboIds.forEach { comboId ->
                val removed = comboDao.getComboWithTechniques(comboId)
                removed shouldBe null
            }
        }
}