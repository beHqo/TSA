package com.example.android.strikingarts.data.local.dao

import com.example.android.strikingarts.data.jabAudioAttributes
import com.example.android.strikingarts.data.local.BaseDatabaseTest
import com.example.android.strikingarts.data.local.assertAudioAttributesAreEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AudioAttributesDaoTest : BaseDatabaseTest() {
    private val dao = AudioAttributesDao(database, testDispatcher, testDispatcher)

    @Test
    fun `Given an AudioAttributes object, When inserted in the database, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            dao.insert(jabAudioAttributes)
            val retrieved = dao.getAudioAttributesById(jabAudioAttributes.id)

            assertAudioAttributesAreEqual(retrieved, jabAudioAttributes)
        }

    @Test
    fun `Given an AudioAttributes object that is in the database, When updated, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            val id = dao.insert(jabAudioAttributes)

            val updatedName = "JabCopied"
            val updated = jabAudioAttributes.copy(name = updatedName)
            dao.update(updated)

            val retrieved = dao.getAudioAttributesById(id)

            retrieved shouldNotBe null
            retrieved?.name shouldBe updatedName
        }

    @Test
    fun `Given an AudioAttributes object that is in the database, When queried using path, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            dao.insert(jabAudioAttributes)

            val retrieved = dao.getAudioAttributesByPath(jabAudioAttributes.audioString)

            assertAudioAttributesAreEqual(retrieved, jabAudioAttributes)
        }
}