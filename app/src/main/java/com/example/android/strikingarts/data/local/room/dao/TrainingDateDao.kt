package com.example.android.strikingarts.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.android.strikingarts.data.local.room.model.TrainingDate
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingDateDao {
    @Query("SELECT * FROM training_day_table")
    fun getTrainingDateList(): Flow<List<TrainingDate>>

    @Query("SELECT * FROM training_day_table WHERE epoch_date BETWEEN :fromEpochDate AND :toEpochDate")
    fun getTrainingDateInRange(fromEpochDate: Long, toEpochDate: Long): Flow<List<TrainingDate>>

    @Query("SELECT epoch_date FROM training_day_table WHERE epoch_date BETWEEN :fromEpochDate AND :toEpochDate")
    suspend fun getEpochDaysOfTrainingDatesInRange(
        fromEpochDate: Long,
        toEpochDate: Long
    ): List<Long>
    
    @Query("SELECT * FROM training_day_table WHERE epoch_date = :epochDate")
    suspend fun getTrainingDate(epochDate: Long): TrainingDate?

    @Query("SELECT * FROM training_day_table ORDER BY epoch_date DESC LIMIT 1")
    suspend fun getLastTrainingDate(): TrainingDate?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trainingDate: TrainingDate): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg trainingDate: TrainingDate): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(trainingDateList: List<TrainingDate>): List<Long>

    @Update
    suspend fun update(trainingDate: TrainingDate): Int

    @Query("DELETE FROM training_day_table WHERE epoch_date = :epochDate")
    suspend fun delete(epochDate: Long): Int

    @Query("DELETE FROM training_day_table WHERE epoch_date IN (:idList)")
    suspend fun deleteAll(idList: List<Long>): Int
}