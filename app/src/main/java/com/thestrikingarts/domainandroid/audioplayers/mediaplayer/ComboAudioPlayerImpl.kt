package com.thestrikingarts.domainandroid.audioplayers.mediaplayer

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.thestrikingarts.di.DefaultDispatcher
import com.thestrikingarts.di.IoDispatcher
import com.thestrikingarts.domain.mediaplayer.ComboAudioPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.abs

private const val TAG = "ComboPlayer"

class ComboAudioPlayerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatchers: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ComboAudioPlayer {
    private var playJob: Job? = null
    private val assetManager = context.assets
    private val mpList = mutableListOf<MediaPlayer?>()
    private var latestPreparedComboPlaylistId: Long = -1
    private var currentlyPlayingMpIndex = -1

    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying = _isPlaying.asStateFlow()

    private fun cleanUpUnusedMediaPlayers(num: Int) {
        Log.d(TAG, "cleanUpUnusedMediaPlayers: Releasing the resources of $num MediaPlayer(s)")

        val affectedRange = mpList.lastIndex downTo mpList.lastIndex - num + 1

        for (i in affectedRange) {
            mpList[i]?.release()
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
            for (i in audioStringList.indices) {
                ensureActive()

                mpList[i]?.setAudioSource(context, assetManager, audioStringList[i])
            }
        }

    private fun prepareMpListWithOldAudioSource() {
        for (mp in mpList) if (mp?.isPlaying == true) try {
            mp.stop()
            mp.prepare()
        } catch (e: IllegalStateException) {
            Log.e(TAG, "prepareMpListWithOldAudioSource: Called in the wrong state", e)
        }
    }

    private fun setOnCompleteListeners(audioStringList: List<String>) {
        val lastIndex = audioStringList.lastIndex

        for (i in audioStringList.indices) {
            if (i >= lastIndex) return

            mpList[i]?.setOnCompletionListener {
                val nextMpIndex = i + 1

                if (nextMpIndex < mpList.size) {
                    mpList[nextMpIndex]?.start()

                    currentlyPlayingMpIndex = nextMpIndex
                }
            }
        }
    }

    override suspend fun setupMediaPlayers(comboId: Long, audioStringList: List<String>) {
        coroutineScope {
            if (comboId == latestPreparedComboPlaylistId) {
                Log.d(TAG, "play: Provided audioStringList was already set as datasource")

                prepareMpListWithOldAudioSource()
                ensureActive()
            } else {
                manageMpListSize(audioStringList.size)
                ensureActive()

                prepareMediaPlayers(audioStringList)

                setOnCompleteListeners(audioStringList)
                ensureActive()

                latestPreparedComboPlaylistId = comboId
            }
        }
    }

    override suspend fun play(comboDurationMillis: Long) = withContext(defaultDispatcher) {
        Log.d(TAG, "play: Started the comboPlayer playback.")

        playJob = launch {
            try {
                mpList[0]?.start()
                _isPlaying.update { true }
            } catch (e: IllegalStateException) {
                Log.e(TAG, "play: Called in the wrong state", e)
            }

            currentlyPlayingMpIndex = 0
            ensureActive()

            delay(comboDurationMillis)
        }
    }

    override fun pause() {
        Log.d(TAG, "pause: Called")

        _isPlaying.update { false }

        dismissPlayJob()

        for (mp in mpList) if (mp?.isPlaying == true) {
            try {
                mp.stop()
                mp.prepare()
            } catch (e: IllegalStateException) {
                Log.e(TAG, "pause: Called in the wrong state", e)
            }
        }
    }

    override fun release() {
        Log.d(
            TAG,
            "releaseAllResources: Releasing the resources of ${mpList.size} MediaPlayer(s) and canceling the coroutine job."
        )

        for ((i, mp) in mpList.withIndex()) {
            mp?.release(); mpList[i] = null
        }

        mpList.clear()

        dismissPlayJob()

        _isPlaying.update { false }
    }

    private fun dismissPlayJob() {
        playJob?.cancel()
        playJob = null
    }
}
