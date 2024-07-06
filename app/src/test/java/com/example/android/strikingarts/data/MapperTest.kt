package com.example.android.strikingarts.data

import com.example.android.strikingarts.data.local.mapper.toDomainModel
import com.example.android.strikingarts.domain.common.constants.transparentHexCode
import com.example.android.strikingarts.domain.model.AudioAttributes
import com.example.android.strikingarts.domain.model.MovementType
import com.example.android.strikingarts.domain.model.Technique
import com.example.android.strikingarts.domain.model.TechniqueType
import io.kotest.matchers.shouldBe
import org.junit.Test
import tables.Audio_attributes_table
import tables.Combo_table
import tables.GetTechnique
import tables.GetTechniqueList
import tables.Workout_result_table
import tables.Workout_table

class MapperTest {
    @Test
    fun assetAudioAttributesTableMapper() {
        val assetAudioAttributesTable = Audio_attributes_table(
            audio_attributes_id = 1L,
            name = "jab",
            path = "audio/techniques/jab.m4a",
            duration_millis = 398
        )

        val converted = assetAudioAttributesTable.toDomainModel()
        assertAudioAttributesAreEqual(assetAudioAttributesTable, converted)
    }

    @Test
    fun uriAudioAttributesTableMapper() {
        val uriAudioAttributesTable = Audio_attributes_table(
            audio_attributes_id = 1L,
            name = "jab",
            path = "content://jab.m4a",
            duration_millis = 398
        )

        val converted = uriAudioAttributesTable.toDomainModel()
        assertAudioAttributesAreEqual(uriAudioAttributesTable, converted)
    }

    @Test
    fun testGetTechniqueMapper() {
        val getTechnique = GetTechnique(
            technique_id = 1,
            name = "Jab",
            num = "1",
            is_offense = true,
            technique_type = TechniqueType.PUNCH.name,
            color = transparentHexCode,
            audio_attributes_id = null,
            audio_name = null,
            audio_duration = null,
            audio_file_path = null
        )

        val converted = getTechnique.toDomainModel()
        assertTechniquesAreEqual(getTechnique, converted)
    }

    @Test
    fun testGetTechniqueListMapper() {
        val getTechniqueList = GetTechniqueList(
            technique_id = 1,
            name = "Jab",
            num = "1",
            is_offense = true,
            technique_type = TechniqueType.PUNCH.name,
            color = transparentHexCode,
            audio_attributes_id = null,
            audio_name = null,
            audio_duration = null,
            audio_file_path = null
        )

        val converted = getTechniqueList.toDomainModel()
        assertTechniquesAreEqual(getTechniqueList, converted)
    }

    @Test
    fun testComboTableMapper() {
        val comboTable = Combo_table(
            combo_id = 1,
            name = "Combo Name",
            desc = "Combo Description",
            delay_after_finished_millis = 1L
        )

        val converted = comboTable.toDomainModel()

        comboTable.combo_id shouldBe converted.id
        comboTable.name shouldBe converted.name
        comboTable.desc shouldBe converted.desc
        comboTable.delay_after_finished_millis shouldBe converted.delayMillis
    }

    @Test
    fun testWorkoutTableMapper() {
        val workoutTable = Workout_table(
            workout_id = 1,
            name = "Workout Name",
            rounds = 1,
            round_length_seconds = 181,
            rest_length_seconds = 61,
            sub_rounds = 2
        )

        val converted = workoutTable.toDomainModel()
        workoutTable.workout_id shouldBe converted.id
        workoutTable.name shouldBe converted.name
        workoutTable.rounds shouldBe converted.rounds
        workoutTable.round_length_seconds shouldBe converted.roundLengthSeconds
        workoutTable.rest_length_seconds shouldBe converted.restLengthSeconds
        workoutTable.sub_rounds shouldBe converted.subRounds
    }

    @Test
    fun testWorkoutResultMapper() {
        val workoutResultTable = Workout_result_table(
            workout_conclusion_id = 1,
            workout_id = 2,
            workout_name = "Workout Name",
            is_workout_aborted = true,
            training_date_epoch_day = 1234
        )

        val converted = workoutResultTable.toDomainModel()

        workoutResultTable.workout_id shouldBe converted.workoutId
        workoutResultTable.workout_name shouldBe converted.workoutName
        workoutResultTable.is_workout_aborted shouldBe converted.isWorkoutAborted
        workoutResultTable.training_date_epoch_day shouldBe converted.epochDay
    }

    private fun assertAudioAttributesAreEqual(
        audioAttributesTable: Audio_attributes_table, audioAttributes: AudioAttributes
    ) {
        audioAttributesTable.audio_attributes_id shouldBe audioAttributes.id
        audioAttributesTable.name shouldBe audioAttributes.name
        audioAttributesTable.path shouldBe audioAttributes.audioString
        audioAttributesTable.duration_millis shouldBe audioAttributes.durationMillis
    }

    private fun assertTechniquesAreEqual(
        getTechnique: GetTechnique, techniqueListItem: Technique
    ) {
        getTechnique.technique_id shouldBe techniqueListItem.id
        getTechnique.name shouldBe techniqueListItem.name
        getTechnique.num shouldBe techniqueListItem.num
        techniqueListItem.movementType shouldBe if (getTechnique.is_offense) MovementType.OFFENSE else MovementType.DEFENSE
        getTechnique.technique_type shouldBe techniqueListItem.techniqueType.name
        getTechnique.color shouldBe techniqueListItem.color
    }

    private fun assertTechniquesAreEqual(
        getTechniqueList: GetTechniqueList, techniqueListItem: Technique
    ) {
        getTechniqueList.technique_id shouldBe techniqueListItem.id
        getTechniqueList.name shouldBe techniqueListItem.name
        getTechniqueList.num shouldBe techniqueListItem.num
        techniqueListItem.movementType shouldBe if (getTechniqueList.is_offense) MovementType.OFFENSE else MovementType.DEFENSE
        getTechniqueList.technique_type shouldBe techniqueListItem.techniqueType.name
        getTechniqueList.color shouldBe techniqueListItem.color
    }
}