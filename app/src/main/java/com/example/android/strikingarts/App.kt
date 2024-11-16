package com.example.android.strikingarts

import android.app.Application
import android.util.Log
import com.example.android.strikingarts.data.local.sqldelight.DriverFactory
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.FileOutputStream
import java.io.IOException

private const val TAG = "App"
private const val DB_ASSET_PATH = "database/pre_populated.db"

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        prePopulateDatabase()
    }

    private fun prePopulateDatabase() = runBlocking(Dispatchers.IO) {
        val database = getDatabasePath(DriverFactory.DATABASE_NAME)

        if (!database.exists()) {
            Log.d(TAG, "INIT: copying pre-populated db asset into sqlite location")

            try {
                val inputStream = assets.open(DB_ASSET_PATH)
                val outputStream = FileOutputStream(database.absolutePath)

                inputStream.use { input ->
                    outputStream.use { output: FileOutputStream -> input.copyTo(output) }
                }
            } catch (e: IOException) {
                Log.e(TAG, "onCreate: Failed to copy the pre-populated db asset", e)

                throw e
            }
        }
    }
}