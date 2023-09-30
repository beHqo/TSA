package com.example.android.strikingarts.domain.usecase.javatime

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetDisplayNameForEpochDay @Inject constructor() {
    operator fun invoke(epochDay: Long): String =
        LocalDate.ofEpochDay(epochDay).format(DateTimeFormatter.ISO_LOCAL_DATE)
}