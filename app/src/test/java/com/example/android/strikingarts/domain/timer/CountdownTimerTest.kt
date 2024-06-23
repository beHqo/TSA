package com.example.android.strikingarts.domain.timer

import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import com.example.android.strikingarts.ui.model.TimerState
import com.example.android.strikingarts.ui.model.TimerStatus
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CountdownTimerTest {
    private val totalTimeSeconds = 10
    private var isTimerFinished = false
    private val onTimerFinished = { isTimerFinished = true }
    private val state = MutableStateFlow(
        TimerState(
            timerStatus = TimerStatus.PLAYING,
            totalTimeSeconds = totalTimeSeconds,
            onTimerFinished = onTimerFinished
        )
    )

    private val testDispatchers = StandardTestDispatcher()
    private val timer = CountdownTimer(testDispatchers)

    @Test
    fun `Timer should emit from the given totalTimeSeconds value down to 1`() = testTimer {
        delay(1)
        testCountdown(10)
    }


    @Test
    fun `Timer should not emit any new values when it's paused`() = testTimer {
        awaitItem() shouldBe 10

        state.update { it.copy(timerStatus = TimerStatus.PAUSED) }

        expectNoEvents()
    }


    @Test
    fun `Timer should resume from the exact second it was paused at`() = testTimer {
        awaitItem() shouldBe 10

        state.update { it.copy(timerStatus = TimerStatus.PAUSED) }
        expectNoEvents()

        state.update { it.copy(timerStatus = TimerStatus.PLAYING) }
        awaitItem() shouldBe 10
    }

    @Test
    fun `Timer should invoke onTimerFinished lambda when the countdown is finished`() = testTimer {
        delay(10_001)
        expectMostRecentItem() shouldBe 1

        isTimerFinished shouldBe true
    }

    private fun testTimer(testBlock: suspend TurbineTestContext<Int>.() -> Unit) =
        runTest(testDispatchers) { timer.timerFlow(state).test(validate = testBlock) }

    private suspend fun TurbineTestContext<Int>.testCountdown(startingTimeSeconds: Int) {
        (startingTimeSeconds downTo 1).forEach { second ->
            expectMostRecentItem() shouldBe second
            delay(1001)
        }
    }
}