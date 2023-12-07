package com.example.android.strikingarts.domain.usecase.javatime

import com.example.android.strikingarts.domain.model.WeekDay
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

class GetWeekDaysOfCurrentWeekUseCase @Inject constructor() {
    operator fun invoke(): List<WeekDay> {
        val locale = Locale.getDefault()

        val currentWeek = WeekFields.of(locale)
        val firstDayOfWeek = currentWeek.firstDayOfWeek
        val firstDayOfWeekLocalDate =
            LocalDate.now().with(TemporalAdjusters.previousOrSame(firstDayOfWeek))

        val weekDayList = mutableListOf<WeekDay>()
        for (i in 0..6L) {
            val indexedDay = firstDayOfWeekLocalDate.plusDays(i)

            weekDayList += WeekDay(
                epochDay = indexedDay.toEpochDay(),
                weekDayDisplayName = indexedDay.dayOfWeek.getDisplayName(TextStyle.SHORT, locale),
                dateDisplayName = indexedDay.format(DateTimeFormatter.ofPattern("dd"))
            )
        }

        return weekDayList
    }
}