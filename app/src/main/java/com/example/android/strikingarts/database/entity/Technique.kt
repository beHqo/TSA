package com.example.android.strikingarts.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.strikingarts.R
import com.example.android.strikingarts.database.entity.TechniqueType.*

@Entity(tableName = "technique_table")
data class Technique(
    @PrimaryKey(autoGenerate = true)
    var techniqueId: Long = 0L,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "number")
    var num: String = "",

    @ColumnInfo(name = "faint")
    var canBeFaint: Boolean = false, // Needs Work

    @ColumnInfo(name = "body_shot")
    var canBeBodyshot: Boolean = false, // Needs Work!

    @ColumnInfo(name = "sound")
    var sound: Int = R.raw.shoombool, // To Be Changed Once I figure out how to implement sounds!

    @ColumnInfo(name = "color")
    var color: String = "18446744069414584320",

    @ColumnInfo(name = "technique_type")
    var techniqueType: TechniqueType = NONE,

    @ColumnInfo(name = "movement_type")
    var movementType: MovementType = when (techniqueType) {
        PUNCH, KICK, ELBOW, KNEE -> MovementType.Offense
        HAND_BLOCK, SHIN_BLOCK, HEAD_MOVEMENT, FOOTWORK -> MovementType.Defense
        else -> MovementType.NONE
    }
)