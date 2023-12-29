package com.example.android.strikingarts.hilt.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.android.strikingarts.data.local.room.StrikingDatabase
import com.example.android.strikingarts.data.local.room.dao.ComboDao
import com.example.android.strikingarts.data.local.room.dao.TechniqueDao
import com.example.android.strikingarts.data.local.room.dao.WorkoutConclusionDao
import com.example.android.strikingarts.data.local.room.dao.WorkoutDao
import com.example.android.strikingarts.data.local.room.model.Combo
import com.example.android.strikingarts.data.local.room.model.ComboTechniqueCrossRef
import com.example.android.strikingarts.data.local.room.model.Technique
import com.example.android.strikingarts.data.local.room.model.Workout
import com.example.android.strikingarts.data.local.room.model.WorkoutComboCrossRef
import com.example.android.strikingarts.data.local.room.model.WorkoutConclusion
import com.example.android.strikingarts.domain.model.MovementType
import com.example.android.strikingarts.domain.model.TechniqueType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
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
    fun providesWorkoutConclusionDao(database: StrikingDatabase): WorkoutConclusionDao =
        database.workoutConclusionDao()

    @Provides
    @Singleton
    fun providesStrikingDatabase(
        @ApplicationContext appContext: Context,
        techniqueDaoProvider: Provider<TechniqueDao>,
        comboDaoProvider: Provider<ComboDao>,
        workoutDaoProvider: Provider<WorkoutDao>,
        workoutConclusionDaoProvider: Provider<WorkoutConclusionDao>
    ): StrikingDatabase = Room.databaseBuilder(
        appContext, StrikingDatabase::class.java, "striking_database"
    ).fallbackToDestructiveMigration().addCallback(object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val scope = CoroutineScope(SupervisorJob())
            scope.launch(Dispatchers.IO) {
                val techniqueIds = populateTechniqueTable(techniqueDaoProvider)
                val comboIds = populateComboTable(comboDaoProvider)
                val workoutIds = populateWorkoutTable(workoutDaoProvider)

                populateTrainingDateTable(workoutConclusionDaoProvider)

                insertComboWithTechniques(
                    comboDaoProvider = comboDaoProvider,
                    techniqueIds = techniqueIds,
                    comboIds = comboIds
                )
                insertWorkoutWithCombo(
                    workoutDaoProvider = workoutDaoProvider,
                    comboIdList = comboIds,
                    workoutIdList = workoutIds
                )
            }
        }
    })
//            .createFromAsset("database/striking_database")
        .build()
}

