package com.github.tsa.domainandroid.usecase.javatime

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class GetElapsedDaysSinceDateUseCase @Inject constructor() {
    operator fun invoke(epochDay: Long): Long {
        val todayLocalDate = LocalDate.now()
        val givenLocalDate = LocalDate.ofEpochDay(epochDay)

        return ChronoUnit.DAYS.between(givenLocalDate, todayLocalDate)
    }
}