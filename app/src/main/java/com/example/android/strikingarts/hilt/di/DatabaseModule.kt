package com.example.android.strikingarts.hilt.di

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.android.strikingarts.data.local.room.StrikingDatabase
import com.example.android.strikingarts.data.local.room.dao.ComboDao
import com.example.android.strikingarts.data.local.room.dao.TechniqueDao
import com.example.android.strikingarts.data.local.room.dao.WorkoutDao
import com.example.android.strikingarts.data.local.room.model.Combo
import com.example.android.strikingarts.data.local.room.model.ComboTechniqueCrossRef
import com.example.android.strikingarts.data.local.room.model.Technique
import com.example.android.strikingarts.data.local.room.model.Workout
import com.example.android.strikingarts.data.local.room.model.WorkoutComboCrossRef
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
    fun providesTechniqueDao(database: StrikingDatabase): TechniqueDao = database.techniqueDao()

    @Provides
    fun providesComboDao(database: StrikingDatabase): ComboDao = database.comboDao()

    @Provides
    fun providesWorkoutDao(database: StrikingDatabase): WorkoutDao = database.workoutDao()

    @Provides
    @Singleton
    fun providesStrikingDatabase(
        @ApplicationContext appContext: Context,
        techniqueDaoProvider: Provider<TechniqueDao>,
        comboDaoProvider: Provider<ComboDao>,
        workoutDaoProvider: Provider<WorkoutDao>
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
                        val workoutIds = populateWorkoutTable(workoutDaoProvider)
                        insertComboWithTechniques(comboDaoProvider = comboDaoProvider, techniqueIds = techniqueIds, comboIds = comboIds)
                        insertWorkoutWithCombo(workoutDaoProvider = workoutDaoProvider, comboIdList = comboIds, workoutIdList = workoutIds)

                        val spearElbow = Technique(name = "Spear Elbow", num = "66", canBeFaint = true, canBeBodyshot = false, techniqueType = "Elbow", movementType = "Offense")
                        val spinningElbow = Technique(name = "Spinning Elbow", num = "77", canBeFaint = true, canBeBodyshot = false, techniqueType = "Elbow", movementType = "Offense")
                        val combo = Combo(name = "Elbow Combo", description =  "Description", delay = 10)

                        val techniqueDao = techniqueDaoProvider.get()!!
                        val comboDao = comboDaoProvider.get()!!

                        val spearElbowId = techniqueDao.insert(spearElbow)
                        val spinningElbowId = techniqueDao.insert(spinningElbow)

                        val comboId = comboDao.insert(combo)
                        comboDao.insertComboTechniqueCrossRef(ComboTechniqueCrossRef(comboId = comboId, techniqueId = spearElbowId))
                        comboDao.insertComboTechniqueCrossRef(ComboTechniqueCrossRef(comboId = comboId, techniqueId = spearElbowId))
                        comboDao.insertComboTechniqueCrossRef(ComboTechniqueCrossRef(comboId = comboId, techniqueId = spinningElbowId))
                        comboDao.insertComboTechniqueCrossRef(ComboTechniqueCrossRef(comboId = comboId, techniqueId = spearElbowId))
                    }
                }
            })
//            .createFromAsset("database/striking_database")
            .build()
}