private suspend fun populateTechniqueTable(techniqueDaoProvider: Provider<TechniqueDao>): List<Long> {
    val techniqueDao = techniqueDaoProvider.get()

    /*Punch*/
    val jab = Technique(
        name = "Jab", num = "1",


        techniqueType = TechniqueType.PUNCH.name, movementType = MovementType.OFFENSE.name
    ) //1
    val cross = Technique(
        name = "Cross", num = "2",


        techniqueType = TechniqueType.PUNCH.name, movementType = MovementType.OFFENSE.name
    ) //2
    val leadHook = Technique(
        name = "Lead Hook", num = "3",


        techniqueType = TechniqueType.PUNCH.name, movementType = MovementType.OFFENSE.name
    ) //3
    val rearHook = Technique(
        name = "Rear Hook", num = "4",


        techniqueType = TechniqueType.PUNCH.name, movementType = MovementType.OFFENSE.name
    ) //4
    val leadUppercut = Technique(
        name = "Lead Uppercut", num = "5",


        techniqueType = TechniqueType.PUNCH.name, movementType = MovementType.OFFENSE.name
    ) //5
    val rearUppercut = Technique(
        name = "Rear Uppercut", num = "6",


        techniqueType = TechniqueType.PUNCH.name, movementType = MovementType.OFFENSE.name
    ) //6
    val checkHook = Technique(
        name = "Check Hook",


        techniqueType = TechniqueType.PUNCH.name, movementType = MovementType.OFFENSE.name
    ) //7
    val backFist = Technique(
        name = "Back Fist",


        techniqueType = TechniqueType.PUNCH.name, movementType = MovementType.OFFENSE.name
    ) //8
    val spinningBackFist = Technique(
        name = "Spinning Back Fist",


        techniqueType = TechniqueType.PUNCH.name, movementType = MovementType.OFFENSE.name
    ) //9
    val supermanPunch = Technique(
        name = "Superman Punch",


        techniqueType = TechniqueType.PUNCH.name, movementType = MovementType.OFFENSE.name
    ) //10
    val overhand = Technique(
        name = "Overhand",


        techniqueType = TechniqueType.PUNCH.name, movementType = MovementType.OFFENSE.name
    ) //11

    /*Elbow*/
    val leadSokTak = Technique(
        name = "Sok Tat (Lead)",


        techniqueType = TechniqueType.ELBOW.name, movementType = MovementType.OFFENSE.name
    ) //12
    val rearSokTak = Technique(
        name = "Sok Tat (Rear)",


        techniqueType = TechniqueType.ELBOW.name, movementType = MovementType.OFFENSE.name
    ) //13
    val leadSokNat = Technique(
        name = "Sok Nat (Lead)",


        techniqueType = TechniqueType.ELBOW.name, movementType = MovementType.OFFENSE.name
    ) //14
    val rearSokNat = Technique(
        name = "Sok Nat (Rear)",


        techniqueType = TechniqueType.ELBOW.name, movementType = MovementType.OFFENSE.name
    ) //15
    val leadSokTi = Technique(
        name = "Sok Ti (Lead)",


        techniqueType = TechniqueType.ELBOW.name, movementType = MovementType.OFFENSE.name
    ) //16
    val rearSokTi = Technique(
        name = "Sok Ti (Rear)",


        techniqueType = TechniqueType.ELBOW.name, movementType = MovementType.OFFENSE.name
    ) //17
    val leadSokPhung = Technique(
        name = "Sok Phung (Lead)",


        techniqueType = TechniqueType.ELBOW.name, movementType = MovementType.OFFENSE.name
    ) //18
    val rearSokPhung = Technique(
        name = "Sok Phung (Rear)",


        techniqueType = TechniqueType.ELBOW.name, movementType = MovementType.OFFENSE.name
    ) //19
    val sokKlap = Technique(
        name = "Sok Klap",


        techniqueType = TechniqueType.ELBOW.name, movementType = MovementType.OFFENSE.name
    ) //20

    /*Kick*/
    val leadTeep = Technique(
        name = "Lead Teep",


        techniqueType = TechniqueType.KICK.name, movementType = MovementType.OFFENSE.name
    ) //21
    val rearTeep = Technique(
        name = "Rear Teep",


        techniqueType = TechniqueType.KICK.name, movementType = MovementType.OFFENSE.name
    ) //22
    val leadRoundhouse = Technique(
        name = "Lead Roundhouse",


        techniqueType = TechniqueType.KICK.name, movementType = MovementType.OFFENSE.name
    ) //23
    val rearRoundhouse = Technique(
        name = "Rear Roundhouse",


        techniqueType = TechniqueType.KICK.name, movementType = MovementType.OFFENSE.name
    ) //24
    val leadSideKick = Technique(
        name = "Lead Side Kick",


        techniqueType = TechniqueType.KICK.name, movementType = MovementType.OFFENSE.name
    ) //25
    val rearSideKick = Technique(
        name = "Rear Side Kick",


        techniqueType = TechniqueType.KICK.name, movementType = MovementType.OFFENSE.name
    ) //26
    val backKick = Technique(
        name = "Back Kick",


        techniqueType = TechniqueType.KICK.name, movementType = MovementType.OFFENSE.name
    ) //27
    val spinningBackKick = Technique(
        name = "Spinning Back Kick",


        techniqueType = TechniqueType.KICK.name, movementType = MovementType.OFFENSE.name
    ) //28
    val hookKick = Technique(
        name = "Hook Kick",


        techniqueType = TechniqueType.KICK.name, movementType = MovementType.OFFENSE.name
    ) //29
    val axeKick = Technique(
        name = "Axe Kick",


        techniqueType = TechniqueType.KICK.name, movementType = MovementType.OFFENSE.name
    ) //30
    val legKick = Technique(
        name = "Leg Kick",


        techniqueType = TechniqueType.KICK.name, movementType = MovementType.OFFENSE.name
    ) //31
    val obliqueKick = Technique(
        name = "Oblique Kick",


        techniqueType = TechniqueType.KICK.name, movementType = MovementType.OFFENSE.name
    ) //32
    val sweep = Technique(
        name = "Sweep",


        techniqueType = TechniqueType.KICK.name, movementType = MovementType.OFFENSE.name
    ) //*****57*****

    /*Knee*/
    val leadKhaoThon = Technique(
        name = "Khao Thon (Lead)",


        techniqueType = TechniqueType.KNEE.name, movementType = MovementType.OFFENSE.name
    ) //33
    val rearKhaoThon = Technique(
        name = "Khao Thon (Rear)",


        techniqueType = TechniqueType.KNEE.name, movementType = MovementType.OFFENSE.name
    ) //34
    val leadKhaoChiang = Technique(
        name = "Khao Chiang (Lead)",


        techniqueType = TechniqueType.KNEE.name, movementType = MovementType.OFFENSE.name
    ) //35
    val rearKhaoChiang = Technique(
        name = "Khao Chiang (Rear)",


        techniqueType = TechniqueType.KNEE.name, movementType = MovementType.OFFENSE.name
    ) //36
    val leadKhaoTat = Technique(
        name = "Khao Tat (Lead)",


        techniqueType = TechniqueType.KNEE.name, movementType = MovementType.OFFENSE.name
    ) //37
    val rearKhaoTat = Technique(
        name = "Khao Tat (Rear)",


        techniqueType = TechniqueType.KNEE.name, movementType = MovementType.OFFENSE.name
    ) //38
    val khaoLoi = Technique(
        name = "Khao Loi",


        techniqueType = TechniqueType.KNEE.name, movementType = MovementType.OFFENSE.name
    ) //39

    // Special
    val faint = Technique(
        name = "Faint",
        techniqueType = TechniqueType.SPECIAL.name,
        movementType = MovementType.OFFENSE.name,
    ) //58

    val bodyShot = Technique(
        name = "To the body",
        techniqueType = TechniqueType.SPECIAL.name,
        movementType = MovementType.OFFENSE.name,
    ) //59

    val legShot = Technique(
        name = "To the leg",
        techniqueType = TechniqueType.SPECIAL.name,
        movementType = MovementType.OFFENSE.name,
    ) //60

    /*Hand Block*/ // colors need to change
    val block = Technique(
        name = "Block",
        techniqueType = TechniqueType.HAND_BLOCK.name,
        movementType = MovementType.DEFENSE.name,
        color = "#FFFF0000"
    ) //40
    val coverUp = Technique(
        name = "Cover Up",
        techniqueType = TechniqueType.HAND_BLOCK.name,
        movementType = MovementType.DEFENSE.name,
        color = "#FF000000"
    ) //41
    val parry = Technique(
        name = "Parry",
        techniqueType = TechniqueType.HAND_BLOCK.name,
        movementType = MovementType.DEFENSE.name,
        color = "#FF0000FF"
    ) //42
    val clinch = Technique(
        name = "Clinch",
        techniqueType = TechniqueType.HAND_BLOCK.name,
        movementType = MovementType.DEFENSE.name,
        color = "#FF00FF00"
    ) //43

    /*Shin Block*/ // colors need to change
    val check = Technique(
        name = "Check",
        techniqueType = TechniqueType.SHIN_BLOCK.name,
        movementType = MovementType.DEFENSE.name,
        color = "#FFFF5500"
    ) //44
    val catch = Technique(
        name = "Catch",
        techniqueType = TechniqueType.SHIN_BLOCK.name,
        movementType = MovementType.DEFENSE.name,
        color = "#FF00FFAA"
    ) //45

    /*Footwork*/ // colors need to change
    val switchStance = Technique(
        name = "switchStance",
        techniqueType = TechniqueType.FOOTWORK.name,
        movementType = MovementType.DEFENSE.name,
        color = "#FFFFC855"
    ) //46
    val stepIn = Technique(
        name = "Step In",
        techniqueType = TechniqueType.FOOTWORK.name,
        movementType = MovementType.DEFENSE.name,
        color = "#FF550000"
    ) //47
    val stepOut = Technique(
        name = "Step Out",
        techniqueType = TechniqueType.FOOTWORK.name,
        movementType = MovementType.DEFENSE.name,
        color = "#FF000055"
    ) //48
    val stepLeft = Technique(
        name = "Step Left",
        techniqueType = TechniqueType.FOOTWORK.name,
        movementType = MovementType.DEFENSE.name,
        color = "#FF555500"
    ) //49
    val stepRight = Technique(
        name = "Step Right",
        techniqueType = TechniqueType.FOOTWORK.name,
        movementType = MovementType.DEFENSE.name,
        color = "#FF005555"
    ) //50
    val pivot = Technique(
        name = "Pivot",
        techniqueType = TechniqueType.FOOTWORK.name,
        movementType = MovementType.DEFENSE.name,
        color = "#FF550055"
    ) //51

    /*Head Movement*/ // colors need to change
    val pull = Technique(
        name = "Pull",
        techniqueType = TechniqueType.HEAD_MOVEMENT.name,
        movementType = MovementType.DEFENSE.name,
        color = "#FF3200B4"
    ) //52
    val slipInside = Technique(
        name = "Slip Inside",
        techniqueType = TechniqueType.HEAD_MOVEMENT.name,
        movementType = MovementType.DEFENSE.name,
        color = "#FFAA3232"
    ) //53
    val slipOutside = Technique(
        name = "Slip Outside",
        techniqueType = TechniqueType.HEAD_MOVEMENT.name,
        movementType = MovementType.DEFENSE.name,
        color = "#FF0032AA"
    ) //54
    val rollInside = Technique(
        name = "Roll Inside",
        techniqueType = TechniqueType.HEAD_MOVEMENT.name,
        movementType = MovementType.DEFENSE.name,
        color = "#FFFFFF00"
    ) //55
    val rollOutside = Technique(
        name = "Roll Outside",
        techniqueType = TechniqueType.HEAD_MOVEMENT.name,
        movementType = MovementType.DEFENSE.name,
        color = "#FFFF00FF"
    ) //56

    return techniqueDao.insertAll(
        jab,
        cross,
        leadHook,
        rearHook,
        leadUppercut,
        rearUppercut,
        checkHook,
        backFist,
        spinningBackFist,
        supermanPunch,
        overhand,
        leadSokTak,
        rearSokTak,
        leadSokNat,
        rearSokNat,
        leadSokTi,
        rearSokTi,
        leadSokPhung,
        rearSokPhung,
        sokKlap,
        leadTeep,
        rearTeep,
        leadRoundhouse,
        rearRoundhouse,
        leadSideKick,
        rearSideKick,
        backKick,
        spinningBackKick,
        hookKick,
        axeKick,
        legKick,
        obliqueKick,
        leadKhaoThon,
        rearKhaoThon,
        leadKhaoChiang,
        rearKhaoChiang,
        leadKhaoTat,
        rearKhaoTat,
        khaoLoi,
        block,
        coverUp,
        parry,
        clinch,
        check,
        catch,
        switchStance,
        stepIn,
        stepOut,
        stepLeft,
        stepRight,
        pivot,
        pull,
        slipInside,
        slipOutside,
        rollInside,
        rollOutside,
        sweep,
        faint,
        bodyShot,
        legShot
    )
}

