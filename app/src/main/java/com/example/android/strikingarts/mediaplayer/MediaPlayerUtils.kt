package com.example.android.strikingarts.mediaplayer

import android.content.Context
import android.content.res.AssetManager
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import com.example.android.strikingarts.mediaplayer.PlayerConstants.ASSET_PATH_PREFIX
import com.example.android.strikingarts.mediaplayer.PlayerConstants.SILENCE_WAV
import java.io.IOException

private const val TAG = "MediaPlayerUtils"

internal fun MediaPlayer.setAudioSource(
    context: Context, assetManager: AssetManager, soundString: String
) = setAudioSourceAndCatchExceptions(assetManager) {
    Log.d(TAG, "setAudioSource: $soundString")

    when {
        soundString.isUriString() -> setAudioSource(context, Uri.parse(soundString))
        soundString.isEmpty() -> setAudioSource(assetManager, SILENCE_WAV)
        else -> setAudioSource(assetManager, soundString)
    }
}

internal fun MediaPlayer.setAudioSource(context: Context, uri: Uri) = run {
    reset()
    setDataSource(context, uri)
    prepareAsync()
}

internal fun MediaPlayer.setAudioSource(assetManager: AssetManager, fileName: String) {
    val afd = try {
        assetManager.openFd("$ASSET_PATH_PREFIX$fileName")
    } catch (e: IOException) {
        Log.e(
            TAG,
            "setAudioSource: Failed to open file with the given name: $fileName\nPlaying Silence",
            e
        )
        assetManager.openFd("$ASSET_PATH_PREFIX$SILENCE_WAV")
    }

    run {
        reset()
        setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        prepareAsync()
    }

    afd.close()
}

private fun MediaPlayer.setAudioSourceAndCatchExceptions(
    assetManager: AssetManager, setAudioSource: () -> Unit
) = try {
    setAudioSource()
} catch (e: IllegalStateException) {
    Log.e(TAG, "setAudioSource: Method was called in the incorrect MediaPlayer state", e)
} catch (e: IllegalArgumentException) {
    Log.e(TAG, "setAudioSource: Provided data source is invalid\nPlaying Silence", e)
    setAudioSource(assetManager, SILENCE_WAV)
} catch (e: IOException) {
    Log.e(TAG, "setAudioSource: Provided data source cannot be read\nPlaying Silence", e)
    setAudioSource(assetManager, SILENCE_WAV)
} catch (e: SecurityException) {
    Log.e(
        TAG,
        "setAudioSource: Required permissions to play the audio file were not given\nPlaying Silence",
        e
    )
    setAudioSource(assetManager, SILENCE_WAV)
}

internal fun String.isUriString() = startsWith("content", true)
