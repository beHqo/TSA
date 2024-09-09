package com.example.android.strikingarts.domain.usecase.technique

import com.example.android.strikingarts.data.cross
import com.example.android.strikingarts.data.jab
import com.example.android.strikingarts.data.jabNotInDB
import com.example.android.strikingarts.data.leadHook
import com.example.android.strikingarts.data.repository.FakeTechniqueRepository
import com.example.android.strikingarts.data.slashingElbowNotInDB
import com.example.android.strikingarts.data.spearElbowNotInDB
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteTechniqueUseCaseTest {
    private val repository = FakeTechniqueRepository()
    private val useCase = DeleteTechniqueUseCase(repository)

    @Test
    fun `Given an id of a technique that is already in the database, When delete operation is performed, Then confirm the object is deleted`() =
        runTest {
            useCase(cross.id)

            repository.doesDatabaseContainTechniqueWithIdOf(cross.id) shouldBe false
        }

    @Test
    fun `Given a list of ids of a techniques that are already in the database, When delete operation is performed, Then confirm the object is deleted`() =
        runTest {
            useCase(listOf(jab.id, leadHook.id))

            repository.doesDatabaseContainTechniqueWithIdOf(jab.id) shouldBe false
            repository.doesDatabaseContainTechniqueWithIdOf(leadHook.id) shouldBe false
        }

    @Test
    fun `Given a database pre-populated with Combo objects, When an id of a combo that does not exist in the database is supplied, Then it should be removed`() =
        runTest {
            val toBeDeleted = jabNotInDB

            val affectedRows = useCase(toBeDeleted.id)
            affectedRows shouldBe 0L
        }

    @Test
    fun `Given a database pre-populated with Combo objects, When a list of ids of combos that do not exist in the database is supplied, Then all the combos should be removed`() =
        runTest {
            val list = listOf(spearElbowNotInDB.id, slashingElbowNotInDB.id)

            val affectedRows = useCase(list)
            affectedRows shouldBe 0L
        }
}