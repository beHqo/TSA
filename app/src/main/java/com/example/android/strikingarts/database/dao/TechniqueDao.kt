package com.example.android.strikingarts.database.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.android.strikingarts.database.entity.*
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
    suspend fun getTechnique(techniqueId: Long) : Technique?

    @Query("SELECT * FROM technique_table ORDER BY techniqueId ASC")
    fun getTechniqueList() : Flow<List<Technique>> //testing flow

    @Query("SELECT * FROM technique_table WHERE movement_type = :movementType")
    fun getTechniqueByMovement(movementType: MovementType) : Flow<List<Technique>>

    // Return type of the function above needs to change according to compose!
    fun getStrikes() = getTechniqueByMovement(MovementType.Offense)
    fun getDefenses() = getTechniqueByMovement(MovementType.Defense)

    @Query("SELECT * FROM technique_table WHERE technique_type = :techniqueType")
    fun getTechniqueByType(techniqueType: TechniqueType) : Flow<List<Technique>>

    @Query("DELETE FROM technique_table WHERE techniqueId = :techniqueId")
    suspend fun removeTechnique(techniqueId: Long)

    @Query("DELETE FROM technique_table")
    suspend fun removeAllTechniques()
}