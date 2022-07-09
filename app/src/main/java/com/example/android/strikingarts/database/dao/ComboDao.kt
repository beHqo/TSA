package com.example.android.strikingarts.database.dao

import androidx.room.*
import com.example.android.strikingarts.database.entity.Combo
import com.example.android.strikingarts.database.entity.ComboTechniqueCrossRef
import com.example.android.strikingarts.database.entity.ComboWithTechniques
import com.example.android.strikingarts.database.entity.Technique
import kotlinx.coroutines.flow.Flow

@Dao
interface ComboDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCombo(combo: Combo): Long

    @Insert
    suspend fun insertCombos(vararg combo: Combo): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertComboTechniqueCrossRef(join: ComboTechniqueCrossRef): Long

    @Update
    suspend fun updateCombo(combo: Combo) // maybe needs work

    @Transaction //Is it necessary?
    @Query("SELECT * FROM combo_table WHERE comboId = :comboId")
    suspend fun getCombo(comboId: Long): ComboWithTechniques

    @Transaction //Is it necessary?
    @Query("SELECT * FROM combo_table")
    fun getComboList() : Flow<List<ComboWithTechniques>>

    @Query("DELETE FROM combo_table WHERE comboId = :comboId")
    suspend fun removeCombo(comboId: Long)

    @Query("DELETE FROM combo_table")
    suspend fun removeAllCombos()
}