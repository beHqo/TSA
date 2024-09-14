package com.example.android.strikingarts.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.example.android.strikingarts.ui.audioplayers.PlayerConstants.SILENCE_AUDIO_FILE
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

interface AudioAttributes {
    val id: Long?
    val name: String
    val audioString: String
    val durationMillis: Long
}

@Parcelize
data class ResourceAudioAttributes(
    override val id: Long = 0L,
    override val name: String = "",
    override val audioString: String = SILENCE_AUDIO_FILE,
    override val durationMillis: Long = 300
) : Parcelable, AudioAttributes

@Parcelize
data class UriAudioAttributes(
    override val id: Long = 0L,
    override val name: String = "",
    override val audioString: String = "",
    override val durationMillis: Long = 0,
    val sizeByte: Int = 0
) : Parcelable, AudioAttributes

@Immutable
@Parcelize
data object SilenceAudioAttributes : Parcelable, AudioAttributes {
    @IgnoredOnParcel
    override val id: Long? = null

    @IgnoredOnParcel
    override val name: String = ""

    @IgnoredOnParcel
    override val audioString: String = SILENCE_AUDIO_FILE

    @IgnoredOnParcel
    override val durationMillis: Long = 300
}