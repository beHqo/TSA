package com.example.android.strikingarts.domainandroid.usecase.javatime

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetDisplayNameForEpochDayUseCase @Inject constructor() {
    operator fun invoke(epochDay: Long): String =
        LocalDate.ofEpochDay(epochDay).format(DateTimeFormatter.ISO_LOCAL_DATE)
}