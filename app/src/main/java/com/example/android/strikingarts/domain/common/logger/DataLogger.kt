package com.example.android.strikingarts.domain.common.logger

import android.util.Log

class DataLogger(private val tag: String) {
    fun logRetrieveOperation(id: Long, functionName: String) {
        Log.e(tag, "$functionName: Failed to retrieve the object with the id of $id")
    }

    fun logRetrieveMultipleItemsOperation(idList: List<Long>, functionName: String) {
        Log.e(
            tag,
            "$functionName: Failed to find any items that match the given list of ids\nidList: ${idList.joinToString()}"
        )
    }

    fun logInsertOperation(id: Long, obj: Any) {
        if (id == -1L) Log.e(tag, "insert: Operation failed.\nGiven object: $obj")
        else Log.d(tag, "insert: Operation succeeded. Id: $id")
    }

    fun logUpdateOperation(affectedRows: Int, id: Long, obj: Any) {
        if (affectedRows == 0) Log.e(tag, "update: Operation failed.\nGiven object: $obj")
        else Log.d(tag, "update: Operation succeeded. Given Id: $id")
    }

    fun logDeleteOperation(affectedRows: Int, id: Long) {
        if (affectedRows == 0) Log.e(tag, "delete: Operation Failed.\nGiven id: $id")
        else Log.d(tag, "delete: Operation Succeeded. Given Id: $id")
    }

    fun logDeleteAllOperation(affectedRows: Int, idList: List<Long>) {
        if (affectedRows == 0) Log.e(
            tag, "deleteAll: Operation Failed.\nGiven idList: ${idList.joinToString()}"
        )
        else if (affectedRows < idList.size) Log.e(
            tag,
            "deleteAll: Failed to delete some of the objects.\nidList: ${idList.joinToString()}"
        )
        else Log.d(tag, "deleteAll: Operation Succeeded.\nidList: ${idList.joinToString()}")
    }
}