package com.github.tsa.domain.combo

import com.github.tsa.data.jabCrossJab
import com.github.tsa.data.local.util.assertCombosAreEqual
import com.github.tsa.data.repository.FakeComboRepository
import com.github.tsa.domain.model.Combo
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RetrieveComboUseCaseTest {
    private val repository = FakeComboRepository()
    private val useCase = RetrieveComboUseCase(repository)

    @Test
    fun `Retrieve a combo by its id`() = runTest {
        val combo = jabCrossJab

        val retrieved = useCase(combo.id)

        assertCombosAreEqual(retrieved, combo)
    }

    @Test
    fun `Return a default combo object when the provided id is not referring to anything in the database`() =
        runTest {
            val retrieved = useCase(77)

            assertCombosAreEqual(retrieved, Combo())
        }
}