private suspend fun populateTechniqueTable(techniqueDaoProvider: Provider<TechniqueDao>) : List<Long> {
    val techniqueDao = techniqueDaoProvider.get()

    val jab = Technique(name = "Jab", num = "1", canBeFaint = true, canBeBodyshot = true, techniqueType = "Punch", movementType = "Defense", color = Color.Red.value.toString())
    val cross = Technique(name = "Cross", num = "2", canBeFaint = true, canBeBodyshot = true, techniqueType = "Punch", movementType = "Offense")
    val leadHook = Technique(name = "Lead Hook", num = "3", canBeFaint = true, canBeBodyshot = true, techniqueType = "Punch", movementType = "Offense")
    val rearHook = Technique(name = "Rear Hook", num = "4", canBeFaint = true, canBeBodyshot = true, techniqueType = "Punch", movementType = "Offense")
    val leadUppercut = Technique(name = "Lead Uppercut", num = "5", canBeFaint = true, canBeBodyshot = false, techniqueType = "Punch", movementType = "Offense")
    val rearUppercut = Technique(name = "Rear Uppercut", num = "6", canBeFaint = true, canBeBodyshot = false, techniqueType = "Punch", movementType = "Offense")
    val backFist = Technique(name = "Back Fist", canBeFaint = true, canBeBodyshot = false, techniqueType = "Punch", movementType = "Offense")
    val spinningBackFist = Technique(name = "Spinning Back Fist", canBeFaint = true, canBeBodyshot = false, techniqueType = "Punch", movementType = "Offense")
    val supermanPunch = Technique(name = "Superman Punch", canBeFaint = true, canBeBodyshot = false, techniqueType = "Punch", movementType = "Offense")

    val rearUppercut2 = Technique(name = "Rear Uppercut2", num = "6", canBeFaint = true, canBeBodyshot = false, techniqueType = "Punch", movementType = "Offense")
    val rearUppercut3 = Technique(name = "Rear Uppercut3", num = "6", canBeFaint = true, canBeBodyshot = false, techniqueType = "Punch", movementType = "Offense")
    val rearUppercut4 = Technique(name = "Rear Uppercut4", num = "6", canBeFaint = true, canBeBodyshot = false, techniqueType = "Punch", movementType = "Offense")
    val rearUppercut5 = Technique(name = "Rear Uppercut5", num = "6", canBeFaint = true, canBeBodyshot = false, techniqueType = "Punch", movementType = "Offense")
    val rearUppercut6 = Technique(name = "Rear Uppercut6", num = "6", canBeFaint = true, canBeBodyshot = false, techniqueType = "Punch", movementType = "Offense")

    val spearElbow = Technique(name = "Spear Elbow", num = "66", canBeFaint = true, canBeBodyshot = false, techniqueType = "Elbow", movementType = "Offense")
    val spinningElbow = Technique(name = "Spinning Elbow", num = "77", canBeFaint = true, canBeBodyshot = false, techniqueType = "Elbow", movementType = "Offense")

    return techniqueDao.insertAll(jab, cross, leadHook, rearHook, leadUppercut, rearUppercut, backFist, spinningBackFist, supermanPunch, rearUppercut2, rearUppercut3, rearUppercut4, rearUppercut5, rearUppercut6)
}

private suspend fun populateComboTable(comboDaoProvider: Provider<ComboDao>) : List<Long> {
    val comboDao = comboDaoProvider.get()

    val oneTwo = Combo(name = "Bread and butter", description = "Simple 1, 2")
    val twoThreeTwo = Combo(name = "Repeat till Death", description = "Simple 2, 3, 2")
    val upperCutHook = Combo(name = "Educated lead-hand", description = "Lead hand hook then uppercut")

    return comboDao.insertAll(oneTwo, twoThreeTwo, upperCutHook)
}

private suspend fun populateWorkoutTable(workoutDaoProvider: Provider<WorkoutDao>) : List<Long> {
    val workoutDao = workoutDaoProvider.get()

    val firstWorkout = Workout(name = "First Workout", rounds = "5")
    val secondWorkout = Workout(
        name = "Second Workout",
        rounds = "3",
        roundLengthMilli = 60_000,
        restsLengthMilli = 60_000
    )
    val thirdWorkout = Workout(
        name = "Third Workout",
        rounds = "12",
        roundLengthMilli = 120_000,
        restsLengthMilli = 10_000
    )

    return workoutDao.insert(firstWorkout, secondWorkout, thirdWorkout)
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

private suspend fun insertWorkoutWithCombo(workoutDaoProvider: Provider<WorkoutDao>, comboIdList: List<Long>, workoutIdList: List<Long>) {
    val workoutDao = workoutDaoProvider.get()

    insertRefs(workoutDao, workoutIdList[0], comboIdList[0])
    insertRefs(workoutDao, workoutIdList[1], comboIdList[0], comboIdList[1])
    insertRefs(workoutDao, workoutIdList[2], comboIdList[0], comboIdList[0], comboIdList[0], comboIdList[1], comboIdList[2])
}

private suspend fun insertRefs(dao: WorkoutDao, workoutId: Long, vararg comboIdList: Long) {
    for (comboId in comboIdList) {
        dao.insertWorkoutComboCrossRef(WorkoutComboCrossRef(workoutId = workoutId, comboId = comboId))
    }
}