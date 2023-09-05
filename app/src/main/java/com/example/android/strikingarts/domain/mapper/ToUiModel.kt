package com.example.android.strikingarts.domain.mapper

import androidx.compose.ui.graphics.Color
import com.example.android.strikingarts.domain.model.ComboListItem
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.toImmutableList
import com.example.android.strikingarts.ui.model.PlayableCombo

fun ComboListItem.toPlayableCombo(): PlayableCombo = PlayableCombo(
    audioStringList = this.getAudioStringList(),
    audioDurationMillis = this.getAudioDuration(),
    delayMillis = this.delayMillis,
)

fun ComboListItem.getAudioStringList(): ImmutableList<String> =
    this.techniqueList.map { it.audioAttributes.audioString }.toImmutableList()

fun ComboListItem.getTechniqueNums(): String =
    this.techniqueList.joinToString { it.num.ifEmpty { it.name } }

fun ComboListItem.getAudioDuration(): Long =
    this.techniqueList.sumOf { it.audioAttributes.durationMillis }

fun String.toColor(): Color = Color(this.toULong())