private suspend fun populateComboTable(comboDaoProvider: Provider<ComboDao>): List<Long> {
    val comboDao = comboDaoProvider.get()

    val oneTwo = Combo(name = "Bread and butter", description = "Simple 1, 2") //1
    val twoThreeTwo = Combo(name = "Repeat till Death", description = "Simple 2, 3, 2") //2
    val upperCutHook =
        Combo(name = "Educated Lead-hand", description = "Lead hand hook then uppercut") //3
    val twoPullTwo =
        Combo(name = "Cross pull cross", description = "Baiting cross, pull, power cross") //4
    val getIn = Combo(name = "Jab, Step in, Lead Hook", description = "Close distance") //5
    val bee = Combo(name = "Jab cross, slip Cross", description = "Make it dirty") //6
    val trick =
        Combo(name = "Jab, Lead Roundhouse", description = "Make them slip into the shot") //7
    val trickRear =
        Combo(name = "Cross, Rear Roundhouse", description = "Make them slip into the shot") //8
    val kickCheck = Combo(name = "Rear Roundhouse, Check", description = "Sharp defense") //9
    val jabLegKick = Combo(name = "Jab, Calf kick", description = "Setup your low kicks") //10
    val powerHooks =
        Combo(name = "Lead hook, roll, lead hook", description = "Powerful Lead Hooks") //11
    val powerHand = Combo(
        name = "Land the Powershot", description = "Bait with the 1 2, roll, nail with the overhand"
    ) //12
    val legKickSweep = Combo(
        name = "Kick and Sweep", description = "Kick to the body, sweep when they fire back"
    ) //13
    val jabFaintUpper = Combo(
        name = "Lead-hand Finisher",
        description = "Jab, step in with faint jab, and throw the uppercut"
    ) //14

    return comboDao.insertAll(
        oneTwo,
        twoThreeTwo,
        upperCutHook,
        twoPullTwo,
        getIn,
        bee,
        trick,
        trickRear,
        kickCheck,
        jabLegKick,
        powerHooks,
        powerHand,
        legKickSweep,
        jabFaintUpper
    )
}

