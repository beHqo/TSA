package com.example.android.strikingarts.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.math.abs

@Parcelize
data class Time(val minutes: Int = 0, val seconds: Int = 0) : Parcelable {
//    fun toMillieSeconds(): Long {
//        return ((this.minutes * 60000) + (this.seconds * 1000)).toLong()
//    }

    fun toSeconds(): Int {
        return (this.minutes * 60) + this.seconds
    }

    fun asString(): String {
        val minutes = if (abs(this.minutes) < 10) "0${this.minutes}" else "$minutes"
        val seconds = if (abs(this.seconds) < 10) "0${this.seconds}" else "$seconds"

        return "$minutes : $seconds"
    }
}

fun Int.toTime(): Time = Time(this / 60, this % 60)

//fun Long.toTime(): Time {
//    val totalSeconds = this / 1000
//    val minutes = totalSeconds / 60
//    val seconds = totalSeconds - (minutes * 60)
//
//    return Time(minutes.toInt(), seconds.toInt())
//}