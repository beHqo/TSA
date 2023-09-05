package com.example.android.strikingarts.ui.model

import com.example.android.strikingarts.domain.model.ImmutableList

data class PlayableCombo(
    val audioStringList: ImmutableList<String> = ImmutableList(),
    val audioDurationMillis: Long = 0,
    val delayMillis: Long = 0,
)