package com.example.android.strikingarts.utils

import com.example.android.strikingarts.database.entity.Combo
import com.example.android.strikingarts.database.entity.ComboWithTechniques
import com.example.android.strikingarts.database.entity.Workout

fun getTechniqueNamesFromCombo(comboWithTechniques: ComboWithTechniques): String {
    return comboWithTechniques.techniques.map { it.name }.toString().drop(1).dropLast(1)
}

fun getTechniqueNumberFromCombo(comboWithTechniques: ComboWithTechniques): String {
    return comboWithTechniques.techniques.map { it.num.ifEmpty { it.name } }.toString().drop(0).dropLast(1)
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

    val numberOfRounds = workout.numberOfRounds

    val roundDurationMinutes = workout.roundsDurationMilli.div(600).toDouble()
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
