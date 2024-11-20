package com.github.tsa.domain.technique

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import javax.inject.Inject

private const val TAG = "DeleteTechniqueUseCase"

class DeleteTechniqueUseCase @Inject constructor(private val repository: TechniqueCacheRepository) {
    suspend operator fun invoke(id: Long): Long = try {
        repository.delete(id)
    } catch (e: SQLiteConstraintException) {
        Log.e(TAG, "invoke: technique is in use", e)

        0
    }

    suspend operator fun invoke(idList: List<Long>): Long = try {
        repository.deleteAll(idList)
    } catch (e: SQLiteConstraintException) {
        Log.e(TAG, "invoke: One or more techniques are in use", e)

        0
    }
}