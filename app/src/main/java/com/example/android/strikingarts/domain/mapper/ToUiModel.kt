package com.example.android.strikingarts.domain.mapper

import com.example.android.strikingarts.domain.model.Combo
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.TechniqueRepresentationFormat
import com.example.android.strikingarts.domain.model.toImmutableList

fun Combo.getAudioStringList(): ImmutableList<String> =
    this.techniqueList.map { it.audioAttributes.audioString }.toImmutableList()

fun Combo.getTechniqueRepresentation(form: TechniqueRepresentationFormat): String =
    this.techniqueList.let { techniqueList ->
        if (form == TechniqueRepresentationFormat.NUM) techniqueList.joinToString { it.num.ifEmpty { it.name } }
        else techniqueList.joinToString { it.name }
    }

fun Combo.getAudioDuration(): Long =
    this.techniqueList.sumOf { it.audioAttributes.durationMillis }