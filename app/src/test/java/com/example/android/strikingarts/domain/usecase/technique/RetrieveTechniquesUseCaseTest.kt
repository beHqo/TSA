package com.example.android.strikingarts.domain.usecase.technique

import app.cash.turbine.test
import com.example.android.strikingarts.data.repository.FakeTechniqueRepository
import com.example.android.strikingarts.data.slashingElbow
import com.example.android.strikingarts.data.spearElbow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RetrieveTechniquesUseCaseTest {
    private val repository = FakeTechniqueRepository()
    private val useCase = RetrieveTechniquesUseCase(repository)

    @Test
    fun `Given the techniqueList Flow, When objects are inserted in the database, Then Flow should emit`() =
        runTest {
            val flow = useCase.techniqueList

            flow.test {
                repository.insert(spearElbow, null)
                awaitItem().last() shouldBe spearElbow
                awaitComplete()
            }

            flow.test {
                repository.insert(slashingElbow, null)
                awaitItem().last() shouldBe slashingElbow
                awaitComplete()
            }
        }
}