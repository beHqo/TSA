package com.example.android.strikingarts.mediaplayer

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.hilt.di.IoDispatcher
import com.example.android.strikingarts.ui.model.ComboPlaylist
import com.example.android.strikingarts.ui.model.toTime
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.abs

private const val TAG = "ComboPlayer"

class ComboPlayer @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatchers: CoroutineDispatcher,
    private val coroutineScope: CoroutineScope,
    private val comboPlaylists: ImmutableList<ComboPlaylist> = ImmutableList()
) {
    private val assetManager = context.assets
    private var index = 0
    private val mpList = mutableListOf<MediaPlayer?>()

    private fun cleanUpUnusedMediaPlayers(num: Int) {
        Log.d(TAG, "cleanUpUnusedMediaPlayers: Releasing the resources of $num MediaPlayer(s)")

        val affectedRange = mpList.lastIndex downTo mpList.lastIndex - num + 1

        for (i in affectedRange) {
            mpList[i]!!.release()
            mpList[i] = null
            mpList.removeAt(i)
        }
    }

    private fun addMediaPlayers(num: Int) {
        Log.d(TAG, "addMediaPlayers: Adding $num MediaPlayer(s)")

        repeat(num) { mpList.add(MediaPlayer()) }
    }


    private fun manageMpListSize(comboSize: Int) {
        val numberOfMediaPlayers = comboSize - mpList.size

        when {
            numberOfMediaPlayers > 0 -> addMediaPlayers(numberOfMediaPlayers)
            numberOfMediaPlayers < 0 -> cleanUpUnusedMediaPlayers(abs(numberOfMediaPlayers))
            else -> return
        }
    }

    private suspend fun prepareMediaPlayers(audioStringList: List<String>) =
        withContext(ioDispatchers) {
            for (i in audioStringList.indices) coroutineScope {
                mpList[i]!!.setAudioSource(context, assetManager, audioStringList[i])
            }
        }

    private fun setOnComplete(audioStringList: List<String>) {
        val lastIndex = audioStringList.lastIndex

        for (i in audioStringList.indices) {
            if (i >= lastIndex) return

            mpList[i]!!.setOnCompletionListener { if (i + 1 < mpList.size) mpList[i + 1]!!.start() }
        }
    }

    private fun getComboDuration(): Long {
        var duration = 1000L

        for (mp in mpList) duration += mp!!.duration

        Log.d(TAG, "getComboDuration: ${duration.toTime()}")

        return duration
    }

    fun play(audioStringList: List<String>) = coroutineScope.launch {
        manageMpListSize(audioStringList.size)

        coroutineScope { prepareMediaPlayers(audioStringList) }

        setOnComplete(audioStringList)

        mpList[0]!!.start()

        Log.d(TAG, "play: ${getComboDuration()}")
    }

    fun play() = coroutineScope.launch {
        val currentComboIndex = index

        for (i in currentComboIndex..comboPlaylists.lastIndex) {
            ensureActive()

            val comboPlayList = comboPlaylists[i]
            val audioStringList = comboPlayList.audioStringList

            manageMpListSize(audioStringList.size)

            coroutineScope { prepareMediaPlayers(audioStringList) }

            setOnComplete(audioStringList)

            delay(comboPlayList.delay.toLong())

            mpList[0]!!.start()

            delay(getComboDuration())

            index++
        }

        index = 0
    }

    fun pause() = coroutineScope.launch {
        for (mp in mpList) {
            coroutineScope { mp!!.stop(); mp.prepareAsync() }
        }

        coroutineScope.coroutineContext.cancelChildren()
    }

    fun releaseAllResources() {
        Log.d(
            TAG,
            "releaseAllResources: Releasing the resources of ${mpList.size} MediaPlayer(s), closing AssetManager and canceling CoroutineScope"
        )

        for ((i, mp) in mpList.withIndex()) {
            mp!!.release(); mpList[i] = null
        }
        mpList.clear()

        assetManager.close()

        coroutineScope.coroutineContext.cancelChildren()
        coroutineScope.cancel()
    }
}
