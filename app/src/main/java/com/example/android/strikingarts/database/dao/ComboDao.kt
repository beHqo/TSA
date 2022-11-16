package com.example.android.strikingarts.database.dao

import androidx.room.*
import com.example.android.strikingarts.database.entity.Combo
import com.example.android.strikingarts.database.entity.ComboTechniqueCrossRef
import com.example.android.strikingarts.database.entity.ComboWithTechniques
import kotlinx.coroutines.flow.Flow

@Dao
interface ComboDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCombo(combo: Combo): Long

    @Insert
    suspend fun insertCombos(vararg combo: Combo): List<Long>

    @Update
    suspend fun updateCombo(combo: Combo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComboTechniqueCrossRef(join: ComboTechniqueCrossRef): Long

    @Query("DELETE FROM ComboTechniqueCrossRef WHERE comboId = :comboId")
    suspend fun deleteComboTechniqueCrossRef(comboId: Long)

    @Transaction
    @Query("SELECT * FROM combo_table WHERE comboId = :comboId")
    suspend fun getCombo(comboId: Long): ComboWithTechniques?

    @Transaction
    @Query("SELECT * FROM combo_table")
    fun getComboList(): Flow<List<ComboWithTechniques>>

    @Query("DELETE FROM combo_table WHERE comboId = :comboId")
    suspend fun deleteCombo(comboId: Long)

    @Query("DELETE FROM combo_table WHERE comboId IN (:idList)")
    suspend fun deleteCombos(idList: List<Long>)
}