package com.example.android.strikingarts.domain.model

data class TrainingWeekDay(
    val epochDay: Long = 0,
    val weekDayDisplayName: String = "",
    val dateDisplayName: String = "",
    val workoutResults: ImmutableList<WorkoutResult> = ImmutableList()
) {
    val isTrainingDay = workoutResults.isNotEmpty()
    val userQuitMidWorkout = workoutResults.any { it.isWorkoutAborted }
}