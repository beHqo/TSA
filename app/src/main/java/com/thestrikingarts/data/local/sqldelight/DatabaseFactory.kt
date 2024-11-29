package com.thestrikingarts.data.local.sqldelight

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.thestrikingarts.LocalDatabase
import com.thestrikingarts.domain.model.WorkoutConclusion
import tables.TechniqueTable
import tables.WorkoutResultTable
import tables.WorkoutTable
import javax.inject.Inject

class DatabaseFactory @Inject constructor(private val sqlDriver: SqlDriver) {
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
        driver = sqlDriver,
        TechniqueTableAdapter = TechniqueTable.Adapter(
            movementTypeAdapter = EnumColumnAdapter(), techniqueTypeAdapter = EnumColumnAdapter()
        ), WorkoutTableAdapter = WorkoutTable.Adapter(
            roundsAdapter = IntColumnAdapter,
            roundLengthSecondsAdapter = IntColumnAdapter,
            restLengthSecondsAdapter = IntColumnAdapter,
            subRoundsAdapter = IntColumnAdapter
        ), WorkoutResultTableAdapter = WorkoutResultTable.Adapter(
            workoutConclusionAdapter = workoutConclusionAdapter
        )
    )
}