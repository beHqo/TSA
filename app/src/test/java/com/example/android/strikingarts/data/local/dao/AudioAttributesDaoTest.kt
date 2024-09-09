package com.example.android.strikingarts.data.local.dao

import com.example.android.strikingarts.data.audioAttributesList
import com.example.android.strikingarts.data.backFistAudioAttributesNotInDB
import com.example.android.strikingarts.data.jabAudioAttributes
import com.example.android.strikingarts.data.local.BaseDatabaseTest
import com.example.android.strikingarts.data.local.util.assertAudioAttributesAreEqual
import com.example.android.strikingarts.domain.model.UriAudioAttributes
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AudioAttributesDaoTest : BaseDatabaseTest() {
    private val dao = AudioAttributesDao(database, ioDispatcherTest, defaultDispatcherTest)
    private val lastInsertedRowId = audioAttributesList.size + 1

    @Before
    fun setup() {
        testScope.launch {
            audioAttributesList.forEach { audioAttributes -> dao.insert(audioAttributes) }
        }
    }

    @Test
    fun `Given an AudioAttributes object, When inserted in the database, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            val id = dao.insert(backFistAudioAttributesNotInDB)

            id shouldBe lastInsertedRowId

            val retrieved = dao.getAudioAttributesById(id)

            assertAudioAttributesAreEqual(retrieved, backFistAudioAttributesNotInDB)
        }

    @Test
    fun `Given an AudioAttributes object that is in the database, When updated, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            val id = 1L
            val toBeUpdated =
                dao.getAudioAttributesById(id)!! as UriAudioAttributes // At this time only UriAudioAttributes were in DB
            val updatedName = "Updated"
            val updated = toBeUpdated.copy(name = updatedName)
            val affectedRows = dao.update(updated)

            affectedRows shouldBe 1L

            val retrieved = dao.getAudioAttributesById(id)

            retrieved?.name shouldBe updatedName
        }

    @Test
    fun `Given an AudioAttributes object that is in the database, When queried using path, Then retrieved and its correctness confirmed`() =
        testScope.runTest {
            val retrieved = dao.getAudioAttributesByPath(jabAudioAttributes.audioString)

            assertAudioAttributesAreEqual(retrieved, jabAudioAttributes)
        }
}