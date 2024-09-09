package com.example.android.strikingarts.data

import com.example.android.strikingarts.domain.model.AssetAudioAttributes
import com.example.android.strikingarts.domain.model.Combo
import com.example.android.strikingarts.domain.model.MovementType
import com.example.android.strikingarts.domain.model.SilenceAudioAttributes
import com.example.android.strikingarts.domain.model.Technique
import com.example.android.strikingarts.domain.model.TechniqueType
import com.example.android.strikingarts.domain.model.UriAudioAttributes
import com.example.android.strikingarts.domain.model.Workout
import com.example.android.strikingarts.domain.model.WorkoutConclusion
import com.example.android.strikingarts.domain.model.WorkoutResult
import java.time.LocalDate

val jabAudioAttributes = UriAudioAttributes(
    id = 1, name = "jab.m4a", audioString = "content://jab.m4a", durationMillis = 398
)
val crossAudioAttributes = UriAudioAttributes(
    id = 2, name = "cross.m4a", audioString = "content://cross.m4a", durationMillis = 893
)
val leadHookAudioAttributes = UriAudioAttributes(
    id = 3, name = "lead_hook.m4a", audioString = "content://lead_hook.m4a", durationMillis = 708
)

val assetAudioAttributes = AssetAudioAttributes(
    id = 4, name = "asset", audioString = "audio/techniques/asset.m4a", durationMillis = 777
) // Not included in audioAttributesList because at this time AssetAudioAttributes objects are not inserted in db

val backFistAudioAttributesNotInDB = UriAudioAttributes(
    id = 5, name = "backfist.m4a", audioString = "content://backfist.m4a", durationMillis = 111
)

val audioAttributesList: List<UriAudioAttributes> =
    listOf(jabAudioAttributes, crossAudioAttributes, leadHookAudioAttributes)

val dickSlap = Technique(
    id = 0,
    name = "Dick Slap",
    num = "91",
    audioAttributes = SilenceAudioAttributes,
    movementType = MovementType.OFFENSE,
    techniqueType = TechniqueType.SPECIAL,
    color = "#00FFFFFF"
)

val jab = Technique(
    id = 1,
    name = "Jab",
    num = "1",
    audioAttributes = jabAudioAttributes,
    movementType = MovementType.OFFENSE,
    techniqueType = TechniqueType.PUNCH,
    color = "#00FFFFFF"
)

val cross = Technique(
    id = 2,
    name = "Cross",
    num = "2",
    audioAttributes = crossAudioAttributes,
    movementType = MovementType.OFFENSE,
    techniqueType = TechniqueType.PUNCH,
    color = "#00FFFFFF"
)

val leadHook = Technique(
    id = 3,
    name = "Lead Hook",
    num = "3",
    audioAttributes = leadHookAudioAttributes,
    movementType = MovementType.OFFENSE,
    techniqueType = TechniqueType.PUNCH,
    color = "#00FFFFFF"
)

val stepBack = Technique(
    id = 4,
    name = "Step Back",
    audioAttributes = SilenceAudioAttributes,
    movementType = MovementType.DEFENSE,
    techniqueType = TechniqueType.FOOTWORK,
    color = "#FF0050"
)

val stepForward = Technique(
    id = 5,
    name = "Step Forward",
    audioAttributes = SilenceAudioAttributes,
    movementType = MovementType.DEFENSE,
    techniqueType = TechniqueType.FOOTWORK,
    color = "##00FF00"
)

val leadHighKick = Technique(
    id = 6,
    name = "Lead High Kick",
    audioAttributes = SilenceAudioAttributes,
    movementType = MovementType.OFFENSE,
    techniqueType = TechniqueType.KICK,
    color = "#00FFFFFF"
)

val rearHighKick = Technique(
    id = 7,
    name = "Rear High Kick",
    audioAttributes = SilenceAudioAttributes,
    movementType = MovementType.OFFENSE,
    techniqueType = TechniqueType.KICK,
    color = "#00FFFFFF"
)

val spearElbowNotInDB = Technique(
    id = 8,
    name = "Spear Elbow",
    audioAttributes = SilenceAudioAttributes,
    movementType = MovementType.OFFENSE,
    techniqueType = TechniqueType.ELBOW,
    color = "#00FFFFFF"
)

val slashingElbowNotInDB = Technique(
    id = 8,
    name = "Slashing Elbow",
    audioAttributes = SilenceAudioAttributes,
    movementType = MovementType.OFFENSE,
    techniqueType = TechniqueType.ELBOW,
    color = "#00FFFFFF"
)

val jabNotInDB = Technique(
    id = 9,
    name = "Jab",
    num = "1",
    audioAttributes = jabAudioAttributes,
    movementType = MovementType.OFFENSE,
    techniqueType = TechniqueType.PUNCH,
    color = "#00FFFFFF"
)

val listOfTechniques =
    listOf(jab, cross, leadHook, stepBack, stepForward, leadHighKick, rearHighKick)

val jabCrossJab = Combo(
    id = 1,
    name = "Jab Cross Jab",
    desc = "1 2 1",
    delayMillis = 3000,
    techniqueList = listOf(jab, cross, jab)
)

