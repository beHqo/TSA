package com.example.android.strikingarts.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.android.strikingarts.data.local.room.model.TrainingDate
import com.example.android.strikingarts.data.local.room.model.TrainingDate.Companion.PRIMARY_KEY_COLUMN_NAME
import com.example.android.strikingarts.data.local.room.model.TrainingDate.Companion.TRAINING_DATE_TABLE_NAME
import com.example.android.strikingarts.data.local.room.model.TrainingDateWithWorkoutConclusions

@Dao
interface TrainingDateDao {
    @Transaction
    @Query("SELECT * FROM $TRAINING_DATE_TABLE_NAME WHERE $PRIMARY_KEY_COLUMN_NAME BETWEEN :fromEpochDate AND :toEpochDate")
    suspend fun getTrainingDatesWithWorkoutConclusionsInRange(
        fromEpochDate: Long, toEpochDate: Long
    ): List<TrainingDateWithWorkoutConclusions>

    @Transaction
    @Query("SELECT * FROM $TRAINING_DATE_TABLE_NAME WHERE $PRIMARY_KEY_COLUMN_NAME = :epochDate")
    suspend fun getTrainingDateWithWorkoutConclusions(epochDate: Long): TrainingDateWithWorkoutConclusions?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trainingDate: TrainingDate): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg trainingDate: TrainingDate): List<Long>
}