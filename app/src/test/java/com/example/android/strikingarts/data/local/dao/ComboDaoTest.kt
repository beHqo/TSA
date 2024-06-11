package com.example.android.strikingarts.data.local.dao

import app.cash.turbine.test
import com.example.android.strikingarts.data.cross
import com.example.android.strikingarts.data.crossAudioAttributes
import com.example.android.strikingarts.data.crossStepBackCrossLeadHook
import com.example.android.strikingarts.data.jab
import com.example.android.strikingarts.data.jabAudioAttributes
import com.example.android.strikingarts.data.jabCrossJab
import com.example.android.strikingarts.data.leadHighKick
import com.example.android.strikingarts.data.leadHook
import com.example.android.strikingarts.data.leadHookAudioAttributes
import com.example.android.strikingarts.data.local.BaseDatabaseTest
import com.example.android.strikingarts.data.local.assertCombosAreEqual
import com.example.android.strikingarts.data.spearElbow
import com.example.android.strikingarts.data.stepBack
import com.example.android.strikingarts.data.stepBackLeadHighKick
import com.example.android.strikingarts.data.stepForward
import com.example.android.strikingarts.data.stepForwardSpearElbow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ComboDaoTest : BaseDatabaseTest() {
    private val audioAttributesDao = AudioAttributesDao(database, testDispatcher, testDispatcher)
    private val techniqueDao = TechniqueDao(database, testDispatcher, testDispatcher)
    private val comboDao = ComboDao(database, testDispatcher, testDispatcher)

    @Test
    fun `Given a flow, When new objects are inserted, Then flow should emit`() = testScope.runTest {
        val flow = comboDao.comboList

        flow.test {
            val stepBackId = techniqueDao.insert(stepBack, null)
            val leadHighKickId = techniqueDao.insert(leadHighKick, null)
            comboDao.insert(stepBackLeadHighKick, listOf(stepBackId, leadHighKickId))

            assertCombosAreEqual(awaitItem()[0], stepBackLeadHighKick)
        }

        flow.test {
            val stepForwardId = techniqueDao.insert(stepForward, null)
            val spearElbowId = techniqueDao.insert(spearElbow, null)
            comboDao.insert(stepForwardSpearElbow, listOf(stepForwardId, spearElbowId))

            assertCombosAreEqual(awaitItem()[1], stepForwardSpearElbow)
        }
    }

    @Test
    fun `Given a Combo object, When it's inserted in the database, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            val jabAAId = audioAttributesDao.insert(jabAudioAttributes)
            val crossAAId = audioAttributesDao.insert(crossAudioAttributes)
            val jabId = techniqueDao.insert(jab, jabAAId)
            val crossId = techniqueDao.insert(cross, crossAAId)

            val comboId = comboDao.insert(jabCrossJab, listOf(jabId, crossId, jabId))
            comboId shouldBe 1

            val retrieved = comboDao.getComboWithTechniques(comboId)
            assertCombosAreEqual(retrieved, jabCrossJab)
        }

    @Test
    fun `Given a Combo object that is in the database, When updated, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            val crossAAId = audioAttributesDao.insert(crossAudioAttributes)
            val leadHookAAId = audioAttributesDao.insert(leadHookAudioAttributes)
            val crossId = techniqueDao.insert(cross, crossAAId)
            val stepBackId = techniqueDao.insert(stepBack, null)
            val leadHookId = techniqueDao.insert(leadHook, leadHookAAId)

            val techniqueIdList = listOf(crossId, stepBackId, crossId, leadHookId)
            val comboId = comboDao.insert(crossStepBackCrossLeadHook, techniqueIdList)
            comboId shouldBe 1

            val retrieved = comboDao.getComboWithTechniques(comboId)
            retrieved shouldNotBe null

            val affectedRows =
                comboDao.update(retrieved!!.copy(name = "comboCopied"), techniqueIdList)
            affectedRows shouldBe 1

            val updated = comboDao.getComboWithTechniques(comboId)
            updated?.name shouldBe "comboCopied"
        }

    @Test
    fun `Given a Combo object that is in the database, When its techniqueList is updated, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            val jabAAId = audioAttributesDao.insert(jabAudioAttributes)
            val crossAAId = audioAttributesDao.insert(crossAudioAttributes)
            val jabId = techniqueDao.insert(jab, jabAAId)
            val crossId = techniqueDao.insert(cross, crossAAId)

            val comboId = comboDao.insert(jabCrossJab, listOf(jabId, crossId, jabId))
            comboId shouldBe 1

            val retrieved = comboDao.getComboWithTechniques(comboId)

            val updatedTechniqueIdList = listOf(jabId, jabId)
            val affectedRows = comboDao.update(retrieved!!, listOf(jabId, jabId))
            affectedRows shouldBe 1

            val updated = comboDao.getComboWithTechniques(comboId)
            updated!!.techniqueList.map { it.id } shouldBe updatedTechniqueIdList
        }

    @Test
    fun `Given a Combo object that is in the database, When deleted, Then retrieved and confirmed to be null`() =
        testScope.runTest {
            val stepBackId = techniqueDao.insert(stepBack, null)
            val leadHighKickId = techniqueDao.insert(leadHighKick, null)

            val comboId = comboDao.insert(stepBackLeadHighKick, listOf(stepBackId, leadHighKickId))
            comboId shouldBe 1

            val affectedRows = comboDao.delete(comboId)
            affectedRows shouldBe 1

            val retrieved = comboDao.getComboWithTechniques(comboId)
            retrieved shouldBe null
        }

    @Test
    fun `Given several Combo objects that are in the database, When deleted, Then retrieved and confirmed to be null`() =
        testScope.runTest {
            val stepBackId = techniqueDao.insert(stepBack, null)
            val leadHighKickId = techniqueDao.insert(leadHighKick, null)
            val stepForwardId = techniqueDao.insert(stepForward, null)
            val spearElbowId = techniqueDao.insert(spearElbow, null)

            val comboId1 = comboDao.insert(stepBackLeadHighKick, listOf(stepBackId, leadHighKickId))
            val comboId2 =
                comboDao.insert(stepForwardSpearElbow, listOf(stepForwardId, spearElbowId))

            val affectedRows = comboDao.deleteAll(listOf(comboId1, comboId2))
            affectedRows shouldBe 2

            val removed1 = comboDao.getComboWithTechniques(comboId1)
            removed1 shouldBe null
            val removed2 = comboDao.getComboWithTechniques(comboId2)
            removed2 shouldBe null
        }
}