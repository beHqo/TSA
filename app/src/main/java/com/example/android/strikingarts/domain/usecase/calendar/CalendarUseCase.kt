package com.example.android.strikingarts.domain.usecase.calendar

import com.example.android.strikingarts.domain.model.Date
import com.example.android.strikingarts.domain.model.Month
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

class CalendarUseCase @Inject constructor() {
    private var currentMonth = YearMonth.now()

    val weekDays: List<String>
        get() {
            val weekDays = mutableListOf<String>()

            val week = WeekFields.of(Locale.getDefault())
            val firstDayOfWeek = week.firstDayOfWeek

            for (i in 0..7L) weekDays += firstDayOfWeek.plus(i)
                .getDisplayName(TextStyle.SHORT, Locale.getDefault())

            return weekDays
        }

    fun getCurrentMonth() = currentMonth.toMonth()

    fun getNextMonth(): Month {
        currentMonth = currentMonth.plusMonths(1)

        return getCurrentMonth()
    }

    fun getPreviousMonth(): Month {
        currentMonth = currentMonth.minusMonths(1)

        return getCurrentMonth()
    }
}

private fun LocalDate.toDate() = Date(
    epochDay = this.toEpochDay(), dayOfMonth = this.dayOfMonth, dayOfWeek = this.dayOfWeek.value
)

private fun YearMonth.toMonth() = Month(
    name = "${this.month.getDisplayName(TextStyle.FULL, Locale.getDefault())}, $year",
    firstDay = this.atDay(1).toDate(),
    lastDay = this.atEndOfMonth().toDate()
)