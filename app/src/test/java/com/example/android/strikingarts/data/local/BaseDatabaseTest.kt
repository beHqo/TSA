package com.example.android.strikingarts.data.local

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.android.strikingarts.LocalDatabase
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import org.junit.After
import tables.Workout_table

private fun inMemorySqlDriver(): SqlDriver =
    JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply { LocalDatabase.Schema.create(this) }

abstract class BaseDatabaseTest {
    protected val testDispatcher = StandardTestDispatcher()
    protected val testScope = TestScope(testDispatcher)

    private val driver: SqlDriver = inMemorySqlDriver()
    protected val database: LocalDatabase = LocalDatabase(
        driver = driver, workout_tableAdapter = Workout_table.Adapter(
            roundsAdapter = IntColumnAdapter,
            round_length_secondsAdapter = IntColumnAdapter,
            rest_length_secondsAdapter = IntColumnAdapter,
            sub_roundsAdapter = IntColumnAdapter
        )
    )

    @After
    fun closeDb() {
        driver.close()
    }
}