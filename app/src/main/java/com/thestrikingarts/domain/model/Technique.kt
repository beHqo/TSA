package com.thestrikingarts.domain.model

import androidx.compose.runtime.Immutable
import com.thestrikingarts.domain.constant.transparentHexCode

@Immutable
data class Technique(
    val id: Long = 0L,
    val name: String = "",
    val num: String = "",
    val audioAttributes: AudioAttributes = SilenceAudioAttributes,
    val color: String = transparentHexCode,
    val movementType: MovementType = MovementType.OFFENSE,
    val techniqueType: TechniqueType = TechniqueType.PUNCH
)