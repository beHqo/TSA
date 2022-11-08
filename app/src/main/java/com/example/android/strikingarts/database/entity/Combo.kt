package com.example.android.strikingarts.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "combo_table")
data class Combo(
    @PrimaryKey(autoGenerate = true) val comboId: Long = 0L,

    @ColumnInfo(name = "combo_name") val name: String = "",

    @ColumnInfo(name = "combo_description") val description: String = "",

    @ColumnInfo(name = "delay_after_combo") val delay: Float = 1F
)