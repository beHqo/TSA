package com.example.android.strikingarts.domain.home

import com.example.android.strikingarts.domain.model.TrainingWeekDay
import com.example.android.strikingarts.domainandroid.usecase.javatime.GetWeekDaysOfCurrentWeekUseCase
import com.example.android.strikingarts.domainandroid.usecase.javatime.RetrieveWorkoutResultsOfWeekUseCase
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