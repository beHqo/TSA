package com.example.android.strikingarts.data.local.room.typeconverters

import androidx.room.TypeConverter
import com.example.android.strikingarts.data.local.room.model.DataAssetAudioAttributes
import com.example.android.strikingarts.data.local.room.model.DataAudioAttributes
import com.example.android.strikingarts.data.local.room.model.DataUriAudioAttributes

class DataAudioAttributesTypeConverter {
    @TypeConverter
    fun fromAudioAttributesToString(audioAttributes: DataAudioAttributes): String =
        if (audioAttributes is DataUriAudioAttributes) fromUriAudioAttributesToString(
            audioAttributes
        )
        else fromAssetAudioAttributesToString(audioAttributes as DataAssetAudioAttributes)

    @TypeConverter
    fun fromStringToAudioAttributes(string: String): DataAudioAttributes {
        val fields = string.split(", ")

        return if (fields.size > 3) DataUriAudioAttributes(
            fields[0], fields[1], fields[2].toLong(), fields[3].toInt()
        ) else DataAssetAudioAttributes(fields[0], fields[1], fields[2].toLong())
    }

    private fun fromUriAudioAttributesToString(uriAudioAttributes: DataUriAudioAttributes): String =
        "${uriAudioAttributes.name}, ${uriAudioAttributes.audioString}, ${uriAudioAttributes.durationMillis}, ${uriAudioAttributes.sizeByte}"

    private fun fromAssetAudioAttributesToString(assetAudioAttributes: DataAssetAudioAttributes): String =
        "${assetAudioAttributes.name}, ${assetAudioAttributes.audioString}, ${assetAudioAttributes.durationMillis}"
}