package com.example.android.strikingarts.data.local.room.typeconverters

import androidx.room.TypeConverter

class WorkoutIdListTypeConverter {
    @TypeConverter
    fun fromListToString(idList: List<Long>): String = idList.joinToString()

    @TypeConverter
    fun fromStringToIdList(string: String): List<Long> =
        string.split(", ").map { it.toLongOrNull() ?: 0L }
}