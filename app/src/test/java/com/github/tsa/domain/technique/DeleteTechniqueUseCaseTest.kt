package com.github.tsa.domain.technique

import com.github.tsa.data.cross
import com.github.tsa.data.jab
import com.github.tsa.data.jabNotInDB
import com.github.tsa.data.leadHook
import com.github.tsa.data.repository.FakeTechniqueRepository
import com.github.tsa.data.slashingElbowNotInDB
import com.github.tsa.data.spearElbowNotInDB
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteTechniqueUseCaseTest {
    private val repository = FakeTechniqueRepository()
    private val useCase = DeleteTechniqueUseCase(repository)

    @Test
    fun `Delete the provided technique`() = runTest {
        useCase(cross.id)

        repository.doesDatabaseContainTechniqueWithIdOf(cross.id) shouldBe false
    }

    @Test
    fun `Delete the provided techniques`() = runTest {
        useCase(listOf(jab.id, leadHook.id))

        repository.doesDatabaseContainTechniqueWithIdOf(jab.id) shouldBe false
        repository.doesDatabaseContainTechniqueWithIdOf(leadHook.id) shouldBe false
    }

    @Test
    fun `When provided with a technique that is not in already the database, do nothing`() =
        runTest {
            val toBeDeleted = jabNotInDB

            val affectedRows = useCase(toBeDeleted.id)
            affectedRows shouldBe 0L
        }

    @Test
    fun `When provided with techniques that is not in already the database, do nothing`() =
        runTest {
            val list = listOf(spearElbowNotInDB.id, slashingElbowNotInDB.id)

            val affectedRows = useCase(list)
            affectedRows shouldBe 0L
        }
}