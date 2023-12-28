package com.example.android.strikingarts.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class TechniqueListItem(
    val id: Long = 0L,
    val name: String = "",
    val num: String = "",
    val audioAttributes: AudioAttributes = SilenceAudioAttributes,
    val color: String = "0",
    val movementType: MovementType = MovementType.OFFENSE,
    val techniqueType: TechniqueType = TechniqueType.PUNCH
)