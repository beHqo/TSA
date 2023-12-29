package com.example.android.strikingarts.ui.audioplayers.soundpool

import android.content.Context
import android.media.SoundPool
import android.net.Uri
import android.util.Log
import com.example.android.strikingarts.hilt.module.DefaultDispatcher
import com.example.android.strikingarts.hilt.module.IoDispatcher
import com.example.android.strikingarts.ui.audioplayers.PlayerConstants.SILENCE_AUDIO_FILE
import com.example.android.strikingarts.ui.audioplayers.mediaplayer.isUriString
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject

private const val TAG = "SoundPoolWrapper"

class SoundPoolWrapper @Inject constructor(
    @ApplicationContext private val context: Context,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val soundPool = SoundPool.Builder().setMaxStreams(1).build()
    private val assetManager = context.assets
    private val audioStringIdMap = mutableMapOf<String, Int>()

    suspend fun play(audioString: String) = withContext(defaultDispatcher) {
        if (audioStringIdMap[audioString] == null) {
            val id = loadSoundString(audioString)
            audioStringIdMap[audioString] = id

            playCallback(id)
        } else this@SoundPoolWrapper.play(audioStringIdMap[audioString]!!)
    }

    private fun playCallback(audioId: Int) {
        soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
            soundPool.play(audioId, 1F, 1F, 1, 0, 1F)
        }
    }

    private fun play(audioId: Int) {
        soundPool.play(audioId, 1F, 1F, 1, 0, 1F)
    }

    suspend fun loadSoundString(str: String): Int {
        val id = if (str.isUriString()) loadUri(str) else loadAssetFile(str)
        audioStringIdMap[str] = id

        return id
    }

    fun release() {
        Log.d(TAG, "releaseResources: Releasing SoundPool resources")

        for (id in audioStringIdMap.values) {
            soundPool.unload(id)
            soundPool.stop(id)
        }
        soundPool.release()
    }

    private suspend fun loadUri(uriString: String): Int = withContext(ioDispatcher) {
        val uri = Uri.parse(uriString)
        var id: Int

        val afd = try {
            context.contentResolver.openAssetFileDescriptor(uri, "r")
        } catch (e: FileNotFoundException) {
            Log.e(TAG, "loadUri: Failed to open file with the given uri\n$uriString", e)
            assetManager.openFd(SILENCE_AUDIO_FILE)
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
            assetManager.openFd(fileName)
        } catch (e: IOException) {
            Log.e(TAG, "setAudioSource: Failed to open file with the given name: $fileName", e)
            assetManager.openFd(SILENCE_AUDIO_FILE)
        }

        val id = soundPool.load(afd, 1)

        Log.d(TAG, "loadAssetFile: fileName=$fileName | soundId=$id")

        return@withContext id
    }
}