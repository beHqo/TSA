package com.thestrikingarts.domain.home

import com.thestrikingarts.domain.model.TrainingWeekDay
import com.thestrikingarts.domainandroid.usecase.javatime.GetWeekDaysOfCurrentWeekUseCase
import com.thestrikingarts.domainandroid.usecase.javatime.RetrieveWorkoutResultsOfWeekUseCase
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