package com.thestrikingarts.domain.combo

import com.thestrikingarts.data.jabCrossJab
import com.thestrikingarts.data.local.util.assertCombosAreEqual
import com.thestrikingarts.data.repository.FakeComboRepository
import com.thestrikingarts.data.stepForwardSpearElbowNotInDB
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UpsertComboUseCaseTest {
    private val repository = FakeComboRepository()
    private val useCase = UpsertComboUseCase(repository)

    @Test
    fun `If the provided combo is not already in the database, insert it`() = runTest {
        val combo = stepForwardSpearElbowNotInDB.copy(id = 0)

        useCase(combo, emptyList())

        assertCombosAreEqual(repository.getLastInsertedCombo(), combo)
    }

    @Test
    fun `If the provided combo is already in the database, update it`() = runTest {
        val newName = "ComboNameCopied"
        useCase(jabCrossJab.copy(name = newName), emptyList())

        repository.getCombo(jabCrossJab.id).name shouldBe newName
    }
}