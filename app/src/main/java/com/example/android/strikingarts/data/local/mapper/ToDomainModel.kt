package com.example.android.strikingarts.data.local.mapper

import com.example.android.strikingarts.domain.common.constants.transparentHexCode
import com.example.android.strikingarts.domain.model.AssetAudioAttributes
import com.example.android.strikingarts.domain.model.AudioAttributes
import com.example.android.strikingarts.domain.model.Combo
import com.example.android.strikingarts.domain.model.MovementType
import com.example.android.strikingarts.domain.model.SilenceAudioAttributes
import com.example.android.strikingarts.domain.model.Technique
import com.example.android.strikingarts.domain.model.TechniqueType
import com.example.android.strikingarts.domain.model.UriAudioAttributes
import com.example.android.strikingarts.domain.model.Workout
import com.example.android.strikingarts.domain.model.WorkoutResult
import com.example.android.strikingarts.ui.audioplayers.mediaplayer.isUriString
import tables.Audio_attributes_table
import tables.Combo_table
import tables.GetTechnique
import tables.GetTechniqueList
import tables.Workout_result_table
import tables.Workout_table

fun Audio_attributes_table.toDomainModel(): AudioAttributes = when {
    this.path.isUriString() -> UriAudioAttributes(
        id = this.audio_attributes_id,
        name = this.name,
        audioString = this.path,
        durationMillis = this.duration_millis
    )

    else -> AssetAudioAttributes(
        id = this.audio_attributes_id,
        name = this.name,
        audioString = this.path,
        durationMillis = this.duration_millis
    )
}

fun GetTechnique.toDomainModel() = Technique(
    id = this.technique_id,
    name = this.name,
    num = this.num,
    movementType = getMovementType(this.is_offense),
    techniqueType = TechniqueType.valueOf(this.technique_type),
    audioAttributes = getAudioAttributes(
        this.audio_attributes_id, this.audio_name, this.audio_duration, this.audio_file_path
    ),
    color = this.color ?: transparentHexCode
)

fun GetTechniqueList.toDomainModel() = Technique(
    id = this.technique_id,
    name = this.name,
    num = this.num,
    movementType = getMovementType(this.is_offense),
    techniqueType = TechniqueType.valueOf(this.technique_type),
    audioAttributes = getAudioAttributes(
        this.audio_attributes_id, this.audio_name, this.audio_duration, this.audio_file_path
    ),
    color = this.color ?: transparentHexCode
)

private fun getMovementType(isOffense: Boolean): MovementType =
    if (isOffense) MovementType.OFFENSE else MovementType.DEFENSE

private fun getAudioAttributes(
    audioAttributesId: Long?, audioName: String?, audioDuration: Long?, audioFilePath: String?
): AudioAttributes = if (audioAttributesId == null) SilenceAudioAttributes else {
    val fileName = audioName ?: ""
    val duration = audioDuration ?: 0L
    val path = audioFilePath ?: ""

    if (path.isUriString()) UriAudioAttributes(audioAttributesId, fileName, path, duration)
    else AssetAudioAttributes(audioAttributesId, fileName, path, duration)
}

fun Combo_table.toDomainModel(techniqueList: List<Technique> = emptyList()) =
    Combo(
        id = this.combo_id,
        name = this.name,
        desc = this.desc,
        delayMillis = this.delay_after_finished_millis,
        techniqueList = techniqueList
    )

fun Workout_table.toDomainModel(comboList: List<Combo> = emptyList()) =
    Workout(
        id = this.workout_id,
        name = this.name,
        rounds = this.rounds,
        roundLengthSeconds = this.round_length_seconds,
        restLengthSeconds = this.rest_length_seconds,
        subRounds = this.sub_rounds,
        comboList = comboList
    )

fun Workout_result_table.toDomainModel() = WorkoutResult(
    workoutId = workout_id,
    workoutName = workout_name,
    isWorkoutAborted = is_workout_aborted,
    epochDay = training_date_epoch_day
)