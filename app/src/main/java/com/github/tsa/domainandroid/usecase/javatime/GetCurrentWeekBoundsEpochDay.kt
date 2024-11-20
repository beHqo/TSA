package com.github.tsa.domainandroid.usecase.javatime

import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

class GetCurrentWeekBoundsEpochDay @Inject constructor() {
    operator fun invoke(): Pair<Long, Long> {
        val currentWeek = WeekFields.of(Locale.getDefault())
        val firstDayOfWeek = currentWeek.firstDayOfWeek

        val firstDayOfWeekDate =
            LocalDate.now().with(TemporalAdjusters.previousOrSame(firstDayOfWeek))

        return Pair(firstDayOfWeekDate.toEpochDay(), firstDayOfWeekDate.plusDays(6).toEpochDay())
    }
}