package com.example.android.strikingarts.database.dao

import androidx.room.*
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.utils.TechniqueCategory.DEFENSE
import com.example.android.strikingarts.utils.TechniqueCategory.OFFENSE
import kotlinx.coroutines.flow.Flow

@Dao
interface TechniqueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(technique: Technique): Long

    @Insert
    suspend fun insertTechniques(vararg technique: Technique): List<Long>

    @Update
    suspend fun update(technique: Technique)

    @Query("DELETE FROM technique_table WHERE techniqueId IN (:idList)")
    suspend fun deleteTechniques(idList: List<Long>)

    @Query("SELECT * FROM technique_table WHERE techniqueId = :techniqueId")
    suspend fun getTechnique(techniqueId: Long): Technique?

    @Query("SELECT * FROM technique_table ORDER BY techniqueId ASC")
    fun getTechniqueList(): Flow<List<Technique>>

    @Query("SELECT * FROM technique_table WHERE movement_type = :movementType")
    fun getTechniqueByMovement(movementType: String): Flow<List<Technique>>

    fun getStrikes() = getTechniqueByMovement(OFFENSE)
    fun getDefenses() = getTechniqueByMovement(DEFENSE)

    @Query("SELECT * FROM technique_table WHERE technique_type = :techniqueType")
    fun getTechniqueByType(techniqueType: String): Flow<List<Technique>>

    @Query("DELETE FROM technique_table WHERE techniqueId = :techniqueId")
    suspend fun removeTechnique(techniqueId: Long)
}