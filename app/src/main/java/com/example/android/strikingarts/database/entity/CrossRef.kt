package com.example.android.strikingarts.database.entity

import androidx.room.Entity

@Entity(primaryKeys = ["comboId", "techniqueId"])
data class ComboTechniqueCrossRef(val comboId: Long, val techniqueId: Long)