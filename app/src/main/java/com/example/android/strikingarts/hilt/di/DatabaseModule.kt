package com.example.android.strikingarts.hilt.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.android.strikingarts.database.StrikingDatabase
import com.example.android.strikingarts.database.dao.ComboDao
import com.example.android.strikingarts.database.dao.TechniqueDao
import com.example.android.strikingarts.database.dao.WorkoutDao
import com.example.android.strikingarts.database.entity.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideTechniqueDao(database: StrikingDatabase): TechniqueDao = database.techniqueDao()

    @Provides
    fun provideComboDao(database: StrikingDatabase): ComboDao = database.comboDao()

    @Provides
    fun provideWorkoutDao(database: StrikingDatabase): WorkoutDao = database.workoutDao()

    @Provides
    @Singleton
    fun provideStrikingDatabase(
        @ApplicationContext appContext: Context,
        techniqueDaoProvider: Provider<TechniqueDao>,
        comboDaoProvider: Provider<ComboDao>,
    ): StrikingDatabase =
        Room.databaseBuilder(
            appContext,
            StrikingDatabase::class.java,
            "striking_database"
        )
            .fallbackToDestructiveMigration()
            .addCallback(object: RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    val scope = CoroutineScope(SupervisorJob())
                    scope.launch(Dispatchers.IO) {
                        val techniqueIds = populateTechniqueTable(techniqueDaoProvider)
                        val comboIds = populateComboTable(comboDaoProvider)
                        insertComboWithTechniques(comboDaoProvider, techniqueIds, comboIds)
                    }
                }
            })
//            .createFromAsset("database/striking_database")
            .build()
}

private suspend fun populateTechniqueTable(techniqueDaoProvider: Provider<TechniqueDao>) : List<Long> {
    val techniqueDao = techniqueDaoProvider.get()

    val jab = Technique(name = "Jab", num = "1", canBeFaint = true, canBeBodyshot = true, techniqueType = "Punch", movementType = "Offense")
    val cross = Technique(name = "Cross", num = "2", canBeFaint = true, canBeBodyshot = true, techniqueType = "Punch", movementType = "Offense")
    val leadHook = Technique(name = "Lead Hook", num = "3", canBeFaint = true, canBeBodyshot = true, techniqueType = "Punch", movementType = "Offense")
    val rearHook = Technique(name = "Rear Hook", num = "4", canBeFaint = true, canBeBodyshot = true, techniqueType = "Punch", movementType = "Offense")
    val leadUppercut = Technique(name = "Lead Uppercut", num = "5", canBeFaint = true, canBeBodyshot = false, techniqueType = "Punch", movementType = "Offense")
    val rearUppercut = Technique(name = "Rear Uppercut", num = "6", canBeFaint = true, canBeBodyshot = false, techniqueType = "Punch", movementType = "Offense")

    return techniqueDao.insertTechniques(jab, cross, leadHook, rearHook, leadUppercut, rearUppercut)
}

private suspend fun populateComboTable(comboDaoProvider: Provider<ComboDao>) : List<Long> {
    val comboDao = comboDaoProvider.get()

    val oneTwo = Combo(name = "Bread and butter", description = "Simple 1, 2")
    val twoThreeTwo = Combo(name = "Repeat till Death", description = "Simple 2, 3, 2")
    val upperCutHook = Combo(name = "Educated lead-hand", description = "Lead hand hook then uppercut")

    return comboDao.insertCombos(oneTwo, twoThreeTwo, upperCutHook)
}

private suspend fun insertComboWithTechniques(comboDaoProvider: Provider<ComboDao>, techniqueIds: List<Long>, comboIds: List<Long>) {
    val comboDao = comboDaoProvider.get()

    insertRefs(comboDao, comboIds[0], techniqueIds[0], techniqueIds[1])
    insertRefs(comboDao, comboIds[1], techniqueIds[1], techniqueIds[2], techniqueIds[1])
    insertRefs(comboDao, comboIds[2], techniqueIds[2], techniqueIds[4])
}

private suspend fun insertRefs(dao: ComboDao, comboId: Long, vararg techniqueIds: Long) {
    for (techniqueId in techniqueIds) {
        dao.insertComboTechniqueCrossRef(ComboTechniqueCrossRef(comboId = comboId, techniqueId = techniqueId))
    }
}