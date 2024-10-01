package com.example.android.strikingarts.data

import com.example.android.strikingarts.data.local.mapper.toDomainModel
import com.example.android.strikingarts.domain.constant.transparentHexCode
import com.example.android.strikingarts.domain.model.AudioAttributes
import com.example.android.strikingarts.domain.model.MovementType
import com.example.android.strikingarts.domain.model.Technique
import com.example.android.strikingarts.domain.model.TechniqueType
import com.example.android.strikingarts.domain.model.WorkoutConclusion
import io.kotest.matchers.shouldBe
import org.junit.Test
import tables.AudioAttributesTable
import tables.ComboTable
import tables.GetTechnique
import tables.GetTechniqueList
import tables.WorkoutResultTable
import tables.WorkoutTable

class MapperTest {
    @Test
    fun resourceAudioAttributesTableMapper() {
        val resourceAudioAttributesTable = AudioAttributesTable(
            audioAttributesId = 1L,
            name = "jab",
            path = "audio/techniques/jab.m4a",
            durationMillis = 398,
        )

        val converted = resourceAudioAttributesTable.toDomainModel()
        assertAudioAttributesAreEqual(resourceAudioAttributesTable, converted)
    }

    @Test
    fun uriAudioAttributesTableMapper() {
        val uriAudioAttributesTable = AudioAttributesTable(
            audioAttributesId = 1L,
            name = "jab",
            path = "content://jab.m4a",
            durationMillis = 398,
        )

        val converted = uriAudioAttributesTable.toDomainModel()
        assertAudioAttributesAreEqual(uriAudioAttributesTable, converted)
    }

    @Test
    fun testGetTechniqueMapper() {
        val getTechnique = GetTechnique(
            techniqueId = 1,
            name = "Jab",
            num = "1",
            movementType = MovementType.OFFENSE,
            techniqueType = TechniqueType.PUNCH,
            color = transparentHexCode,
            audioAttributesId = null,
            audioName = null,
            audioDuration = null,
            audioFilePath = null,
        )

        val converted = getTechnique.toDomainModel()
        assertTechniquesAreEqual(getTechnique, converted)
    }

    @Test
    fun testGetTechniqueListMapper() {
        val getTechniqueList = GetTechniqueList(
            techniqueId = 1,
            name = "Jab",
            num = "1",
            movementType = MovementType.OFFENSE,
            techniqueType = TechniqueType.PUNCH,
            color = transparentHexCode,
            audioAttributesId = null,
            audioName = null,
            audioDuration = null,
            audioFilePath = null,
        )

        val converted = getTechniqueList.toDomainModel()
        assertTechniquesAreEqual(getTechniqueList, converted)
    }

    @Test
    fun testComboTableMapper() {
        val comboTable = ComboTable(
            comboId = 1,
            name = "Combo Name",
            desc = "Combo Description",
            delayAfterFinishedMillis = 1L
        )

        val converted = comboTable.toDomainModel()

        comboTable.comboId shouldBe converted.id
        comboTable.name shouldBe converted.name
        comboTable.desc shouldBe converted.desc
        comboTable.delayAfterFinishedMillis shouldBe converted.delayMillis
    }

    @Test
    fun testWorkoutTableMapper() {
        val workoutTable = WorkoutTable(
            workoutId = 1,
            name = "Workout Name",
            rounds = 1,
            roundLengthSeconds = 181,
            restLengthSeconds = 61,
            subRounds = 2
        )

        val converted = workoutTable.toDomainModel()
        workoutTable.workoutId shouldBe converted.id
        workoutTable.name shouldBe converted.name
        workoutTable.rounds shouldBe converted.rounds
        workoutTable.roundLengthSeconds shouldBe converted.roundLengthSeconds
        workoutTable.restLengthSeconds shouldBe converted.restLengthSeconds
        workoutTable.subRounds shouldBe converted.subRounds
    }

    @Test
    fun testWorkoutResultMapper() {
        val workoutResultTable = WorkoutResultTable(
            workoutResultId = 1,
            workoutId = 2,
            workoutName = "Workout Name",
            workoutConclusion = WorkoutConclusion.Aborted(true),
            trainingDateEpochDay = 1234
        )

        val converted = workoutResultTable.toDomainModel()

        workoutResultTable.workoutId shouldBe converted.workoutId
        workoutResultTable.workoutName shouldBe converted.workoutName
        workoutResultTable.workoutConclusion shouldBe converted.conclusion
        workoutResultTable.trainingDateEpochDay shouldBe converted.epochDay
    }

    private fun assertAudioAttributesAreEqual(
        audioAttributesTable: AudioAttributesTable, audioAttributes: AudioAttributes
    ) {
        audioAttributesTable.audioAttributesId shouldBe audioAttributes.id
        audioAttributesTable.name shouldBe audioAttributes.name
        audioAttributesTable.path shouldBe audioAttributes.audioString
        audioAttributesTable.durationMillis shouldBe audioAttributes.durationMillis
    }

    private fun assertTechniquesAreEqual(
        getTechnique: GetTechnique, technique: Technique
    ) {
        getTechnique.techniqueId shouldBe technique.id
        getTechnique.name shouldBe technique.name
        getTechnique.num shouldBe technique.num
        getTechnique.movementType shouldBe technique.movementType
        getTechnique.techniqueType shouldBe technique.techniqueType
        getTechnique.color shouldBe technique.color
    }

    private fun assertTechniquesAreEqual(
        getTechniqueList: GetTechniqueList, technique: Technique
    ) {
        getTechniqueList.techniqueId shouldBe technique.id
        getTechniqueList.name shouldBe technique.name
        getTechniqueList.num shouldBe technique.num
        getTechniqueList.movementType shouldBe technique.movementType
        getTechniqueList.techniqueType shouldBe technique.techniqueType
        getTechniqueList.color shouldBe technique.color
    }
}