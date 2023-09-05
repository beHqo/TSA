package com.example.android.strikingarts.hilt.module

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
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    val scope = CoroutineScope(SupervisorJob())
                    scope.launch(Dispatchers.IO) {
                        val techniqueIds = populateTechniqueTable(techniqueDaoProvider)
                        val comboIds = populateComboTable(comboDaoProvider)
                        val workoutIds = populateWorkoutTable(workoutDaoProvider)
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

                        val spearElbow = Technique(
                            name = "Spear Elbow",
                            num = "66",
                            canBeFaint = true,
                            canBeBodyshot = false,
                            techniqueType = "Elbow",
                            movementType = "Offense"
                        )
                        val spinningElbow = Technique(
                            name = "Spinning Elbow",
                            num = "77",
                            canBeFaint = true,
                            canBeBodyshot = false,
                            techniqueType = "Elbow",
                            movementType = "Offense"
                        )
                        val combo = Combo(
                            name = "Elbow Combo",
                            description = "Description",
                            delayMillis = 10
                        )

                        val techniqueDao = techniqueDaoProvider.get()!!
                        val comboDao = comboDaoProvider.get()!!

                        val spearElbowId = techniqueDao.insert(spearElbow)
                        val spinningElbowId = techniqueDao.insert(spinningElbow)

                        val comboId = comboDao.insert(combo)
                        comboDao.insertComboTechniqueCrossRef(
                            ComboTechniqueCrossRef(
                                comboId = comboId,
                                techniqueId = spearElbowId
                            )
                        )
                        comboDao.insertComboTechniqueCrossRef(
                            ComboTechniqueCrossRef(
                                comboId = comboId,
                                techniqueId = spearElbowId
                            )
                        )
                        comboDao.insertComboTechniqueCrossRef(
                            ComboTechniqueCrossRef(
                                comboId = comboId,
                                techniqueId = spinningElbowId
                            )
                        )
                        comboDao.insertComboTechniqueCrossRef(
                            ComboTechniqueCrossRef(
                                comboId = comboId,
                                techniqueId = spearElbowId
                            )
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
        name = "Jab",
        num = "1",
        canBeFaint = true,
        canBeBodyshot = true,
        techniqueType = "Punch",
        movementType = "Offense"
    ) //1
    val cross = Technique(
        name = "Cross",
        num = "2",
        canBeFaint = true,
        canBeBodyshot = true,
        techniqueType = "Punch",
        movementType = "Offense"
    ) //2
    val leadHook = Technique(
        name = "Lead Hook",
        num = "3",
        canBeFaint = true,
        canBeBodyshot = true,
        techniqueType = "Punch",
        movementType = "Offense"
    ) //3
    val rearHook = Technique(
        name = "Rear Hook",
        num = "4",
        canBeFaint = true,
        canBeBodyshot = true,
        techniqueType = "Punch",
        movementType = "Offense"
    ) //4
    val leadUppercut = Technique(
        name = "Lead Uppercut",
        num = "5",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Punch",
        movementType = "Offense"
    ) //5
    val rearUppercut = Technique(
        name = "Rear Uppercut",
        num = "6",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Punch",
        movementType = "Offense"
    ) //6
    val checkHook = Technique(
        name = "Check Hook",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Punch",
        movementType = "Offense"
    ) //7
    val backFist = Technique(
        name = "Back Fist",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Punch",
        movementType = "Offense"
    ) //8
    val spinningBackFist = Technique(
        name = "Spinning Back Fist",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Punch",
        movementType = "Offense"
    ) //9
    val supermanPunch = Technique(
        name = "Superman Punch",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Punch",
        movementType = "Offense"
    ) //10
    val overhand = Technique(
        name = "Overhand",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Punch",
        movementType = "Offense"
    ) //11

    /*Elbow*/
    val leadSokTak = Technique(
        name = "Sok Tat (Lead)",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Elbow",
        movementType = "Offense"
    ) //12
    val rearSokTak = Technique(
        name = "Sok Tat (Rear)",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Elbow",
        movementType = "Offense"
    ) //13
    val leadSokNat = Technique(
        name = "Sok Nat (Lead)",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Elbow",
        movementType = "Offense"
    ) //14
    val rearSokNat = Technique(
        name = "Sok Nat (Rear)",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Elbow",
        movementType = "Offense"
    ) //15
    val leadSokTi = Technique(
        name = "Sok Ti (Lead)",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Elbow",
        movementType = "Offense"
    ) //16
    val rearSokTi = Technique(
        name = "Sok Ti (Rear)",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Elbow",
        movementType = "Offense"
    ) //17
    val leadSokPhung = Technique(
        name = "Sok Phung (Lead)",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Elbow",
        movementType = "Offense"
    ) //18
    val rearSokPhung = Technique(
        name = "Sok Phung (Rear)",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Elbow",
        movementType = "Offense"
    ) //19
    val sokKlap = Technique(
        name = "Sok Klap",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Elbow",
        movementType = "Offense"
    ) //20

    /*Kick*/
    val leadTeep = Technique(
        name = "Lead Teep",
        canBeFaint = true,
        canBeBodyshot = true,
        techniqueType = "Kick",
        movementType = "Offense"
    ) //21
    val rearTeep = Technique(
        name = "Rear Teep",
        canBeFaint = true,
        canBeBodyshot = true,
        techniqueType = "Kick",
        movementType = "Offense"
    ) //22
    val leadRoundhouse = Technique(
        name = "Lead Roundhouse",
        canBeFaint = true,
        canBeBodyshot = true,
        techniqueType = "Kick",
        movementType = "Offense"
    ) //23
    val rearRoundhouse = Technique(
        name = "Rear Roundhouse",
        canBeFaint = true,
        canBeBodyshot = true,
        techniqueType = "Kick",
        movementType = "Offense"
    ) //24
    val leadSideKick = Technique(
        name = "Lead Side Kick",
        canBeFaint = true,
        canBeBodyshot = true,
        techniqueType = "Kick",
        movementType = "Offense"
    ) //25
    val rearSideKick = Technique(
        name = "Rear Side Kick",
        canBeFaint = true,
        canBeBodyshot = true,
        techniqueType = "Kick",
        movementType = "Offense"
    ) //26
    val backKick = Technique(
        name = "Back Kick",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Kick",
        movementType = "Offense"
    ) //27
    val spinningBackKick = Technique(
        name = "Spinning Back Kick",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Kick",
        movementType = "Offense"
    ) //28
    val hookKick = Technique(
        name = "Hook Kick",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Kick",
        movementType = "Offense"
    ) //29
    val axeKick = Technique(
        name = "Axe Kick",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Kick",
        movementType = "Offense"
    ) //30
    val legKick = Technique(
        name = "Leg Kick",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Kick",
        movementType = "Offense"
    ) //31
    val obliqueKick = Technique(
        name = "Oblique Kick",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Kick",
        movementType = "Offense"
    ) //32
    val sweep = Technique(
        name = "Sweep",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Kick",
        movementType = "Offense"
    ) //*****57*****

    /*Knee*/
    val leadKhaoThon = Technique(
        name = "Khao Thon (Lead)",
        canBeFaint = true,
        canBeBodyshot = true,
        techniqueType = "Knee",
        movementType = "Offense"
    ) //33
    val rearKhaoThon = Technique(
        name = "Khao Thon (Rear)",
        canBeFaint = true,
        canBeBodyshot = true,
        techniqueType = "Knee",
        movementType = "Offense"
    ) //34
    val leadKhaoChiang = Technique(
        name = "Khao Chiang (Lead)",
        canBeFaint = true,
        canBeBodyshot = true,
        techniqueType = "Knee",
        movementType = "Offense"
    ) //35
    val rearKhaoChiang = Technique(
        name = "Khao Chiang (Rear)",
        canBeFaint = true,
        canBeBodyshot = true,
        techniqueType = "Knee",
        movementType = "Offense"
    ) //36
    val leadKhaoTat = Technique(
        name = "Khao Tat (Lead)",
        canBeFaint = true,
        canBeBodyshot = true,
        techniqueType = "Knee",
        movementType = "Offense"
    ) //37
    val rearKhaoTat = Technique(
        name = "Khao Tat (Rear)",
        canBeFaint = true,
        canBeBodyshot = true,
        techniqueType = "Knee",
        movementType = "Offense"
    ) //38
    val khaoLoi = Technique(
        name = "Khao Loi",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Knee",
        movementType = "Offense"
    ) //39

    /*Hand Block*/ // colors need to change
    val block = Technique(
        name = "Block",
        techniqueType = "Hand Block",
        movementType = "Defense",
        color = Color.Red.value.toString()
    ) //40
    val coverUp = Technique(
        name = "Cover Up",
        techniqueType = "Hand Block",
        movementType = "Defense",
        color = Color.Red.value.toString()
    ) //41
    val parry = Technique(
        name = "Parry",
        techniqueType = "Hand Block",
        movementType = "Defense",
        color = Color.Red.value.toString()
    ) //42
    val clinch = Technique(
        name = "Clinch",
        techniqueType = "Hand Block",
        movementType = "Defense",
        color = Color.Red.value.toString()
    ) //43

    /*Shin Block*/ // colors need to change
    val check = Technique(
        name = "Check",
        techniqueType = "Shin Block",
        movementType = "Defense",
        color = Color.Red.value.toString()
    ) //44
    val catch = Technique(
        name = "Catch",
        techniqueType = "Shin Block",
        movementType = "Defense",
        color = Color.Red.value.toString()
    ) //45

    /*Footwork*/ // colors need to change
    val switchStance = Technique(
        name = "switchStance",
        techniqueType = "Footwork",
        movementType = "Defense",
        color = Color.Red.value.toString()
    ) //46
    val stepIn = Technique(
        name = "Step In",
        techniqueType = "Footwork",
        movementType = "Defense",
        color = Color.Red.value.toString()
    ) //47
    val stepOut = Technique(
        name = "Step Out",
        techniqueType = "Footwork",
        movementType = "Defense",
        color = Color.Red.value.toString()
    ) //48
    val stepLeft = Technique(
        name = "Step Left",
        techniqueType = "Footwork",
        movementType = "Defense",
        color = Color.Red.value.toString()
    ) //49
    val stepRight = Technique(
        name = "Step Right",
        techniqueType = "Footwork",
        movementType = "Defense",
        color = Color.Red.value.toString()
    ) //50
    val pivot = Technique(
        name = "Pivot",
        techniqueType = "Footwork",
        movementType = "Defense",
        color = Color.Red.value.toString()
    ) //51

    /*Head Movement*/ // colors need to change
    val pull = Technique(
        name = "Pull",
        techniqueType = "Head Movement",
        movementType = "Defense",
        color = Color.Red.value.toString()
    ) //52
    val slipInside = Technique(
        name = "Slip Inside",
        techniqueType = "Head Movement",
        movementType = "Defense",
        color = Color.Red.value.toString()
    ) //53
    val slipOutside = Technique(
        name = "Slip Outside",
        techniqueType = "Head Movement",
        movementType = "Defense",
        color = Color.Red.value.toString()
    ) //54
    val rollInside = Technique(
        name = "Roll Inside",
        techniqueType = "Head Movement",
        movementType = "Defense",
        color = Color.Red.value.toString()
    ) //55
    val rollOutside = Technique(
        name = "Roll Outside",
        techniqueType = "Head Movement",
        movementType = "Defense",
        color = Color.Red.value.toString()
    ) //56

    return techniqueDao.insertAll(
        jab, cross, leadHook, rearHook, leadUppercut, rearUppercut,
        checkHook, backFist, spinningBackFist, supermanPunch, overhand,
        leadSokTak, rearSokTak, leadSokNat, rearSokNat, leadSokTi, rearSokTi,
        leadSokPhung, rearSokPhung, sokKlap,
        leadTeep, rearTeep, leadRoundhouse, rearRoundhouse, leadSideKick,
        rearSideKick, backKick, spinningBackKick, hookKick, axeKick,
        legKick, obliqueKick,
        leadKhaoThon, rearKhaoThon, leadKhaoChiang, rearKhaoChiang,
        leadKhaoTat, rearKhaoTat, khaoLoi,
        block, coverUp, parry, clinch,
        check, catch,
        switchStance, stepIn, stepOut, stepLeft, stepRight, pivot,
        pull, slipInside, slipOutside, rollInside, rollOutside,
        sweep
    )
}

