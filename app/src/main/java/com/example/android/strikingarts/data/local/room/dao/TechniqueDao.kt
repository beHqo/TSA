package com.example.android.strikingarts.data.local.room.dao

import androidx.room.*
import com.example.android.strikingarts.data.local.room.model.Technique
import kotlinx.coroutines.flow.Flow

@Dao
interface TechniqueDao {
    @Query("SELECT * FROM technique_table WHERE techniqueId = :techniqueId")
    suspend fun getTechnique(techniqueId: Long): Technique?

    @Query("SELECT * FROM technique_table ORDER BY techniqueId ASC")
    fun getTechniqueList(): Flow<List<Technique>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(technique: Technique): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg technique: Technique): List<Long>

    @Update
    suspend fun update(technique: Technique): Int


    @Query("DELETE FROM technique_table WHERE techniqueId = :techniqueId")
    suspend fun delete(techniqueId: Long): Int

    @Query("DELETE FROM technique_table WHERE techniqueId IN (:idList)")
    suspend fun deleteAll(idList: List<Long>): Int
}