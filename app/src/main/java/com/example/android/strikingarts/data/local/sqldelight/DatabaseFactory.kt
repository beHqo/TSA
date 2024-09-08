package com.example.android.strikingarts.data.local.sqldelight

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.example.android.strikingarts.LocalDatabase
import com.example.android.strikingarts.domain.model.WorkoutConclusion
import tables.Workout_result_table
import tables.Workout_table
import javax.inject.Inject

class DatabaseFactory @Inject constructor(private val driverFactory: DriverFactory) {
    private val workoutConclusionAdapter = object : ColumnAdapter<WorkoutConclusion, Long> {
        override fun decode(databaseValue: Long): WorkoutConclusion = when (databaseValue) {
            1L -> WorkoutConclusion.Successful
            2L -> WorkoutConclusion.Aborted(false)
            else -> WorkoutConclusion.Aborted(true)
        }

        override fun encode(value: WorkoutConclusion): Long = when (value) {
            is WorkoutConclusion.Successful -> 1
            is WorkoutConclusion.Aborted -> if (value.redeemed) 3 else 2
        }
    }

    fun createDatabase(): LocalDatabase = LocalDatabase(
        driver = driverFactory.createDriver(),
        workout_tableAdapter = Workout_table.Adapter(
            roundsAdapter = IntColumnAdapter,
            round_length_secondsAdapter = IntColumnAdapter,
            rest_length_secondsAdapter = IntColumnAdapter,
            sub_roundsAdapter = IntColumnAdapter
        ),
        workout_result_tableAdapter = Workout_result_table.Adapter(
            workout_conclusionAdapter = workoutConclusionAdapter
        )
    )
}