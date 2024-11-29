package com.thestrikingarts.domain.technique

import app.cash.turbine.test
import com.thestrikingarts.data.repository.FakeTechniqueRepository
import com.thestrikingarts.domain.model.MovementType
import com.thestrikingarts.domain.model.TechniqueType
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
    fun `Filter techniques by MovementType`() = runTest(testDispatchers) {
        val flow = useCase.techniqueList

        useCase.setMovementType(MovementType.DEFENSE)
        flow.test { awaitItem().forEach { it.movementType shouldBe MovementType.DEFENSE } }

        useCase.setMovementType(MovementType.OFFENSE)
        flow.test { awaitItem().forEach { it.movementType shouldBe MovementType.OFFENSE } }
    }

    @Test
    fun `Filter techniques by TechniqueType`() = runTest(testDispatchers) {
        val flow = useCase.techniqueList

        useCase.setTechniqueType(TechniqueType.KICK)
        flow.test { awaitItem().forEach { it.techniqueType shouldBe TechniqueType.KICK } }

        useCase.setTechniqueType(TechniqueType.PUNCH)
        flow.test { awaitItem().forEach { it.techniqueType shouldBe TechniqueType.PUNCH } }
    }
}