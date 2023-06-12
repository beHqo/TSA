package com.example.android.strikingarts.data.local.resolver

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.example.android.strikingarts.domain.interfaces.SoundAttributesRetriever
import com.example.android.strikingarts.ui.model.SoundAttributes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val TAG = "SoundAttributeRetriever"

class SoundAttributesRetrieverImpl @Inject constructor(@ApplicationContext private val context: Context) :
    SoundAttributesRetriever {
    private val resolver = context.contentResolver

    override fun getSoundAttributes(uri: Uri): SoundAttributes {
        Log.d(TAG, "getSoundAttributes: Given uri: $uri")

        resolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

        var displayName = ""
        var size = 0
        val length = getAudioDuration(uri)

        val displayNameColumnName = OpenableColumns.DISPLAY_NAME
        val sizeColumnName = OpenableColumns.SIZE

        val projection = arrayOf(displayNameColumnName, sizeColumnName)

        val cursor = resolver.query(uri, projection, null, null, null)

        if (cursor == null) {
            val exception =
                IllegalArgumentException("Failed to retrieve a cursor based on the given uri: $uri")
            Log.e(TAG, "cursor is null", exception)
            throw exception
        }

        cursor.use {
            if (it.moveToFirst()) {
                val nameColumnIndex = getColumnIndex(it, displayNameColumnName)
                val sizeColumnIndex = getColumnIndex(it, sizeColumnName)

                displayName = it.getString(nameColumnIndex)
                size = it.getInt(sizeColumnIndex)
            }
        }

        return SoundAttributes(displayName, size, length)
    }

    private fun getAudioDuration(uri: Uri): Long {
        var length = ""

        val metadataRetriever = MediaMetadataRetriever()

        try {
            metadataRetriever.setDataSource(context, uri)
        } catch (e: Exception) {
            Log.e(TAG, "getAudioDuration: Failed to setDataSource on the given uri", e)
        }

        metadataRetriever.use {
            length = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                ?: ""

            if (length == "") Log.e(
                TAG, "getAudioDuration: Failed to retrieve audio duration from the given uri: $uri"
            )
        }

        return if (length.isEmpty()) 0L else length.toLong()
    }

    private fun getColumnIndex(cursor: Cursor, columnName: String): Int {
        return try {
            cursor.getColumnIndexOrThrow(columnName)
        } catch (e: IllegalArgumentException) {
            Log.e(
                TAG,
                "getColumnIndex: Could not find the column Index containing the column: $columnName",
                e
            )
        }
    }
}