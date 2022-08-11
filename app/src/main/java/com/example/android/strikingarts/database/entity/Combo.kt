package com.example.android.strikingarts.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "combo_table")
data class Combo(
    @PrimaryKey(autoGenerate = true)
    var comboId: Long = 0L,

    @ColumnInfo(name = "combo_name")
    var name: String = "",

    @ColumnInfo(name = "combo_description")
    var description: String = "",

    @ColumnInfo(name = "delay_after_combo")
    var delay: Long = 3000L
)