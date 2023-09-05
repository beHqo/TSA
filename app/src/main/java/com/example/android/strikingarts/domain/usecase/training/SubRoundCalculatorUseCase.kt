package com.example.android.strikingarts.domain.usecase.training

import javax.inject.Inject
import kotlin.math.roundToInt

class SubRoundCalculatorUseCase @Inject constructor() {
    fun calculateSubRoundIntersects(roundLengthSeconds: Int, subRounds: Int): Map<Int, Long> {
        val timeMap = mutableMapOf<Int, Long>()
        val subRoundDuration = roundToThreeDecimals(roundLengthSeconds / subRounds.toFloat())

        var currentIntervalTime = roundToThreeDecimals(roundLengthSeconds - subRoundDuration)
        while (currentIntervalTime > 1) {
            timeMap += calculateDelayAndAddToTimeMap(currentIntervalTime)

            currentIntervalTime = roundToThreeDecimals(currentIntervalTime - subRoundDuration)
        }

        return timeMap
    }

    private fun roundToThreeDecimals(num: Float): Float = ((num * 1000).roundToInt() / 1000F)

    private fun calculateDelayAndAddToTimeMap(num: Float): Pair<Int, Long> {
        val whole = num.toInt()
        val fraction = ((num - whole) * 1000).toLong()

        return Pair(
            first = if (fraction == 0L) whole else whole + 1,
            second = if (fraction == 0L) 0L else 1000 - fraction
        )
    }
}