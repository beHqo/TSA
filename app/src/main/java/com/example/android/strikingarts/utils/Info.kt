package com.example.android.strikingarts.utils

import com.example.android.strikingarts.data.local.room.model.Combo
import com.example.android.strikingarts.data.local.room.model.Technique
import com.example.android.strikingarts.data.local.room.model.Workout
import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.domain.model.TechniqueListItem

fun getTechniqueNamesFromCombo(techniques: List<Technique>): String {
    return techniques.map { it.name }.toString().drop(1).dropLast(1)
}

fun getTechniqueNumberFromCombo(techniques: ImmutableList<TechniqueListItem>): String {
//    return techniques.map { it.num.ifEmpty { it.name } }.toString().drop(1).dropLast(1)
    return techniques.joinToString { it.num.ifEmpty { it.name } }
}

// TODO: Fix this shit
fun getWorkoutDetails(
    workout: Workout,
//    workoutsWithCombos: WorkoutsWithCombos,
): Array<String> {
    val details = mutableListOf<String>()

    details.add(getRoundsDetails(workout))
    details.add(getRestDetails(workout))
    details.add(getNumberOfCombos(listOf<Combo>())) //Obv needs work!

    return details.toTypedArray()
}

// TODO: Fix this shit(2)
private fun getRoundsDetails(workout: Workout): String {
    val roundDetails = StringBuffer()

    val numberOfRounds = workout.rounds

    val roundDurationMinutes = workout.roundDurationMilli.div(600).toDouble()
    val roundDurationFormatted =
        if (roundDurationMinutes % 1 == 0.0) roundDurationMinutes.toInt() else roundDurationMinutes


    if (numberOfRounds == 1)
        roundDetails.append("$numberOfRounds Round, ")
    else
        roundDetails.append("$numberOfRounds Rounds, ")

    if (roundDurationFormatted == 1.0)
        roundDetails.append("$roundDurationFormatted Minute each")
    else
        roundDetails.append("$roundDurationFormatted Minutes each")

    return roundDetails.toString()
}

private fun getRestDetails(workout: Workout): String {
    val restDurationSeconds = workout.restsDurationMilli.div(100).toInt()

    return "$restDurationSeconds Seconds Rest"
}

private fun getNumberOfCombos(combos: List<Combo>): String {
    return "${combos.size} Combos"
}
