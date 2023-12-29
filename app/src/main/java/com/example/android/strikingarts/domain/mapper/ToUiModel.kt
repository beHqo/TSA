package com.example.android.strikingarts.domain.mapper

import com.example.android.strikingarts.domain.model.ComboListItem
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.TechniqueRepresentationFormat
import com.example.android.strikingarts.domain.model.toImmutableList
import com.example.android.strikingarts.ui.model.PlayableCombo

fun ComboListItem.toPlayableCombo(): PlayableCombo = PlayableCombo(
    audioStringList = this.getAudioStringList(),
    audioDurationMillis = this.getAudioDuration(),
    delayMillis = this.delayMillis,
)

fun ComboListItem.getAudioStringList(): ImmutableList<String> =
    this.techniqueList.map { it.audioAttributes.audioString }.toImmutableList()

fun ComboListItem.getTechniqueRepresentation(form: TechniqueRepresentationFormat): String =
    this.techniqueList.let { techniqueList ->
        if (form == TechniqueRepresentationFormat.NUM) techniqueList.joinToString { it.num.ifEmpty { it.name } }
        else techniqueList.joinToString { it.name }
    }

fun ComboListItem.getAudioDuration(): Long =
    this.techniqueList.sumOf { it.audioAttributes.durationMillis }