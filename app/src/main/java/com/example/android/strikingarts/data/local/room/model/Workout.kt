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
    val rounds: Int = 1,

    @ColumnInfo(name = "rounds_length_seconds")
    val roundLengthSeconds: Int = 180, // may change to 5 minutes

    @ColumnInfo(name = "rests_length_seconds")
    val restsLengthSeconds: Int = 60, // May change to 1 minute

    @ColumnInfo(name = "sub_rounds")
    val subRounds: Int = 0
)