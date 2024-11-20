package com.github.tsa.domain.technique

import app.cash.turbine.test
import com.github.tsa.data.local.util.assertTechniquesAreEqual
import com.github.tsa.data.repository.FakeTechniqueRepository
import com.github.tsa.data.slashingElbowNotInDB
import com.github.tsa.data.spearElbowNotInDB
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