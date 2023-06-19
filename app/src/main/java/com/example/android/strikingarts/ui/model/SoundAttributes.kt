package com.example.android.strikingarts.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SoundAttributes(
    val name: String = "", val size: Int = 0, val durationMilli: Long = 0
) : Parcelable