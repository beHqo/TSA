package com.example.android.strikingarts.domain.usecase.technique

import app.cash.turbine.test
import com.example.android.strikingarts.data.repository.FakeTechniqueRepository
import com.example.android.strikingarts.domain.model.MovementType
import com.example.android.strikingarts.domain.model.TechniqueType
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FilterTechniquesUseCaseTest {
    private val testDispatchers = StandardTestDispatcher()
    private val repository = FakeTechniqueRepository()
    private val useCase =
        FilterTechniquesUseCase(RetrieveTechniquesUseCase(repository), testDispatchers)

    @Test
    fun `Given a Flow that emits Technique objects stored in a database, When a MovementType value is presented, Then the flow should only contain Techniques with the specified MovementType value`() =
        runTest(testDispatchers) {
            val flow = useCase.techniqueList

            useCase.setMovementType(MovementType.DEFENSE)
            flow.test { awaitItem().forEach { it.movementType shouldBe MovementType.DEFENSE } }

            useCase.setMovementType(MovementType.OFFENSE)
            flow.test { awaitItem().forEach { it.movementType shouldBe MovementType.OFFENSE } }
        }

    @Test
    fun `Given a Flow that emits Technique objects stored in a database, When a TechniqueType value is presented, Then the flow should only contain Techniques with the specified TechniqueType value`() =
        runTest(testDispatchers) {
            val flow = useCase.techniqueList

            useCase.setTechniqueType(TechniqueType.KICK)
            flow.test { awaitItem().forEach { it.techniqueType shouldBe TechniqueType.KICK } }

            useCase.setTechniqueType(TechniqueType.PUNCH)
            flow.test { awaitItem().forEach { it.techniqueType shouldBe TechniqueType.PUNCH } }
        }
}