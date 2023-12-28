package com.example.android.strikingarts.data.local.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "technique_table")
data class Technique(
    @PrimaryKey(autoGenerate = true) val techniqueId: Long = 0L,

    @ColumnInfo(name = "name") val name: String = "",

    @ColumnInfo(name = "number") val num: String = "",

    @ColumnInfo(name = "audio_attributes") val audioAttributes: DataAudioAttributes? = null,

    @ColumnInfo(name = "color") val color: String = "0",

    @ColumnInfo(name = "technique_type") val techniqueType: String = "",

    @ColumnInfo(name = "movement_type") val movementType: String = ""
)