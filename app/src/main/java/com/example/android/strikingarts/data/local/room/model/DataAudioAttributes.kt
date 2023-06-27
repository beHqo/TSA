package com.example.android.strikingarts.data.local.room.model

interface DataAudioAttributes {
    val name: String
    val audioString: String
    val durationMilli: Long
}

data class DataUriAudioAttributes(
    override val name: String = "",
    override val audioString: String = "",
    override val durationMilli: Long = 0,
    val sizeByte: Int = 0
) : DataAudioAttributes

data class DataAssetAudioAttributes(
    override val name: String = "",
    override val audioString: String = "",
    override val durationMilli: Long = 0
) : DataAudioAttributes