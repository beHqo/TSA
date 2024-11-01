package com.example.android.strikingarts.di

import app.cash.sqldelight.db.SqlDriver
import com.example.android.strikingarts.LocalDatabase
import com.example.android.strikingarts.data.local.sqldelight.DatabaseFactory
import com.example.android.strikingarts.data.local.sqldelight.DriverFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideSqlDriver(driverFactory: DriverFactory): SqlDriver = driverFactory.createDriver()

    @Provides
    @Singleton
    fun provideDatabase(databaseFactory: DatabaseFactory): LocalDatabase =
        databaseFactory.createDatabase()
}