package com.example.android.strikingarts.domain.usecase.training

import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import com.example.android.strikingarts.data.longCombo
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ComboVisualPlayerUseCaseTest {
    private val testDispatchers = StandardTestDispatcher()
    private val testScope = TestScope(testDispatchers)
    private val useCase = ComboVisualPlayerUseCase(testDispatchers)

    @Test
    fun `SUT should emit colorStrings of the given combo and set isPlaying to true`() = doTest {
        useCase.isPlaying.value shouldBe false

        useCase.display(longCombo)

        useCase.isPlaying.value shouldBe true

        useCase.currentCombo.value shouldBe longCombo

        delay(1)
        longCombo.techniqueList.forEach { technique ->
            val actual = expectMostRecentItem()
            val expected = technique.color

            actual shouldBe expected

            delay(technique.audioAttributes.durationMillis)
        }

        expectNoEvents()
    }

    @Test
    fun `SUT should stop emitting new values when paused and set isPlaying to false`() = doTest {
        useCase.display(longCombo)

        useCase.isPlaying.value shouldBe true

        useCase.pause()

        useCase.isPlaying.value shouldBe false

        expectNoEvents()

        useCase.display(longCombo)

        useCase.isPlaying.value shouldBe true

        delay(1)
        longCombo.techniqueList.forEach { technique ->
            val actual = expectMostRecentItem()
            val expected = technique.color

            actual shouldBe expected

            delay(technique.audioAttributes.durationMillis)
        }
    }

    private fun doTest(testBlock: suspend TurbineTestContext<String>.() -> Unit) =
        runTest(context = testDispatchers) {
            testScope.launch { useCase.currentColorString.test(validate = testBlock) }
        }
}