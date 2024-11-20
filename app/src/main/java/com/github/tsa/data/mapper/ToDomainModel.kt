package com.github.tsa.data.mapper

import com.github.tsa.domain.constant.transparentHexCode
import com.github.tsa.domain.model.AudioAttributes
import com.github.tsa.domain.model.Combo
import com.github.tsa.domain.model.ResourceAudioAttributes
import com.github.tsa.domain.model.SilenceAudioAttributes
import com.github.tsa.domain.model.Technique
import com.github.tsa.domain.model.UriAudioAttributes
import com.github.tsa.domain.model.Workout
import com.github.tsa.domain.model.WorkoutResult
import com.github.tsa.domainandroid.audioplayers.mediaplayer.isUriString
import tables.AudioAttributesTable
import tables.ComboTable
import tables.GetTechnique
import tables.GetTechniqueList
import tables.WorkoutResultTable
import tables.WorkoutTable

fun AudioAttributesTable.toDomainModel(): AudioAttributes = when {
    this.path.isUriString() -> UriAudioAttributes(
        id = this.audioAttributesId,
        name = this.name,
        audioString = this.path,
        durationMillis = this.durationMillis
    )

    else -> ResourceAudioAttributes(
        id = this.audioAttributesId,
        name = this.name,
        audioString = this.path,
        durationMillis = this.durationMillis
    )
}

fun GetTechnique.toDomainModel() = Technique(
    id = this.techniqueId,
    name = this.name,
    num = this.num,
    movementType = this.movementType,
    techniqueType = this.techniqueType,
    audioAttributes = getAudioAttributes(
        this.audioAttributesId, this.audioName, this.audioDuration, this.audioFilePath
    ),
    color = this.color ?: transparentHexCode
)

fun GetTechniqueList.toDomainModel() = Technique(
    id = this.techniqueId,
    name = this.name,
    num = this.num,
    movementType = this.movementType,
    techniqueType = this.techniqueType,
    audioAttributes = getAudioAttributes(
        this.audioAttributesId, this.audioName, this.audioDuration, this.audioFilePath
    ),
    color = this.color ?: transparentHexCode
)

private fun getAudioAttributes(
    audioAttributesId: Long?, audioName: String?, audioDuration: Long?, audioFilePath: String?
): AudioAttributes = if (audioAttributesId == null) SilenceAudioAttributes else {
    val fileName = audioName ?: ""
    val duration = audioDuration ?: 0L
    val path = audioFilePath ?: ""

    if (path.isUriString()) UriAudioAttributes(audioAttributesId, fileName, path, duration)
    else ResourceAudioAttributes(audioAttributesId, fileName, path, duration)
}

fun ComboTable.toDomainModel(techniqueList: List<Technique> = emptyList()) = Combo(
    id = this.comboId,
    name = this.name,
    desc = this.desc,
    delayMillis = this.delayAfterFinishedMillis,
    techniqueList = techniqueList
)

fun WorkoutTable.toDomainModel(comboList: List<Combo> = emptyList()) = Workout(
    id = this.workoutId,
    name = this.name,
    rounds = this.rounds,
    roundLengthSeconds = this.roundLengthSeconds,
    restLengthSeconds = this.restLengthSeconds,
    subRounds = this.subRounds,
    comboList = comboList
)

fun WorkoutResultTable.toDomainModel() = WorkoutResult(
    id = workoutResultId,
    workoutId = workoutId,
    workoutName = workoutName,
    conclusion = workoutConclusion,
    epochDay = trainingDateEpochDay
)