package com.example.android.strikingarts.data.local.sqldelight

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.example.android.strikingarts.LocalDatabase
import tables.Workout_table
import javax.inject.Inject

class DatabaseFactory @Inject constructor(private val driverFactory: DriverFactory) {
    fun createDatabase(): LocalDatabase = LocalDatabase(
        driver = driverFactory.createDriver(), workout_tableAdapter = Workout_table.Adapter(
            roundsAdapter = IntColumnAdapter,
            round_length_secondsAdapter = IntColumnAdapter,
            rest_length_secondsAdapter = IntColumnAdapter,
            sub_roundsAdapter = IntColumnAdapter
        )
    )
}