package com.example.android.strikingarts.domain.usecase.javatime

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class RetrieveElapsedDaysBetweenTwoEpochDays @Inject constructor() {
    operator fun invoke(epochDay: Long): Long {
        val currentLocalDate = LocalDate.now()
        val givenLocalDate = LocalDate.ofEpochDay(epochDay)

        return ChronoUnit.DAYS.between(givenLocalDate, currentLocalDate)
    }
}