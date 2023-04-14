package com.example.android.strikingarts.data.local.room.dao

import android.util.Log
import androidx.room.*
import com.example.android.strikingarts.data.local.room.model.Combo
import com.example.android.strikingarts.data.local.room.model.ComboTechniqueCrossRef
import com.example.android.strikingarts.data.local.room.model.ComboWithTechniques
import kotlinx.coroutines.flow.Flow

private const val TAG = "ComboDao"

@Dao
interface ComboDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(combo: Combo): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg combo: Combo): List<Long>

    @Update
    suspend fun update(combo: Combo): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertComboTechniqueCrossRef(join: ComboTechniqueCrossRef): Long

    @Query("DELETE FROM combo_technique_ref WHERE comboId = :comboId")
    suspend fun deleteComboTechniqueCrossRef(comboId: Long): Int

    @Transaction
    suspend fun insertComboWithTechniques(combo: Combo, techniqueIdList: List<Long>) {
        var count = 0

        val comboId = insert(combo)

        if (comboId == -1L) {
            Log.e(
                TAG,
                "insertComboWithTechniques: Failed to insert the combo, Stopping all of the remaining operations."
            )

            return
        } else Log.d(TAG, "insertComboWithTechniques: Combo was inserted successfully.")

        techniqueIdList.forEach { techniqueId ->
            count++

            val refId = insertComboTechniqueCrossRef(ComboTechniqueCrossRef(comboId, techniqueId))

            if (refId == -1L) Log.e(
                TAG,
                "insertComboWithTechniques: Operation failed. comboId=$comboId, techniqueId=$techniqueId, $count/${techniqueIdList.size}"
            )
            else Log.d(
                TAG,
                "insertComboWithTechniques: Operation succeeded. refId=$refId, $count/${techniqueIdList.size}"
            )
        }
    }

    @Transaction
    suspend fun updateComboWithTechniques(combo: Combo, techniqueIdList: List<Long>) {
        var count = 0

        val comboId = combo.comboId

        val updatedRows = update(combo)

        if (updatedRows == 0) {
            Log.e(
                TAG,
                "updateComboWithTechniques: Failed to update the combo, stopping all of the remaining operations."
            )

            return
        } else Log.d(
            TAG, "updateComboWithTechniques: Combo was updated successfully."
        )

        val deletedRows = deleteComboTechniqueCrossRef(comboId)

        Log.d(
            TAG, "updateComboWithTechniques: number of deleted cross-ref objects: $deletedRows"
        )

        techniqueIdList.forEach { techniqueId ->
            count++

            val refId = insertComboTechniqueCrossRef(ComboTechniqueCrossRef(comboId, techniqueId))

            if (refId == -1L) Log.e(
                TAG,
                "updateComboWithTechniques: Operation failed. comboId=$comboId, techniqueId=$techniqueId, $count/${techniqueIdList.size}"
            )
            else Log.d(
                TAG,
                "updateComboWithTechniques: Operation succeeded. refId=$refId, $count/${techniqueIdList.size}"
            )
        }
    }

    @Transaction
    @Query("SELECT * FROM combo_table WHERE comboId = :comboId")
    suspend fun getCombo(comboId: Long): ComboWithTechniques?

    @Transaction
    @Query("SELECT * FROM combo_table")
    fun getComboList(): Flow<List<ComboWithTechniques>>

    @Query("DELETE FROM combo_table WHERE comboId = :comboId")
    suspend fun delete(comboId: Long): Int

    @Query("DELETE FROM combo_table WHERE comboId IN (:idList)")
    suspend fun deleteAll(idList: List<Long>): Int
}