package com.example.android.strikingarts.mediaplayer

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.ui.model.ComboPlaylist
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs

private const val TAG = "ComboPlayer"

class ComboPlayer(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
    private val ioDispatchers: CoroutineDispatcher = Dispatchers.IO,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val comboPlaylists: ImmutableList<ComboPlaylist> = ImmutableList()
) {
    private val assetManager = context.assets
    private var index = 0
    private val mpList = mutableListOf<MediaPlayer?>()

    private var playJob: Job = Job()

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

    fun play(audioStringList: ImmutableList<String>) {
        playJob = coroutineScope.launch {
            withContext(defaultDispatcher) {
                manageMpListSize(audioStringList.size)

                coroutineScope { prepareMediaPlayers(audioStringList) }

                setOnComplete(audioStringList)

                mpList[0]!!.start()
            }
        }
    }

    fun play() {
        playJob = coroutineScope.launch {
            withContext(defaultDispatcher) {
                val currentComboIndex = index

                for (i in currentComboIndex..comboPlaylists.lastIndex) {
                    ensureActive()

                    val comboPlayList = comboPlaylists[i]
                    val audioStringList =
                        comboPlayList.playableTechniqueList.map { it.audioAttributes.audioString }

                    manageMpListSize(audioStringList.size)

                    coroutineScope { prepareMediaPlayers(audioStringList) }

                    setOnComplete(audioStringList)

                    delay(comboPlayList.delay.toLong())

                    mpList[0]!!.start()

                    delay(comboPlayList.playableTechniqueList.sumOf { it.audioAttributes.durationMilli }) //TODO: Maybe add 100ms extra delay?

                    index++
                }
            }

            index = 0
        }
    }

    fun pause() = coroutineScope.launch {
        Log.d(TAG, "pause: Called")

        playJob.cancel()

        withContext(defaultDispatcher) {
            for (mp in mpList) {
                coroutineScope { mp!!.stop(); mp.prepareAsync() }
            }
        }
    }

    fun releaseAllResources() {
        Log.d(
            TAG,
            "releaseAllResources: Releasing the resources of ${mpList.size} MediaPlayer(s) and canceling CoroutineScope"
        )

        for ((i, mp) in mpList.withIndex()) {
            mp!!.release(); mpList[i] = null
        }
        mpList.clear()

        playJob.cancel()
        coroutineScope.coroutineContext.cancelChildren()
        coroutineScope.cancel()
    }
}
