package com.example.android.strikingarts.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.strikingarts.R
import com.example.android.strikingarts.database.entity.TechniqueType.*

@Entity(tableName = "technique_table")
data class Technique(
    @PrimaryKey(autoGenerate = true)
    val techniqueId: Long = 0L,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "number")
    val num: String = "",

    @ColumnInfo(name = "faint")
    val canBeFaint: Boolean = false, // Needs Work

    @ColumnInfo(name = "body_shot")
    val canBeBodyshot: Boolean = false, // Needs Work!

    @ColumnInfo(name = "sound")
    val sound: Int = R.raw.shoombool, // To Be Changed Once I figure out how to implement sounds!

    @ColumnInfo(name = "color")
    val color: String = "18446744069414584320",

    @ColumnInfo(name = "technique_type")
    val techniqueType: TechniqueType = NONE,

    @ColumnInfo(name = "movement_type")
    val movementType: MovementType = MovementType.NONE
)