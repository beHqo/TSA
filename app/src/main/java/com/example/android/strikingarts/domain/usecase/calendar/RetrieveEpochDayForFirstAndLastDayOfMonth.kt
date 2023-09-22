package com.example.android.strikingarts.domain.usecase.calendar

import java.time.YearMonth
import javax.inject.Inject
import kotlin.math.absoluteValue

class RetrieveEpochDayForFirstAndLastDayOfMonth @Inject constructor() {
    /**
     * Calculates the EpochDay for first and last day of a month.
     *
     * @param [beforeOrAfter]: An integer referring to the month before (if negative) or after
     * (if positive) the current month. If left at 0, the returned value belongs to the current
     * month.
     *
     */

    operator fun invoke(beforeOrAfter: Long = 0): Pair<Long, Long> {
        val month = when {
            beforeOrAfter > 0 -> YearMonth.now().plusMonths(beforeOrAfter)
            beforeOrAfter < 0 -> YearMonth.now().minusMonths(beforeOrAfter.absoluteValue)
            else -> YearMonth.now()
        }

        return Pair(month.atDay(1).toEpochDay(), month.atEndOfMonth().toEpochDay())
    }
}