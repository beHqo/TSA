package com.example.android.strikingarts.domain.usecase.home

import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.WeekDay
import com.example.android.strikingarts.domain.model.toImmutableList
import com.example.android.strikingarts.domain.usecase.javatime.RetrieveTrainingDaysOfWeekUseCase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

class GetWeekDaysForCurrentWeek
@Inject constructor(private val retrieveTrainingDaysOfWeekUseCase: RetrieveTrainingDaysOfWeekUseCase) {
    suspend operator fun invoke(): ImmutableList<WeekDay> {
        val locale = Locale.getDefault()

        val trainingDaysOfWeek = retrieveTrainingDaysOfWeekUseCase()

        val currentWeek = WeekFields.of(locale)
        val firstDayOfWeek = currentWeek.firstDayOfWeek
        val firstDayOfWeekLocalDate =
            LocalDate.now().with(TemporalAdjusters.previousOrSame(firstDayOfWeek))

        val weekList = mutableListOf<WeekDay>()

        for (i in 0..6L) {
            val indexedDay = firstDayOfWeekLocalDate.plusDays(i)

            weekList += WeekDay(
                weekDayDisplayName = indexedDay.dayOfWeek.getDisplayName(TextStyle.SHORT, locale),
                dateDisplayName = indexedDay.format(DateTimeFormatter.ofPattern("dd")),
                isTrainingDay = trainingDaysOfWeek.contains(indexedDay.toEpochDay())
            )
        }

        return weekList.toImmutableList()
    }
}