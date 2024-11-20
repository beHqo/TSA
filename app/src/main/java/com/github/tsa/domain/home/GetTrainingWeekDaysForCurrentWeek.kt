package com.github.tsa.domain.home

import com.github.tsa.domain.model.TrainingWeekDay
import com.github.tsa.domainandroid.usecase.javatime.GetWeekDaysOfCurrentWeekUseCase
import com.github.tsa.domainandroid.usecase.javatime.RetrieveWorkoutResultsOfWeekUseCase
import javax.inject.Inject

class GetTrainingWeekDaysForCurrentWeek @Inject constructor(
    private val getWeekDaysOfCurrentWeekUseCase: GetWeekDaysOfCurrentWeekUseCase,
    private val retrieveWorkoutResultsOfWeekUseCase: RetrieveWorkoutResultsOfWeekUseCase
) {
    suspend operator fun invoke(): List<TrainingWeekDay> {
        val weekDayList = getWeekDaysOfCurrentWeekUseCase()
        val workoutResultsOfWeek = retrieveWorkoutResultsOfWeekUseCase()

        return weekDayList.map { weekDay ->
            val currentWorkoutResult =
                workoutResultsOfWeek.filter { it.epochDay == weekDay.epochDay }

            TrainingWeekDay(
                epochDay = weekDay.epochDay,
                weekDayDisplayName = weekDay.weekDayDisplayName,
                dateDisplayName = weekDay.dateDisplayName,
                workoutResults = currentWorkoutResult
            )
        }
    }
}