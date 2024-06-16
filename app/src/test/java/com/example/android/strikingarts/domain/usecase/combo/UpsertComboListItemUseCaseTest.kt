package com.example.android.strikingarts.domain.usecase.combo

import com.example.android.strikingarts.data.jabCrossJab
import com.example.android.strikingarts.data.local.assertCombosAreEqual
import com.example.android.strikingarts.data.repository.FakeComboRepository
import com.example.android.strikingarts.data.stepForwardSpearElbow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UpsertComboListItemUseCaseTest {
    private val repository = FakeComboRepository()
    private val useCase = UpsertComboListItemUseCase(repository)

    @Test
    fun `Given a database populated with Combo objects, When a new Combo is supplied, Then it should be inserted`() =
        runTest {
            val combo = stepForwardSpearElbow.copy(id = 0)

            useCase(combo, emptyList())

            assertCombosAreEqual(repository.getLastInsertedCombo(), combo)
        }

    @Test
    fun `Given a database populated with Combo objects, When a Combo that is already in the database is supplied, Then it should be updated`() =
        runTest {
            val newName = "ComboNameCopied"
            useCase(jabCrossJab.copy(name = newName), emptyList())

            repository.getCombo(jabCrossJab.id).name shouldBe newName
        }
}