package com.example.android.strikingarts.domain.mapper

import com.example.android.strikingarts.data.crossStepBackCrossLeadHook
import com.example.android.strikingarts.data.jabCrossJab
import com.example.android.strikingarts.data.workout2
import com.example.android.strikingarts.domain.model.WorkoutDetails
import io.kotest.matchers.shouldBe
import org.junit.Test

class ToDomainModelKtTest {

    @Test
    fun toWorkoutDetails() {
        val expected = WorkoutDetails(
            rounds = workout2.rounds,
            roundLengthSeconds = workout2.roundLengthSeconds,
            restLengthSeconds = workout2.restLengthSeconds
        )

        val actual = workout2.toWorkoutDetails()

        expected shouldBe actual
    }

    @Test
    fun getAudioStringList() {
        val expected = jabCrossJab.techniqueList.map { it.audioAttributes.audioString }
        val actual = jabCrossJab.getAudioStringList()

        expected shouldBe actual
    }

    @Test
    fun getAudioDuration() {
        val expected =
            crossStepBackCrossLeadHook.techniqueList.sumOf { it.audioAttributes.durationMillis }
        val actual = crossStepBackCrossLeadHook.getAudioDuration()

        expected shouldBe actual
    }
}