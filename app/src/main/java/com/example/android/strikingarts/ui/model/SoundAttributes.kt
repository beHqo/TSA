package com.example.android.strikingarts.ui.model

import android.os.Parcel
import android.os.Parcelable

data class SoundAttributes(val name: String = "", val size: Int = 0, val durationMilli: Long = 0) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(size)
        parcel.writeLong(durationMilli)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SoundAttributes> {
        override fun createFromParcel(parcel: Parcel): SoundAttributes {
            return SoundAttributes(parcel)
        }

        override fun newArray(size: Int): Array<SoundAttributes?> {
            return arrayOfNulls(size)
        }
    }
}