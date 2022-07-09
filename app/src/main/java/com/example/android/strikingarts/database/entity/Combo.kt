package com.example.android.strikingarts.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "combo_table")
data class Combo(
    @PrimaryKey(autoGenerate = true)
    var comboId: Long = 0L,

    @ColumnInfo(name = "combo_name")
    var name: String = "combo $comboId",

    @ColumnInfo(name = "combo_description")
    var description: String = "Enter a simple description regarding this combo",
)