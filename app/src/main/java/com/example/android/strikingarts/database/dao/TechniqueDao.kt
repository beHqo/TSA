package com.example.android.strikingarts.database.dao

import androidx.room.*
import com.example.android.strikingarts.database.entity.Technique
import kotlinx.coroutines.flow.Flow

@Dao
interface TechniqueDao {
    @Query("SELECT * FROM technique_table WHERE techniqueId = :techniqueId")
    suspend fun getTechnique(techniqueId: Long): Technique?

    @Query("SELECT * FROM technique_table ORDER BY techniqueId ASC")
    fun getTechniqueList(): Flow<List<Technique>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(technique: Technique): Long

    @Insert
    suspend fun insertTechniques(vararg technique: Technique): List<Long>

    @Update
    suspend fun update(technique: Technique)

    @Query("DELETE FROM technique_table WHERE techniqueId = :techniqueId")
    suspend fun deleteTechnique(techniqueId: Long)

    @Query("DELETE FROM technique_table WHERE techniqueId IN (:idList)")
    suspend fun deleteTechniques(idList: List<Long>)
}