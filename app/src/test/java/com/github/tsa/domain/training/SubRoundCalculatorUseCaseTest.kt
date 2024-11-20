package com.github.tsa.domain.training

import io.kotest.matchers.shouldBe
import org.junit.Test

class SubRoundCalculatorUseCaseTest {
    private val useCase = SubRoundCalculatorUseCase()

    @Test
    fun `Given a value for subRounds that is divisible by roundsLengthSeconds, When operator function is invoked, Then the returned map should contain whole integers as keys and zero as values`() {
        val retrieved = useCase.calculateSubRoundIntersects(30, 3)

        val intersects = mapOf(10 to 0, 20 to 0)

        retrieved shouldBe intersects
    }

    @Test
    fun `Given a value for subRounds that is not divisible by roundsLengthSeconds, When operator function is invoked, Then the returned map should contain seconds as keys and milliseconds as values`() {
        val retrieved = useCase.calculateSubRoundIntersects(10, 4)

        val intersects = mapOf(2 to 500, 5 to 0, 7 to 500)

        retrieved shouldBe intersects
    }
}