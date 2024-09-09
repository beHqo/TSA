package com.example.android.strikingarts.domain.usecase.combo

import com.example.android.strikingarts.data.jabCrossJab
import com.example.android.strikingarts.data.local.util.assertCombosAreEqual
import com.example.android.strikingarts.data.repository.FakeComboRepository
import com.example.android.strikingarts.domain.model.Combo
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RetrieveComboUseCaseTest {
    private val repository = FakeComboRepository()
    private val useCase = RetrieveComboUseCase(repository)

    @Test
    fun `Given a database pre-populated with Combo objects, When an id of a combo that already exists in the database is supplied, Then it should be retrieved`() =
        runTest {
            val combo = jabCrossJab

            val retrieved = useCase(combo.id)

            assertCombosAreEqual(retrieved, combo)
        }

    @Test
    fun `Given a database pre-populated with Combo objects, When an id of a combo that does not exists in the database is supplied, Then a Combo with default values should be retrieved`() =
        runTest {
            val retrieved = useCase(77)

            assertCombosAreEqual(retrieved, Combo())
        }
}