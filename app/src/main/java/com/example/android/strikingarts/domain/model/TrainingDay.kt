package com.example.android.strikingarts.domain.model

data class TrainingDay(
    val epochDay: Long = 0, val workoutResults: List<WorkoutResult> = emptyList()
)