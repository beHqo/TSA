package com.example.android.strikingarts.data.local.resolver

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.example.android.strikingarts.domain.audioattributes.AudioAttributesRetriever
import com.example.android.strikingarts.domain.model.AudioAttributes
import com.example.android.strikingarts.domain.model.ResourceAudioAttributes
import com.example.android.strikingarts.domain.model.UriAudioAttributes
import com.example.android.strikingarts.domainandroid.audioplayers.mediaplayer.isUriString
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

private const val TAG = "SoundAttributeRetriever"

class AudioAttributesRetrieverImpl @Inject constructor(@ApplicationContext private val context: Context) :
    AudioAttributesRetriever {
    private val resolver = context.contentResolver

    override fun getAudioAttributes(audioString: String): AudioAttributes {
        val isUri = audioString.isUriString()

        return if (isUri) {
            Log.d(TAG, "getFunctionNameLOL: Given uri:\n$audioString")
            getUriAudioAttributes(Uri.parse(audioString))
        } else {
            Log.d(TAG, "getFunctionNameLOL: Given asset file path:\n$audioString")
            getResourceAudioAttributes(audioString)
        }
    }

    private fun getUriAudioAttributes(uri: Uri): UriAudioAttributes {
        resolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

        var displayName = ""
        var size = 0
        val length = getUriAudioDuration(uri)

        val displayNameColumnName = OpenableColumns.DISPLAY_NAME
        val sizeColumnName = OpenableColumns.SIZE

        val projection = arrayOf(displayNameColumnName, sizeColumnName)

        val cursor = resolver.query(uri, projection, null, null, null)

        if (cursor == null) {
            Log.e(TAG, "cursor is null"); return UriAudioAttributes()
        }

        cursor.use {
            if (it.moveToFirst()) {
                val nameColumnIndex = getColumnIndex(it, displayNameColumnName)
                val sizeColumnIndex = getColumnIndex(it, sizeColumnName)

                displayName = it.getString(nameColumnIndex)
                size = it.getInt(sizeColumnIndex)
            }
        }

        return UriAudioAttributes(0, displayName, uri.toString(), length, size)
    }

    private fun getUriAudioDuration(uri: Uri): Long {
        var length: String

        val metadataRetriever = MediaMetadataRetriever()

        try {
            metadataRetriever.setDataSource(context, uri)
        } catch (e: Exception) {
            Log.e(TAG, "getAudioDuration: Failed to setDataSource on the given uri:\n$uri", e)
            return 0
        }

        metadataRetriever.use {
            length = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                ?: ""

            if (length == "") Log.e(
                TAG, "getAudioDuration: Failed to retrieve audio duration from the given uri:\n$uri"
            )
        }

        return if (length.isEmpty()) 0L else length.toLong()
    }

    private fun getResourceAudioAttributes(filePath: String): ResourceAudioAttributes {
        val fileName = filePath.retrieveFileNameFromAssetFilePath()

        var length: String

        val metadataRetriever = MediaMetadataRetriever()
        val assetManager = context.assets

        val afd = try {
            assetManager.openFd(filePath)
        } catch (e: IOException) {
            Log.e(
                TAG,
                "getResourceAudioAttributes: Failed to open the given asset file path:\n$filePath",
                e
            )
            return ResourceAudioAttributes(0, fileName, filePath, 0L)
        }

        try {
            metadataRetriever.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        } catch (e: Exception) {
            Log.e(
                TAG,
                "getAudioDuration: Failed to setDataSource on the given asset file path:\n$filePath",
                e
            )
            return ResourceAudioAttributes(0, fileName, filePath, 0L)
        }

        metadataRetriever.use {
            length = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                ?: ""

            if (length == "") Log.e(
                TAG,
                "getAudioDuration: Failed to retrieve audio duration from the given asset file path:\n$filePath"
            )
        }

        return ResourceAudioAttributes(
            0, fileName, filePath, if (length.isEmpty()) 0L else length.toLong()
        )
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

private fun String.retrieveFileNameFromAssetFilePath(): String = this.substringAfterLast('/')