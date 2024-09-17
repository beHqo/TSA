package com.example.android.strikingarts.domain.usecase.technique

import app.cash.turbine.test
import com.example.android.strikingarts.data.local.util.assertTechniquesAreEqual
import com.example.android.strikingarts.data.repository.FakeTechniqueRepository
import com.example.android.strikingarts.data.slashingElbowNotInDB
import com.example.android.strikingarts.data.spearElbowNotInDB
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RetrieveTechniquesUseCaseTest {
    private val repository = FakeTechniqueRepository()
    private val useCase = RetrieveTechniquesUseCase(repository)

    @Test
    fun `The flow should emit the newly inserted technique`() = runTest {
        val flow = useCase.techniqueList

        flow.test {
            repository.insert(spearElbowNotInDB, null)
            assertTechniquesAreEqual(awaitItem().last(), spearElbowNotInDB)
            awaitComplete()
        }

        flow.test {
            repository.insert(slashingElbowNotInDB, null)
            assertTechniquesAreEqual(awaitItem().last(), slashingElbowNotInDB)
            awaitComplete()
        }
    }
}