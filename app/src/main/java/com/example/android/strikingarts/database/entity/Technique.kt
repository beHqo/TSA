package com.example.android.strikingarts.database.entity

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.strikingarts.R

@Entity(tableName = "technique_table")
data class Technique(
    @PrimaryKey(autoGenerate = true) val techniqueId: Long = 0L,

    @ColumnInfo(name = "name") val name: String = "",

    @ColumnInfo(name = "number") val num: String = "",

    @ColumnInfo(name = "faint") val canBeFaint: Boolean = false, // Needs Work

    @ColumnInfo(name = "body_shot") val canBeBodyshot: Boolean = false, // Needs Work!

    @ColumnInfo(name = "sound") val sound: Int = R.raw.shoombool, // To Be Changed Once I figure out how to implement sounds!

    @ColumnInfo(name = "color") val color: String = Color.Transparent.value.toString(),

    @ColumnInfo(name = "technique_type") val techniqueType: String = "",

    @ColumnInfo(name = "movement_type") val movementType: String = ""
)