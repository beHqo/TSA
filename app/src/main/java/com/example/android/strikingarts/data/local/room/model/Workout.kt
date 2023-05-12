package com.example.android.strikingarts.data.local.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_table")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val workoutId: Long = 0L,

    @ColumnInfo(name = "workout_name")
    val name: String = "",

    @ColumnInfo(name = "rounds")
    val rounds: String = "",

    @ColumnInfo(name = "rounds_length_milli")
    val roundLengthMilli: Long = 300_000L, // may change to 5 minutes

    @ColumnInfo(name = "rests_length_milli")
    val restsLengthMilli: Long = 60_000L, // May change to 1 minute

    @ColumnInfo(name = "notification_intervals")
    val notificationIntervals: String = "0"
)