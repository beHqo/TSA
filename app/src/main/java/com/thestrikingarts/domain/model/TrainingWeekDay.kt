package com.thestrikingarts.domain.model

data class TrainingWeekDay(
    val epochDay: Long = 0,
    val weekDayDisplayName: String = "",
    val dateDisplayName: String = "",
    val workoutResults: List<WorkoutResult> = emptyList()
) {
    val isTrainingDay = workoutResults.isNotEmpty()
    val userQuitMidWorkout = workoutResults.any { it.conclusion.isWorkoutFailed() }
}