package com.example.android.strikingarts.ui.model

import com.example.android.strikingarts.domain.model.AudioAttributes
import com.example.android.strikingarts.domain.model.SilenceAudioAttributes

data class PlayableTechnique(
    val color: String = "0",
    val audioAttributes: AudioAttributes = SilenceAudioAttributes
)