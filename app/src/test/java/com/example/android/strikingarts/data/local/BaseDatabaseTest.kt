package com.example.android.strikingarts.data.local

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.android.strikingarts.LocalDatabase
import com.example.android.strikingarts.domain.model.WorkoutConclusion
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import org.junit.After
import tables.Workout_result_table
import tables.Workout_table

private fun inMemorySqlDriver(): SqlDriver =
    JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply { LocalDatabase.Schema.create(this) }

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

abstract class BaseDatabaseTest {
    private val scheduler = TestCoroutineScheduler()
    protected val ioDispatcherTest = StandardTestDispatcher(scheduler, "ioDispatcher")
    protected val defaultDispatcherTest = StandardTestDispatcher(scheduler, "defaultDispatcher")
    protected val testScope = TestScope(ioDispatcherTest)

    private val driver: SqlDriver = inMemorySqlDriver()
    protected val database: LocalDatabase = LocalDatabase(
        driver = driver, workout_tableAdapter = Workout_table.Adapter(
            roundsAdapter = IntColumnAdapter,
            round_length_secondsAdapter = IntColumnAdapter,
            rest_length_secondsAdapter = IntColumnAdapter,
            sub_roundsAdapter = IntColumnAdapter
        ), workout_result_tableAdapter = Workout_result_table.Adapter(
            workout_conclusionAdapter = workoutConclusionAdapter
        )
    )

    @After
    fun closeDb() {
        driver.close()
    }
}