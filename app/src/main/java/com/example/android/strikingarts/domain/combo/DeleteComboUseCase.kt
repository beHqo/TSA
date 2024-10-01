package com.example.android.strikingarts.domain.combo

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import javax.inject.Inject

private const val TAG = "DeleteComboUseCase"

class DeleteComboUseCase @Inject constructor(private val repository: ComboCacheRepository) {
    suspend operator fun invoke(id: Long): Long = try {
        repository.delete(id)
    } catch (e: SQLiteConstraintException) {
        Log.e(TAG, "invoke: Combo is in use", e)

        0
    }

    suspend operator fun invoke(idList: List<Long>): Long = try {
        repository.deleteAll(idList)
    } catch (e: SQLiteConstraintException) {
        Log.e(TAG, "invoke: Combo is in use", e)

        0
    }
}