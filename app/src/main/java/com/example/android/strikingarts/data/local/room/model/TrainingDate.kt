package com.example.android.strikingarts.data.local.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.strikingarts.data.local.room.model.TrainingDate.Companion.TRAINING_DATE_TABLE_NAME


@Entity(tableName = TRAINING_DATE_TABLE_NAME)
data class TrainingDate(
    @PrimaryKey(autoGenerate = false) val epochDay: Long = 0L
) {
    companion object {
        const val TRAINING_DATE_TABLE_NAME = "training_date_table"
        const val PRIMARY_KEY_COLUMN_NAME = "epochDay"
    }
}