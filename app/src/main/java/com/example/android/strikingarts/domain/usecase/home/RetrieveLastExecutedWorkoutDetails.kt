package com.example.android.strikingarts.domain.usecase.home

import com.example.android.strikingarts.domain.interfaces.TrainingDateCacheRepository
import com.example.android.strikingarts.domain.interfaces.WorkoutCacheRepository
import com.example.android.strikingarts.domain.usecase.javatime.GetDisplayNameForEpochDay
import com.example.android.strikingarts.domain.usecase.javatime.RetrieveElapsedDaysBetweenTwoEpochDays
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class RetrieveLastExecutedWorkoutDetails @Inject constructor(
    private val trainingDateRepository: TrainingDateCacheRepository,
    private val workoutRepository: WorkoutCacheRepository,
    private val retrieveElapsedDaysBetweenTwoEpochDays: RetrieveElapsedDaysBetweenTwoEpochDays,
    private val getDisplayNameForEpochDay: GetDisplayNameForEpochDay
) {
    private val mutex = Mutex()

    private var workoutId: Long? = null
    private lateinit var workoutName: String
    private var elapsedDaysSinceLastExecutedWorkout: Long? = null
    private lateinit var displayNameForDate: String

    private suspend fun retrieveInformation() {
        coroutineScope {
            val job = launch {
                val lastTrainingDate = trainingDateRepository.retrieveLastTrainingDate()
                val epochDay = lastTrainingDate.first

                val workoutId = lastTrainingDate.second.last()
                val workoutName =
                    workoutRepository.getWorkoutNames(listOf(workoutId)).joinToString()
                val displayNameDate = retrieveElapsedDaysBetweenTwoEpochDays(epochDay)

                this@RetrieveLastExecutedWorkoutDetails.workoutId = workoutId
                this@RetrieveLastExecutedWorkoutDetails.workoutName = workoutName
                this@RetrieveLastExecutedWorkoutDetails.elapsedDaysSinceLastExecutedWorkout =
                    displayNameDate
                this@RetrieveLastExecutedWorkoutDetails.displayNameForDate =
                    getDisplayNameForEpochDay(epochDay)
            }

            job.join()
        }
    }

    suspend fun getWorkoutName(): String {
        if (!this::workoutName.isInitialized) mutex.withLock {
            retrieveInformation()

            return workoutName
        } else return workoutName
    }

    suspend fun getElapsedDaysSinceLastExecutedWorkout(): Long {
        if (elapsedDaysSinceLastExecutedWorkout == null) mutex.withLock {
            retrieveInformation()

            return elapsedDaysSinceLastExecutedWorkout ?: 0
        } else return elapsedDaysSinceLastExecutedWorkout ?: 0L
    }

    suspend fun getWorkoutId(): Long {
        if (workoutId == null) mutex.withLock {
            retrieveInformation()

            return workoutId ?: 0
        } else return workoutId ?: 0
    }

    suspend fun getDisplayNameForDate(): String {
        if (!this::displayNameForDate.isInitialized) mutex.withLock {
            retrieveInformation()

            return displayNameForDate
        } else return displayNameForDate
    }
}