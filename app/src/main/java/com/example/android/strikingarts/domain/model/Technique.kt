package com.example.android.strikingarts.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Technique(
    val id: Long = 0L,
    val name: String = "",
    val num: String = "",
    val audioAttributes: AudioAttributes = SilenceAudioAttributes,
    val color: String = "#00FFFFFF",
    val movementType: MovementType = MovementType.OFFENSE,
    val techniqueType: TechniqueType = TechniqueType.PUNCH
)