package com.example.android.strikingarts.domain.javatime

import com.example.android.strikingarts.domainandroid.usecase.javatime.RetrieveEpochDayForFirstAndLastDayOfMonth
import io.kotest.matchers.shouldBe
import org.junit.Test
import java.time.YearMonth

class RetrieveEpochDayForFirstAndLastDayOfMonthTest {
    private val useCase = RetrieveEpochDayForFirstAndLastDayOfMonth()

    @Test
    fun `Given a beforeOrAfter value of 0, Then confirm the correctness of returned value`() {
        val retrieved = useCase(0)

        val month = YearMonth.now()
        val leftBound = month.atDay(1).toEpochDay()
        val rightBound = month.atEndOfMonth().toEpochDay()

        retrieved shouldBe (leftBound to rightBound)
    }

    @Test
    fun `Given a beforeOrAfter a positive value, Then confirm the correctness of returned value`() {
        val beforeOrAfter: Long = 7
        val retrieved = useCase(beforeOrAfter)

        val month = YearMonth.now().plusMonths(beforeOrAfter)
        val leftBound = month.atDay(1).toEpochDay()
        val rightBound = month.atEndOfMonth().toEpochDay()

        retrieved shouldBe (leftBound to rightBound)
    }

    @Test
    fun `Given a beforeOrAfter a negative value, Then confirm the correctness of returned value`() {
        val beforeOrAfter: Long = -7
        val retrieved = useCase(beforeOrAfter)

        val month = YearMonth.now().minusMonths(7)
        val leftBound = month.atDay(1).toEpochDay()
        val rightBound = month.atEndOfMonth().toEpochDay()

        retrieved shouldBe (leftBound to rightBound)
    }
}