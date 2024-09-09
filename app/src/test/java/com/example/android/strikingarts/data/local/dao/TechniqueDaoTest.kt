package com.example.android.strikingarts.data.local.dao

import app.cash.turbine.test
import com.example.android.strikingarts.data.audioAttributesList
import com.example.android.strikingarts.data.cross
import com.example.android.strikingarts.data.jab
import com.example.android.strikingarts.data.jabAudioAttributes
import com.example.android.strikingarts.data.jabNotInDB
import com.example.android.strikingarts.data.leadHook
import com.example.android.strikingarts.data.listOfTechniques
import com.example.android.strikingarts.data.local.BaseDatabaseTest
import com.example.android.strikingarts.data.local.util.assertTechniquesAreEqual
import com.example.android.strikingarts.data.spearElbowNotInDB
import com.example.android.strikingarts.domain.model.SilenceAudioAttributes
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TechniqueDaoTest : BaseDatabaseTest() {
    private val techniqueDao = TechniqueDao(database, ioDispatcherTest, defaultDispatcherTest)
    private val audioAttributesDao =
        AudioAttributesDao(database, ioDispatcherTest, ioDispatcherTest)
    private val lastInsertedRowId = listOfTechniques.size + 1

    @Before
    fun setup() {
        testScope.launch {
            listOfTechniques.forEach { technique ->
                techniqueDao.insert(technique, technique.audioAttributes.id)
            }

            audioAttributesList.forEach { audioAttributes ->
                audioAttributesDao.insert(audioAttributes)
            }
        }
    }

    @Test
    fun `Given a flow, When new objects are inserted, Then flow should emit`() = testScope.runTest {
        val flow = techniqueDao.getTechniqueList

        flow.test {
            techniqueDao.insert(spearElbowNotInDB, null)
            assertTechniquesAreEqual(awaitItem().last(), spearElbowNotInDB)
        }

        flow.test {
            techniqueDao.insert(jabNotInDB, jabAudioAttributes.id)
            assertTechniquesAreEqual(awaitItem().last(), jabNotInDB)
        }
    }

    @Test
    fun `Given a Technique object, When it's inserted in the database, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            val toBeInserted = jabNotInDB

            val jabId = techniqueDao.insert(toBeInserted, toBeInserted.audioAttributes.id)
            jabId shouldBe lastInsertedRowId

            val retrieved = techniqueDao.getTechnique(jabId)
            assertTechniquesAreEqual(retrieved, toBeInserted)
        }

    @Test
    fun `Given a Technique object that is in the database, When updated, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            val jabId = jab.id

            val retrieved = techniqueDao.getTechnique(jabId)
            retrieved shouldNotBe null

            val updatedName = "Copied"
            val toBeUpdated =
                retrieved!!.copy(name = updatedName, audioAttributes = SilenceAudioAttributes)
            val affectedRows = techniqueDao.update(toBeUpdated, null)

            affectedRows shouldBe 1

            val updated = techniqueDao.getTechnique(jabId)

            updated!!.name shouldBe updatedName
            updated.audioAttributes shouldBe SilenceAudioAttributes
        }

    @Test
    fun `Given a Technique object that is in the database, When deleted, Then retrieved and confirmed to be null`() =
        testScope.runTest {
            val jabId = jab.id

            val affectedRows = techniqueDao.delete(jabId)
            affectedRows shouldBe 1

            val retrieved = techniqueDao.getTechnique(jabId)
            retrieved shouldBe null
        }

    @Test
    fun `Given a Technique object that is in the database, When deleted, Then its AudioAttributes is also deleted`() =
        testScope.runTest {
            val jabId = jab.id

            techniqueDao.delete(jabId)

            val removedJabAudioAttributes =
                audioAttributesDao.getAudioAttributesById(jabAudioAttributes.id)

            removedJabAudioAttributes shouldBe null
        }

    @Test
    fun `Given several Technique objects that are in the database, When deleted, Then retrieved and confirmed to be null`() =
        testScope.runTest {
            val jabId = jab.id
            val crossId = cross.id
            val leadHookId = leadHook.id

            val affectedRows = techniqueDao.deleteAll(listOf(jabId, crossId, leadHookId))
            affectedRows shouldBe 3

            val removedJab = techniqueDao.getTechnique(jabId)
            val removedCross = techniqueDao.getTechnique(crossId)
            val removedLeadHook = techniqueDao.getTechnique(leadHookId)

            removedJab shouldBe null
            removedCross shouldBe null
            removedLeadHook shouldBe null
        }
}