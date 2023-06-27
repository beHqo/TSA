package com.example.android.strikingarts.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.example.android.strikingarts.mediaplayer.PlayerConstants.SILENCE_AUDIO_FILE
import kotlinx.parcelize.Parcelize

interface AudioAttributes {
    val name: String
    val audioString: String
    val durationMilli: Long
}

@Parcelize
data class AssetAudioAttributes(
    override val name: String = "",
    override val audioString: String = SILENCE_AUDIO_FILE,
    override val durationMilli: Long = 300
) : Parcelable, AudioAttributes

@Parcelize
data class UriAudioAttributes(
    override val name: String = "",
    override val audioString: String = "",
    override val durationMilli: Long = 0,
    val sizeByte: Int = 0
) : Parcelable, AudioAttributes

@Immutable
object SilenceAudioAttributes : AudioAttributes {
    override val name = ""
    override val audioString = SILENCE_AUDIO_FILE
    override val durationMilli = 250L

    override fun toString(): String =
        "SilenceAudioAttributes(name=$SILENCE_AUDIO_FILE, $durationMilli=$durationMilli)"

    override fun hashCode(): Int =
        17 + this.durationMilli.hashCode() + SILENCE_AUDIO_FILE.hashCode()

    override fun equals(other: Any?): Boolean =
        this === other || !(other == null || javaClass != other.javaClass)
}