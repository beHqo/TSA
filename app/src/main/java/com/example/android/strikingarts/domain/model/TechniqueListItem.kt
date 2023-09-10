package com.example.android.strikingarts.domain.model

import androidx.compose.runtime.Immutable
import com.example.android.strikingarts.R
import com.example.android.strikingarts.domain.model.TechniqueCategory.OFFENSE

@Immutable
data class TechniqueListItem(
    val id: Long = 0L,
    val name: String = "",
    val num: String = "",
    val canBeBodyshot: Boolean = false,
    val canBeFaint: Boolean = false,
    val audioAttributes: AudioAttributes = SilenceAudioAttributes,
    val color: String = "0",
    val techniqueType: String = "",
    val movementType: String = OFFENSE,
    val imageRes: Int = R.drawable.none_color
)