package com.example.android.strikingarts.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.android.strikingarts.LocalDatabase
import com.example.android.strikingarts.data.local.sqldelight.DatabaseFactory
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import org.junit.After

private fun inMemorySqlDriver(): SqlDriver =
    JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply { LocalDatabase.Schema.create(this) }

abstract class BaseDatabaseTest {
    private val scheduler = TestCoroutineScheduler()
    protected val ioDispatcherTest = StandardTestDispatcher(scheduler, "ioDispatcher")
    protected val defaultDispatcherTest = StandardTestDispatcher(scheduler, "defaultDispatcher")
    protected val testScope = TestScope(ioDispatcherTest)

    private val sqlDriver: SqlDriver = inMemorySqlDriver()
    protected val database = DatabaseFactory(sqlDriver).createDatabase()

    @After
    fun closeDb() {
        sqlDriver.close()
    }
}