private suspend fun populateComboTable(comboDaoProvider: Provider<ComboDao>): List<Long> {
    val comboDao = comboDaoProvider.get()

    val oneTwo = Combo(name = "Bread and butter", description = "Simple 1, 2") //1
    val twoThreeTwo = Combo(name = "Repeat till Death", description = "Simple 2, 3, 2") //2
    val upperCutHook =
        Combo(name = "Educated lead-hand", description = "Lead hand hook then uppercut") //3
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
        name = "Land the Powershot",
        description = "Bait with the 1 2, roll, nail with the overhand"
    ) //12
    val legKickSweep = Combo(
        name = "Kick and Sweep",
        description = "Kick to the body, sweep when they fire back"
    ) //13

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
        legKickSweep
    )
}

private suspend fun populateWorkoutTable(workoutDaoProvider: Provider<WorkoutDao>): List<Long> {
    val workoutDao = workoutDaoProvider.get()

    val firstWorkout = Workout(name = "First Workout", rounds = 5)
    val secondWorkout = Workout(
        name = "Second Workout",
        rounds = 3,
        roundLengthSeconds = 300,
        restsLengthSeconds = 60
    )
    val thirdWorkout = Workout(
        name = "Third Workout",
        rounds = 12,
        roundLengthSeconds = 180,
        restsLengthSeconds = 60
    )

    return workoutDao.insert(firstWorkout, secondWorkout, thirdWorkout)
}

private suspend fun insertComboWithTechniques(
    comboDaoProvider: Provider<ComboDao>,
    techniqueIds: List<Long>,
    comboIds: List<Long>
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
        comboDao,
        comboIds[11],
        techniqueIds[0],
        techniqueIds[1],
        techniqueIds[55],
        techniqueIds[11]
    )
    insertRefs(comboDao, comboIds[12], techniqueIds[23], techniqueIds[44], techniqueIds[56])
}

private suspend fun insertRefs(dao: ComboDao, comboId: Long, vararg techniqueIds: Long) {
    for (techniqueId in techniqueIds) {
        dao.insertComboTechniqueCrossRef(
            ComboTechniqueCrossRef(
                comboId = comboId,
                techniqueId = techniqueId
            )
        )
    }
}

private suspend fun insertWorkoutWithCombo(
    workoutDaoProvider: Provider<WorkoutDao>,
    comboIdList: List<Long>,
    workoutIdList: List<Long>
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
    for (comboId in comboIdList) {
        dao.insertWorkoutComboCrossRef(
            WorkoutComboCrossRef(
                workoutId = workoutId,
                comboId = comboId
            )
        )
    }
}