package com.github.tsa.domain.model

data class WorkoutResult(
    val id: Long = -1,
    val workoutId: Long = 0,
    val workoutName: String = "",
    val conclusion: WorkoutConclusion = WorkoutConclusion.Successful,
    val epochDay: Long = 0L
)
