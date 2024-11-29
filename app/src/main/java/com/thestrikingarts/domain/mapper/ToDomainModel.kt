package com.thestrikingarts.domain.mapper

import com.thestrikingarts.domain.model.Combo
import com.thestrikingarts.domain.model.Workout
import com.thestrikingarts.domain.model.WorkoutDetails

fun Workout.toWorkoutDetails() = WorkoutDetails(
    rounds = this.rounds,
    roundLengthSeconds = this.roundLengthSeconds,
    restLengthSeconds = this.restLengthSeconds
)

fun Combo.getAudioStringList(): List<String> =
    this.techniqueList.map { it.audioAttributes.audioString }

fun Combo.getAudioDuration(): Long =
    this.techniqueList.sumOf { it.audioAttributes.durationMillis }