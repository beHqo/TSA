package com.github.tsa.domain.combo

import app.cash.turbine.test
import com.github.tsa.data.local.util.assertCombosAreEqual
import com.github.tsa.data.rearHighKickStepForwardSlashingElbowNotInDB
import com.github.tsa.data.repository.FakeComboRepository
import com.github.tsa.data.stepForwardSpearElbowNotInDB
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RetrieveComboListUseCaseTest {
    private val repository = FakeComboRepository()
    private val useCase = RetrieveComboListUseCase(repository)

    @Test
    fun `Flow should emit the values that are being inserted in the database`() = runTest {
        val flow = useCase.comboList

        flow.test {
            repository.insert(stepForwardSpearElbowNotInDB, emptyList())
            assertCombosAreEqual(awaitItem().last(), stepForwardSpearElbowNotInDB)
            awaitComplete()
        }

        flow.test {
            repository.insert(rearHighKickStepForwardSlashingElbowNotInDB, emptyList())
            assertCombosAreEqual(
                awaitItem().last(), rearHighKickStepForwardSlashingElbowNotInDB
            )
            awaitComplete()
        }
    }
}