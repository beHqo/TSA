package com.example.android.strikingarts.ui.model

import android.os.Parcel
import android.os.Parcelable
import kotlin.math.abs

data class Time(val minutes: Int = 0, val seconds: Int = 0) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(), parcel.readInt()
    ) {
    }

    fun toMillieSeconds(): Long {
        return ((this.minutes * 60000) + (this.seconds * 1000)).toLong()
    }

    fun toSeconds(): Int {
        return (this.minutes * 60) + this.seconds
    }

    fun asString(): String {
        val minutes = if (abs(this.minutes) < 10) "0${this.minutes}" else "$minutes"
        val seconds = if (abs(this.seconds) < 10) "0${this.seconds}" else "$seconds"

        return "$minutes : $seconds"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(minutes)
        parcel.writeInt(seconds)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Time> {
        override fun createFromParcel(parcel: Parcel): Time {
            return Time(parcel)
        }

        override fun newArray(size: Int): Array<Time?> {
            return arrayOfNulls(size)
        }
    }
}

fun Long.toTime(): Time {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds - (minutes * 60)

    return Time(minutes.toInt(), seconds.toInt())
}