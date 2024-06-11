package com.example.android.strikingarts.data.local.sqldelight

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.android.strikingarts.LocalDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val DATABASE_NAME = "local_database.db"

class DriverFactory @Inject constructor(@ApplicationContext private val context: Context) {
    fun createDriver(): SqlDriver = AndroidSqliteDriver(schema = LocalDatabase.Schema,
        context = context,
        name = DATABASE_NAME,
        callback = object : AndroidSqliteDriver.Callback(LocalDatabase.Schema) {
            override fun onConfigure(db: SupportSQLiteDatabase) {
                super.onConfigure(db)
                db.enableWriteAheadLogging()
                db.setForeignKeyConstraintsEnabled(true)
            }
        })
}