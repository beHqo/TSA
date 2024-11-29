package com.thestrikingarts.domainandroid.calendar

import android.view.View
import androidx.core.text.layoutDirection
import com.thestrikingarts.domain.model.Date
import com.thestrikingarts.domain.model.Month
import com.thestrikingarts.ui.util.localized
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
            val locale = Locale.getDefault()
            val textStyle = if (locale.layoutDirection == View.LAYOUT_DIRECTION_LTR) TextStyle.SHORT
            else TextStyle.NARROW

            val weekDays = mutableListOf<String>()

            val week = WeekFields.of(locale)
            val firstDayOfWeek = week.firstDayOfWeek

            for (i in 0..6L) weekDays += firstDayOfWeek.plus(i).getDisplayName(textStyle, locale)

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
    name = "${this.month.getDisplayName(TextStyle.FULL, Locale.getDefault())}, ${year.localized()}",
    firstDay = this.atDay(1).toDate(),
    lastDay = this.atEndOfMonth().toDate()
)