package com.github.tsa.data.local.dao

import com.github.tsa.data.audioAttributesList
import com.github.tsa.data.backFistAudioAttributesNotInDB
import com.github.tsa.data.jabAudioAttributes
import com.github.tsa.data.local.BaseDatabaseTest
import com.github.tsa.data.local.util.assertAudioAttributesAreEqual
import com.github.tsa.data.nutKickAudioAttributesNotInDB
import com.github.tsa.domain.model.UriAudioAttributes
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
    fun `Insert UriAudioAttributes`() =
        testScope.runTest {
            val toBeInserted = backFistAudioAttributesNotInDB

            val id = dao.insert(toBeInserted)

            id shouldBe lastInsertedRowId

            val retrieved = dao.getAudioAttributesById(id)

            assertAudioAttributesAreEqual(retrieved, toBeInserted)
        }

    @Test
    fun `Insert ResourceAudioAttributes`() =
        testScope.runTest {
            val toBeInserted = nutKickAudioAttributesNotInDB

            val id = dao.insert(toBeInserted)

            id shouldBe lastInsertedRowId

            val retrieved = dao.getAudioAttributesById(id)

            assertAudioAttributesAreEqual(retrieved, toBeInserted)
        }

    @Test
    fun `Update UriAudioAttributes`() =
        testScope.runTest {
            val id = audioAttributesList.filterIsInstance<UriAudioAttributes>().first().id
            val toBeUpdated = dao.getAudioAttributesById(id)!!
            val updatedName = "Updated"

            if (toBeUpdated is UriAudioAttributes) {
                val updated = toBeUpdated.copy(name = updatedName)
                val affectedRows = dao.update(updated)

                affectedRows shouldBe 1L
            }

            val retrieved = dao.getAudioAttributesById(id)

            retrieved?.name shouldBe updatedName
        }

    @Test
    fun `Query AudioAttributes via path`() =
        testScope.runTest {
            val retrieved = dao.getAudioAttributesByPath(jabAudioAttributes.audioString)

            assertAudioAttributesAreEqual(retrieved, jabAudioAttributes)
        }
}