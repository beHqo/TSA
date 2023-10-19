package com.example.android.strikingarts.domain.usecase.home

import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.TrainingWeekDay
import com.example.android.strikingarts.domain.model.toImmutableList
import com.example.android.strikingarts.domain.usecase.javatime.GetWeekDaysOfCurrentWeekUseCase
import com.example.android.strikingarts.domain.usecase.javatime.RetrieveTrainingDaysOfWeekUseCase
import javax.inject.Inject

class GetTrainingWeekDaysForCurrentWeek @Inject constructor(
    private val getWeekDaysOfCurrentWeekUseCase: GetWeekDaysOfCurrentWeekUseCase,
    private val retrieveTrainingDaysOfWeekUseCase: RetrieveTrainingDaysOfWeekUseCase
) {
    suspend operator fun invoke(): ImmutableList<TrainingWeekDay> {
        val weekDayList = getWeekDaysOfCurrentWeekUseCase()
        val trainingDaysOfWeek = retrieveTrainingDaysOfWeekUseCase()

        if (trainingDaysOfWeek.isEmpty()) return ImmutableList()

        return weekDayList.mapIndexed { index, weekDay ->

            val currentTrainingDay = trainingDaysOfWeek.getOrNull(index)

            TrainingWeekDay(
                epochDay = currentTrainingDay?.epochDay ?: 0L,
                weekDayDisplayName = weekDay.weekDayDisplayName,
                dateDisplayName = weekDay.dateDisplayName,
                workoutResults = currentTrainingDay?.workoutResults ?: ImmutableList()
            )
        }.toImmutableList()
    }
}