private suspend fun populateWorkoutTable(workoutDaoProvider: Provider<WorkoutDao>): List<Long> {
    val workoutDao = workoutDaoProvider.get()

    val firstWorkout = Workout(name = "First Workout", rounds = 5)
    val secondWorkout = Workout(
        name = "Second Workout", rounds = 3, roundLengthSeconds = 300, restsLengthSeconds = 60
    )
    val thirdWorkout = Workout(
        name = "Third Workout", rounds = 12, roundLengthSeconds = 180, restsLengthSeconds = 60
    )

    return workoutDao.insert(firstWorkout, secondWorkout, thirdWorkout)
}

private suspend fun insertComboWithTechniques(
    comboDaoProvider: Provider<ComboDao>, techniqueIds: List<Long>, comboIds: List<Long>
) {
    val comboDao = comboDaoProvider.get()

    insertRefs(comboDao, comboIds[0], techniqueIds[0], techniqueIds[1])
    insertRefs(comboDao, comboIds[1], techniqueIds[1], techniqueIds[2], techniqueIds[1])
    insertRefs(comboDao, comboIds[2], techniqueIds[2], techniqueIds[4])
    insertRefs(comboDao, comboIds[3], techniqueIds[51], techniqueIds[1])
    insertRefs(comboDao, comboIds[4], techniqueIds[0], techniqueIds[46], techniqueIds[0])
    insertRefs(comboDao, comboIds[5], techniqueIds[1], techniqueIds[52], techniqueIds[1])
    insertRefs(comboDao, comboIds[6], techniqueIds[0], techniqueIds[22])
    insertRefs(comboDao, comboIds[7], techniqueIds[0], techniqueIds[23])
    insertRefs(comboDao, comboIds[8], techniqueIds[23], techniqueIds[43])
    insertRefs(comboDao, comboIds[9], techniqueIds[0], techniqueIds[30])
    insertRefs(comboDao, comboIds[10], techniqueIds[2], techniqueIds[55], techniqueIds[2])
    insertRefs(
        comboDao, comboIds[11], techniqueIds[0], techniqueIds[1], techniqueIds[55], techniqueIds[11]
    )
    insertRefs(
        comboDao,
        comboIds[12],
        techniqueIds[23],
        techniqueIds[58],
        techniqueIds[44],
        techniqueIds[56]
    )
    insertRefs(
        comboDao,
        comboIds[13],
        techniqueIds[0],
        techniqueIds[57],
        techniqueIds[0],
        techniqueIds[46],
        techniqueIds[55],
        techniqueIds[4]
    )
}