val crossStepBackCrossLeadHook = Combo(
    id = 2,
    name = "Cross Step Back Cross LeadHook",
    desc = "2 step back 2 3",
    delayMillis = 5000,
    techniqueList = listOf(cross, stepBack, cross, leadHook)
)

val stepBackLeadHighKick = Combo(
    id = 3,
    name = "step back lead high kick",
    desc = "step back, LHK",
    delayMillis = 1000,
    techniqueList = listOf(stepBack, leadHighKick)
)

val stepForwardSpearElbowNotInDB = Combo(
    id = 4,
    name = "Step Forward, Spear Elbow",
    desc = "Make 'em bleed",
    delayMillis = 3000,
    techniqueList = listOf(stepForward, spearElbowNotInDB)
)

val rearHighKickStepForwardSlashingElbowNotInDB = Combo(
    id = 5,
    name = "Rear high kick, Step forward, Slashing elbow",
    desc = "kick, close the distance, cut",
    delayMillis = 10000,
    techniqueList = listOf(rearHighKick, stepForward, slashingElbowNotInDB)
)

val longComboNotInDB = Combo(
    id = 6,
    name = "Just throw shit for an hour",
    desc = "Cardiac arrest",
    delayMillis = 3000,
    techniqueList = listOf(
        jab,
        cross,
        jab,
        leadHook,
        cross,
        leadHook,
        stepBack,
        leadHighKick,
        stepForward,
        spearElbowNotInDB,
        jab,
        jab,
        stepBack
    )
)

val listOfCombos = listOf(jabCrossJab, crossStepBackCrossLeadHook, stepBackLeadHighKick)

val workout1 = Workout(
    id = 1,
    name = "First Workout",
    rounds = 3,
    roundLengthSeconds = 180,
    restLengthSeconds = 60,
    subRounds = 2,
    comboList = listOf(stepBackLeadHighKick)
)

val workout2 = Workout(
    id = 2,
    name = "Second Workout",
    rounds = 5,
    roundLengthSeconds = 300,
    restLengthSeconds = 60,
    subRounds = 3,
    comboList = listOf(
        stepForwardSpearElbowNotInDB, rearHighKickStepForwardSlashingElbowNotInDB
    )
)

val workout3NotInDB = Workout(
    id = 3,
    name = "Third Workout",
    rounds = 10,
    roundLengthSeconds = 30,
    restLengthSeconds = 30,
    subRounds = 0,
    comboList = emptyList()
)

val workout4NotInDB = Workout(
    id = 4,
    name = "Forth Workout",
    rounds = 3,
    roundLengthSeconds = 300,
    restLengthSeconds = 60,
    subRounds = 10,
    comboList = emptyList()
)

val listOfWorkouts = listOf(workout1, workout2)

val workoutResultSuccess1 = WorkoutResult(
    id = 1,
    workoutId = 1,
    workoutName = "Finished 1",
    conclusion = WorkoutConclusion.Successful,
    epochDay = LocalDate.now().toEpochDay()
)

val workoutResultSuccess2 = WorkoutResult(
    id = 2,
    workoutId = 2,
    workoutName = "Finished 2",
    conclusion = WorkoutConclusion.Successful,
    epochDay = LocalDate.now().minusDays(1L).toEpochDay()
)

val workoutResultFailure1 = WorkoutResult(
    id = 3,
    workoutId = 3,
    workoutName = "Aborted 1",
    conclusion = WorkoutConclusion.Aborted(false),
    epochDay = LocalDate.now().minusDays(1L).toEpochDay()
)

val workoutResultFailure2 = WorkoutResult(
    id = 4,
    workoutId = 4,
    workoutName = "Aborted 2",
    conclusion = WorkoutConclusion.Aborted(false),
    epochDay = LocalDate.now().minusDays(2L).toEpochDay()
)

val workoutResultRedeemed1 = WorkoutResult(
    id = 5,
    workoutId = 5,
    workoutName = "Redeemed 1",
    conclusion = WorkoutConclusion.Aborted(true),
    epochDay = LocalDate.now().minusDays(2).toEpochDay()
)

val workoutResultRedeemedNotInDB = WorkoutResult(
    id = 6,
    workoutId = 6,
    workoutName = "Redeemed 2",
    conclusion = WorkoutConclusion.Aborted(true),
    epochDay = LocalDate.now().minusDays(2).toEpochDay()
)

val workoutResultSuccessNotInDB = WorkoutResult(
    id = 7,
    workoutId = 7,
    workoutName = "Finished 3",
    conclusion = WorkoutConclusion.Successful,
    epochDay = LocalDate.now().toEpochDay()
)

val workoutResultFailure3NotInDB = WorkoutResult(
    id = 8,
    workoutId = 8,
    workoutName = "Aborted 3",
    conclusion = WorkoutConclusion.Aborted(false),
    epochDay = LocalDate.now().toEpochDay()
)

val workoutResultList = listOf(
    workoutResultSuccess1,
    workoutResultSuccess2,
    workoutResultFailure1,
    workoutResultFailure2,
    workoutResultRedeemed1
)