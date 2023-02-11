package com.example.android.strikingarts.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(primaryKeys = ["refId", "comboId", "techniqueId"])
@Entity
data class ComboTechniqueCrossRef(
    @PrimaryKey(autoGenerate = true) val refId: Long = 0, val comboId: Long, val techniqueId: Long
)
//@Entity(primaryKeys = ["comboId, techniqueId"])
////@Entity(tableName = "combo_technique_ref")
//data class ComboTechniqueCrossRef(
////    @PrimaryKey(autoGenerate = true) val refId: Long = 0,
//    val comboId: Long, val techniqueId: Long
//)