private suspend fun insertRefs(dao: ComboDao, comboId: Long, vararg techniqueIds: Long) {
    for (techniqueId in techniqueIds)
        dao.insertComboTechniqueCrossRef(ComboTechniqueCrossRef(comboId, techniqueId))
}

private suspend fun insertWorkoutWithCombo(
    workoutDaoProvider: Provider<WorkoutDao>, comboIdList: List<Long>, workoutIdList: List<Long>
) {
    val workoutDao = workoutDaoProvider.get()

    insertRefs(workoutDao, workoutIdList[0], comboIdList[0])
    insertRefs(workoutDao, workoutIdList[1], comboIdList[0], comboIdList[1])
    insertRefs(
        workoutDao,
        workoutIdList[2],
        comboIdList[0],
        comboIdList[0],
        comboIdList[0],
        comboIdList[1],
        comboIdList[2]
    )
}

private suspend fun insertRefs(dao: WorkoutDao, workoutId: Long, vararg comboIdList: Long) {
    for (comboId in comboIdList)
        dao.insertWorkoutComboCrossRef(WorkoutComboCrossRef(workoutId, comboId))
}

private suspend fun populateTrainingDateTable(
    workoutConclusionDaoProvider: Provider<WorkoutConclusionDao>
) {
    val workoutConclusionDao = workoutConclusionDaoProvider.get()

    val date1 = LocalDate.now().toEpochDay()
    val date2 = LocalDate.now().minusDays(1L).toEpochDay()
    val date7 = LocalDate.now().minusDays(2L).toEpochDay()

    val date3 = YearMonth.now().minusMonths(1L).atEndOfMonth().toEpochDay()

    val date4 = YearMonth.now().minusMonths(1L).atDay(10).toEpochDay()


    val date5 = YearMonth.now().minusMonths(2L).atDay(1).toEpochDay()

//    val date6 = YearMonth.now().minusMonths(2L).atDay(13).toEpochDay()


    val workoutConclusion1 = WorkoutConclusion(
        workoutId = 1,
        workoutName = "First Workout",
        isWorkoutAborted = true,
        trainingDateEpochDay = date1
    )
    val workoutConclusion2 = WorkoutConclusion(
        workoutId = 2,
        workoutName = "Second Workout",
        isWorkoutAborted = false,
        trainingDateEpochDay = date1
    )
    val workoutConclusion3 = WorkoutConclusion(
        workoutId = 1,
        workoutName = "First Workout",
        isWorkoutAborted = false,
        trainingDateEpochDay = date1
    )

    val workoutConclusion4 = WorkoutConclusion(
        workoutId = 1,
        workoutName = "First Workout",
        isWorkoutAborted = true,
        trainingDateEpochDay = date2
    )
    val workoutConclusion5 = WorkoutConclusion(
        workoutId = 2,
        workoutName = "Second Workout",
        isWorkoutAborted = true,
        trainingDateEpochDay = date2
    )

    val workoutConclusion6 = WorkoutConclusion(
        workoutId = 1,
        workoutName = "First Workout",
        isWorkoutAborted = false,
        trainingDateEpochDay = date7
    )
    val workoutConclusion7 = WorkoutConclusion(
        workoutId = 2,
        workoutName = "Second Workout",
        isWorkoutAborted = false,
        trainingDateEpochDay = date7
    )

    val workoutConclusion8 = WorkoutConclusion(
        workoutId = 1,
        workoutName = "First Workout",
        isWorkoutAborted = false,
        trainingDateEpochDay = date3
    )
    val workoutConclusion9 = WorkoutConclusion(
        workoutId = 1,
        workoutName = "First Workout",
        isWorkoutAborted = false,
        trainingDateEpochDay = date3
    )
    val workoutConclusion10 = WorkoutConclusion(
        workoutId = 2,
        workoutName = "Second Workout",
        isWorkoutAborted = false,
        trainingDateEpochDay = date3
    )

    val workoutConclusion11 = WorkoutConclusion(
        workoutId = 1,
        workoutName = "First Workout",
        isWorkoutAborted = false,
        trainingDateEpochDay = date4
    )
    val workoutConclusion12 = WorkoutConclusion(
        workoutId = 1,
        workoutName = "First Workout",
        isWorkoutAborted = false,
        trainingDateEpochDay = date4
    )
    val workoutConclusion13 = WorkoutConclusion(
        workoutId = 2,
        workoutName = "Second Workout",
        isWorkoutAborted = false,
        trainingDateEpochDay = date4
    )

    val workoutConclusion14 = WorkoutConclusion(
        workoutId = 1,
        workoutName = "First Workout",
        isWorkoutAborted = true,
        trainingDateEpochDay = date5
    )

    workoutConclusionDao.insertAll(
        workoutConclusion1,
        workoutConclusion2,
        workoutConclusion3,
        workoutConclusion4,
        workoutConclusion5,
        workoutConclusion6,
        workoutConclusion7,
        workoutConclusion8,
        workoutConclusion9,
        workoutConclusion10,
        workoutConclusion11,
        workoutConclusion12,
        workoutConclusion13,
        workoutConclusion14
    )
}