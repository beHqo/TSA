package com.example.android.strikingarts.database.dao

import androidx.room.*
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.utils.DEFENSE
import com.example.android.strikingarts.utils.OFFENSE
import kotlinx.coroutines.flow.Flow

@Dao
interface TechniqueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(technique: Technique): Long

    @Insert
    suspend fun insertTechniques(vararg technique: Technique): List<Long>

    @Update
    suspend fun update(technique: Technique) // maybe needs work

    @Query("SELECT * FROM technique_table WHERE techniqueId = :techniqueId")
    suspend fun getTechnique(techniqueId: Long): Technique?

    @Query("SELECT * FROM technique_table ORDER BY techniqueId ASC")
    fun getTechniqueList(): Flow<List<Technique>> //testing flow

    @Query("SELECT * FROM technique_table WHERE movement_type = :movementType")
    fun getTechniqueByMovement(movementType: String): Flow<List<Technique>>

    // Return type of the function above needs to change according to compose!
    fun getStrikes() = getTechniqueByMovement(OFFENSE)
    fun getDefenses() = getTechniqueByMovement(DEFENSE)

    @Query("SELECT * FROM technique_table WHERE technique_type = :techniqueType")
    fun getTechniqueByType(techniqueType: String): Flow<List<Technique>>

    @Query("DELETE FROM technique_table WHERE techniqueId = :techniqueId")
    suspend fun removeTechnique(techniqueId: Long)

    @Query("DELETE FROM technique_table")
    suspend fun removeAllTechniques()
}