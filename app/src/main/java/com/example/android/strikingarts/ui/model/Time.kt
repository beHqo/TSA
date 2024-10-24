package com.example.android.strikingarts.ui.model

import android.os.Parcelable
import android.text.BidiFormatter
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Parcelize
data class Time(val minutes: Int = 0, val seconds: Int = 0) : Parcelable {
    fun toSeconds(): Int = (this.minutes * 60) + this.seconds

    fun asString(): String {
        val bidiFormatter = BidiFormatter.getInstance()
        val locale = Locale.getDefault()

        val minutes = String.format(locale, "%02d", this.minutes)
        val seconds = String.format(locale, "%02d", this.seconds)

        val time = bidiFormatter.unicodeWrap("$minutes : $seconds")

        return time
    }
}

fun Int.toTime(): Time = Time(this / 60, this % 60)