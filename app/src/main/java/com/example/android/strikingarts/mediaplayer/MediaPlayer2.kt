package com.example.android.strikingarts.mediaplayer

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.annotation.RawRes
import com.example.android.strikingarts.database.entity.Technique
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

// TODO: When you can test this class, see if the MediaPlayer instances need to be nullable and whether calling release on them suffices.
// TODO 2: handle IllegalArgumentException and IOException

internal class MediaPlayer2(
    private val context: Context,
    private var techniqueList: List<Technique>,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    private val delayTime: Long = 3_000L
) {
    private var lastTechniqueWasPlayed = false

    private var index1 by Delegates.observable(0) { _, _, newValue ->
        mp1?.setAudioSource(context, techniqueList[newValue].sound)
    }
    private var index2 by Delegates.observable(0) { _, _, newValue ->
        mp2?.setAudioSource(context, techniqueList[newValue].sound)
    }

    private var mp1: MediaPlayer? = MediaPlayer.create(context, techniqueList[index1].sound)
    private var mp2: MediaPlayer? = MediaPlayer.create(context, techniqueList[index2].sound)
    private var mp3: MediaPlayer? = MediaPlayer()

    fun initializeMediaPlayers() {
        resetCombo()

        scope.launch {
            delay(delayTime)
            mp1!!.start()
        }

        handleIndices()
    }

    private fun handleIndices() {
        mp1!!.setOnCompletionListener {
            if (index1.plus(2) < techniqueList.size) index1 += 2
            else index1 = 0
            if (index2 == 1 && lastTechniqueWasPlayed) return@setOnCompletionListener
            mp2!!.start()
        }

        mp2!!.setOnCompletionListener {
            if (index2.plus(2) < techniqueList.size) index2 += 2
            else {
                index2 = 1
                lastTechniqueWasPlayed = true
            }
            if (index1 != 0) mp1!!.start()
        }
    }

    private fun resetCombo() {
        index1 = 0
        index2 = 1
        lastTechniqueWasPlayed = false
    }

    fun pause() {
        pauseMediaPlayer(mp1!!)
        pauseMediaPlayer(mp2!!)
    }

    private fun pauseMediaPlayer(mp: MediaPlayer) {
        if (mp.isPlaying) mp.pause()
    }

    fun resume() { // Todo: Test to see whether this approach works/what changes it needs
        initializeMediaPlayers()
    }

    fun destroyAllMediaPlayers() {
        mp1?.release()
        mp1 = null
        mp2?.release()
        mp2 = null
    }
}

private fun MediaPlayer.setAudioSource(context: Context, @RawRes rawResId: Int) {
    val assetFd = context.resources.openRawResourceFd(rawResId) ?: return
    this.run {
        reset()
        setDataSource(assetFd.fileDescriptor, assetFd.startOffset, assetFd.declaredLength)
        prepareAsync()
    }
}