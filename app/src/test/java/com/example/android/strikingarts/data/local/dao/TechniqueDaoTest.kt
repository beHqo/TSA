package com.example.android.strikingarts.data.local.dao

import app.cash.turbine.test
import com.example.android.strikingarts.data.cross
import com.example.android.strikingarts.data.crossAudioAttributes
import com.example.android.strikingarts.data.jab
import com.example.android.strikingarts.data.jabAudioAttributes
import com.example.android.strikingarts.data.leadHook
import com.example.android.strikingarts.data.leadHookAudioAttributes
import com.example.android.strikingarts.data.local.BaseDatabaseTest
import com.example.android.strikingarts.data.local.assertTechniquesAreEqual
import com.example.android.strikingarts.data.stepBack
import com.example.android.strikingarts.domain.model.SilenceAudioAttributes
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TechniqueDaoTest : BaseDatabaseTest() {
    private val techniqueDao = TechniqueDao(database, testDispatcher, testDispatcher)
    private val audioAttributesDao = AudioAttributesDao(database, testDispatcher, testDispatcher)

    @Test
    fun `Given a flow, When new objects are inserted, Then flow should emit`() = testScope.runTest {
        val flow = techniqueDao.getTechniqueList

        flow.test {
            audioAttributesDao.insert(jabAudioAttributes)
            techniqueDao.insert(jab, jabAudioAttributes.id)
            assertTechniquesAreEqual(awaitItem()[0], jab)
        }

        flow.test {
            audioAttributesDao.insert(crossAudioAttributes)
            techniqueDao.insert(cross, crossAudioAttributes.id)
            assertTechniquesAreEqual(awaitItem()[1], cross)
        }

        flow.test {
            audioAttributesDao.insert(leadHookAudioAttributes)
            techniqueDao.insert(leadHook, leadHookAudioAttributes.id)
            assertTechniquesAreEqual(awaitItem()[2], leadHook)
        }

        flow.test {
            techniqueDao.insert(stepBack, null)
            assertTechniquesAreEqual(awaitItem()[3], stepBack)
        }
    }

    @Test
    fun `Given a Technique object, When it's inserted in the database, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            val jabAAId = audioAttributesDao.insert(jabAudioAttributes)
            val jabId = techniqueDao.insert(jab, jabAAId)
            jabId shouldBe 1

            val retrieved = techniqueDao.getTechnique(jab.id)
            assertTechniquesAreEqual(retrieved, jab)
        }

    @Test
    fun `Given a Technique object that is in the database, When updated, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            val jabAAId = audioAttributesDao.insert(jabAudioAttributes)
            val jabId = techniqueDao.insert(jab, jabAAId)

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
            val jabAAId = audioAttributesDao.insert(jabAudioAttributes)
            val jabId = techniqueDao.insert(jab, jabAAId)

            val affectedRows = techniqueDao.delete(jabId)
            affectedRows shouldBe 1

            val retrieved = techniqueDao.getTechnique(jabId)
            retrieved shouldBe null
        }

    @Test
    fun `Given a Technique object that is in the database, When deleted, Then its AudioAttributes is also deleted`() =
        testScope.runTest {
            val jabAAId = audioAttributesDao.insert(jabAudioAttributes)
            val jabId = techniqueDao.insert(jab, jabAAId)

            techniqueDao.delete(jabId)

            val removedJabAudioAttributes =
                audioAttributesDao.getAudioAttributesById(jabAudioAttributes.id)

            removedJabAudioAttributes shouldBe null
        }

    @Test
    fun `Given several Technique objects that are in the database, When deleted, Then retrieved and confirmed to be null`() =
        testScope.runTest {
            val jabAAId = audioAttributesDao.insert(jabAudioAttributes)
            val jabId = techniqueDao.insert(jab, jabAAId)
            val crossAAId = audioAttributesDao.insert(crossAudioAttributes)
            val crossId = techniqueDao.insert(cross, crossAAId)
            val leadHookAAId = audioAttributesDao.insert(leadHookAudioAttributes)
            val leadHookId = techniqueDao.insert(leadHook, leadHookAAId)

            val affectedRows = techniqueDao.deleteAll(listOf(jabId, crossId, leadHookId))
            affectedRows shouldBe 3

            val removedJabAudioAttributes = audioAttributesDao.getAudioAttributesById(jabAAId)
            val removedCrossAudioAttributes = audioAttributesDao.getAudioAttributesById(crossAAId)
            val removedLeadHookAudioAttributes =
                audioAttributesDao.getAudioAttributesById(leadHookAAId)
            removedJabAudioAttributes shouldBe null
            removedCrossAudioAttributes shouldBe null
            removedLeadHookAudioAttributes shouldBe null

            val removedJab = techniqueDao.getTechnique(jabId)
            val removedCross = techniqueDao.getTechnique(crossId)
            val removedLeadHook = techniqueDao.getTechnique(leadHookId)
            removedJab shouldBe null
            removedCross shouldBe null
            removedLeadHook shouldBe null
        }
}