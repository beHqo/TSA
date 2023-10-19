package com.example.android.strikingarts.domain.model

data class WorkoutResult(
    val workoutId: Long = 0,
    val workoutName: String = "",
    val isWorkoutAborted: Boolean = false,
    val epochDay: Long = 0L
)
