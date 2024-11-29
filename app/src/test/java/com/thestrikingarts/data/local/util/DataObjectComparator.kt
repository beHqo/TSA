package com.thestrikingarts.data.local.util

import com.thestrikingarts.domain.model.AudioAttributes
import com.thestrikingarts.domain.model.Combo
import com.thestrikingarts.domain.model.Technique
import com.thestrikingarts.domain.model.Workout
import com.thestrikingarts.domain.model.WorkoutResult
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

fun assertAudioAttributesAreEqual(a: AudioAttributes?, b: AudioAttributes) {
    a shouldNotBe null
    a?.name shouldBe b.name
    a?.audioString shouldBe b.audioString
    a?.durationMillis shouldBe b.durationMillis
}

fun assertTechniquesAreEqual(a: Technique?, b: Technique) {
    a shouldNotBe null
    a?.name shouldBe b.name
    a?.num shouldBe b.num
    a?.movementType shouldBe b.movementType
    a?.techniqueType shouldBe b.techniqueType
    a?.color shouldBe b.color
    assertAudioAttributesAreEqual(a?.audioAttributes, b.audioAttributes)
}

fun assertCombosAreEqual(a: Combo?, b: Combo) {
    a shouldNotBe null
    a?.name shouldBe b.name
    a?.desc shouldBe b.desc
    a?.delayMillis shouldBe b.delayMillis
    for (i in a?.techniqueList?.indices ?: 0..1) {
        assertTechniquesAreEqual(a?.techniqueList?.get(i), b.techniqueList[i])
    }
}

fun assertWorkoutsAreEqual(a: Workout?, b: Workout) {
    a shouldNotBe null
    a?.name shouldBe b.name
    a?.rounds shouldBe b.rounds
    a?.roundLengthSeconds shouldBe b.roundLengthSeconds
    a?.restLengthSeconds shouldBe b.restLengthSeconds
    a?.subRounds shouldBe b.subRounds
    for (i in a?.comboList?.indices ?: 0..1) {
        assertCombosAreEqual(a?.comboList?.get(i), b.comboList[i])
    }
}

fun assertWorkoutResultsAreEqual(a: WorkoutResult?, b: WorkoutResult) {
    a shouldNotBe null
    a?.workoutId shouldBe b.workoutId
    a?.workoutName shouldBe b.workoutName
    a?.epochDay shouldBe b.epochDay
    a?.conclusion shouldBe b.conclusion
}