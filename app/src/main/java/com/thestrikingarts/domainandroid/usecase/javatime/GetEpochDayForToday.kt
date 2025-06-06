package com.thestrikingarts.domainandroid.usecase.javatime

import java.time.LocalDate
import javax.inject.Inject

class GetEpochDayForToday @Inject constructor() {
    operator fun invoke(): Long = LocalDate.now().toEpochDay()
}