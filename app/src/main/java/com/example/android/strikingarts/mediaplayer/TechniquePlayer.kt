package com.example.android.strikingarts.mediaplayer

import android.content.Context
import android.media.SoundPool
import android.net.Uri
import android.util.Log
import com.example.android.strikingarts.hilt.di.DefaultDispatcher
import com.example.android.strikingarts.hilt.di.IoDispatcher
import com.example.android.strikingarts.mediaplayer.PlayerConstants.ASSET_PATH_PREFIX
import com.example.android.strikingarts.mediaplayer.PlayerConstants.SILENCE_WAV
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject

private const val TAG = "TechniquePlayer"

class TechniquePlayer @Inject constructor(
    @ApplicationContext private val context: Context,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val soundPool = SoundPool.Builder().setMaxStreams(1).build()
    private val assetManager = context.assets

    private suspend fun loadUri(uriString: String): Int = withContext(ioDispatcher) {
        val uri = Uri.parse(uriString)
        var id: Int

        val afd = try {
            context.contentResolver.openAssetFileDescriptor(uri, "r")
        } catch (e: FileNotFoundException) {
            Log.e(TAG, "loadUri: Failed to open file with the given uri\n$uriString", e)
            assetManager.openFd("$ASSET_PATH_PREFIX$SILENCE_WAV")
        }

        if (afd == null) {
            Log.e(TAG, "loadUri: Provider has Crashed")
            return@withContext 0
        }

        afd.use {
            val soundId = soundPool.load(afd.fileDescriptor, afd.startOffset, afd.length, 1)

            Log.d(TAG, "loadUri: soundId=$soundId\nuri=$uriString")

            id = soundId
        }

        return@withContext id
    }

    private suspend fun loadAssetFile(fileName: String): Int = withContext(ioDispatcher) {
        val afd = try {
            assetManager.openFd("$ASSET_PATH_PREFIX$fileName")
        } catch (e: IOException) {
            Log.e(TAG, "setAudioSource: Failed to open file with the given name: $fileName", e)
            assetManager.openFd("$ASSET_PATH_PREFIX$SILENCE_WAV")
        }

        val id = soundPool.load(afd, 1)

        Log.d(TAG, "loadAssetFile: fileName=$fileName | soundId=$id")

        return@withContext id
    }

    private suspend fun loadSoundString(str: String): Int =
        if (str.isUriString()) loadUri(str) else loadAssetFile(str)

    suspend fun play(soundString: String) = withContext(defaultDispatcher) {
        loadSoundString(soundString)
        soundPool.setOnLoadCompleteListener { soundPool, sampleId, _ ->
            soundPool.play(sampleId, 1F, 1F, 1, 0, 1F)
        }
    }

    fun releaseResources() {
        Log.d(TAG, "releaseResources: Releasing soundPool and assetManager")
        soundPool.release()
    }
}