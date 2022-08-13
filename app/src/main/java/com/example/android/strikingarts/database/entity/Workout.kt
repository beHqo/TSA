package com.example.android.strikingarts.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_table")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val workoutId: Long = 0L,

    @ColumnInfo(name = "workout_name")
    val name: String = "$workoutId Workout",

    @ColumnInfo(name = "number_of_rounds")
    val numberOfRounds: Int = 0, // May change to 3 or 5

    @ColumnInfo(name = "rounds_duration_milli")
    val roundsDurationMilli: Long = 0L, // may change to 5 minutes

    @ColumnInfo(name = "number_of_breakpoints")
    val numberOfBreakpoints: Int = 0, // For now, we can only put breakpoints in the middle

    @ColumnInfo(name = "number_of_rests")
    val numberOfRests: Int = numberOfRounds - 1,

    @ColumnInfo(name = "rests_duration_milli")
    val restsDurationMilli: Long = 0L, // May change to 1 minute
)