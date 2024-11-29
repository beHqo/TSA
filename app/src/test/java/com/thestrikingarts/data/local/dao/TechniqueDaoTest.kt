package com.thestrikingarts.data.local.dao

import app.cash.turbine.test
import com.thestrikingarts.data.audioAttributesList
import com.thestrikingarts.data.cross
import com.thestrikingarts.data.jab
import com.thestrikingarts.data.jabAudioAttributes
import com.thestrikingarts.data.jabNotInDB
import com.thestrikingarts.data.leadHook
import com.thestrikingarts.data.listOfTechniques
import com.thestrikingarts.data.local.BaseDatabaseTest
import com.thestrikingarts.data.local.util.assertTechniquesAreEqual
import com.thestrikingarts.data.spearElbowNotInDB
import com.thestrikingarts.domain.model.SilenceAudioAttributes
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
    fun `Flow should emit the most recently inserted Technique`() = testScope.runTest {
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
    fun `Insert Technique`() = testScope.runTest {
        val toBeInserted = jabNotInDB

        val jabId = techniqueDao.insert(toBeInserted, toBeInserted.audioAttributes.id)
        jabId shouldBe lastInsertedRowId

        val retrieved = techniqueDao.getTechnique(jabId)
        assertTechniquesAreEqual(retrieved, toBeInserted)
    }

    @Test
    fun `If the provided Technique already is saved in the database, update it`() =
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
    fun `If the provided Technique already is saved in the database, delete it`() =
        testScope.runTest {
            val jabId = jab.id

            val affectedRows = techniqueDao.delete(jabId)
            affectedRows shouldBe 1

            val retrieved = techniqueDao.getTechnique(jabId)
            retrieved shouldBe null
        }

    @Test
    fun `If the provided Technique already is saved in the database and its AudioAttributes is not used by any other technique, delete both the technique and the AudioAttributes`() =
        testScope.runTest {
            val jabId = jab.id

            techniqueDao.delete(jabId)

            val removedJabAudioAttributes =
                audioAttributesDao.getAudioAttributesById(jabAudioAttributes.id)

            removedJabAudioAttributes shouldBe null
        }

    @Test
    fun `If the provided techniques already are saved in the database, delete them`() =
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