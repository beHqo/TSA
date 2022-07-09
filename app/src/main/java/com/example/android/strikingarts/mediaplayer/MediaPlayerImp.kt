package com.example.android.strikingarts.mediaplayer

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes
import com.example.android.strikingarts.database.entity.Technique
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class MediaPlayerWrapper(
    private val context: Context,
    private var techniqueList: List<Technique>,
    private val indices: Indices = Indices(),
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    private val delayTime: Long = 3_000L
) : Observer {
    private var lastTechniqueWasPlayed = false

    init {
        indices.observer = this
    }

    private var mp1: MediaPlayer? = MediaPlayer.create(context, techniqueList[indices.index1].sound)
    private var mp2: MediaPlayer? = MediaPlayer.create(context, techniqueList[indices.index2].sound)

    override fun onUpdateComponent1() = setAudioSource(mp1!!, techniqueList[indices.index1].sound)

    override fun onUpdateComponent2() = setAudioSource(mp2!!, techniqueList[indices.index2].sound)

    private fun setAudioSource(mp: MediaPlayer, @RawRes rawResId: Int) {
        val assetFd = context.resources.openRawResourceFd(rawResId) ?: return
        mp.run {
            reset()
            setDataSource(assetFd.fileDescriptor, assetFd.startOffset, assetFd.declaredLength)
            prepareAsync()
        }
    }

    fun initializeMediaPlayers() {
        resetCombo()

        scope.launch {
            delay(delayTime)
            mp1!!.start()
        }

        mp1!!.setOnCompletionListener {
            if (indices.index1.plus(2) < techniqueList.size) indices.index1 += 2
            else indices.index1 = 0
            if (indices.index2 == 1 && lastTechniqueWasPlayed) return@setOnCompletionListener
            mp2!!.start()
        }

        mp2!!.setOnCompletionListener {
            if (indices.index2.plus(2) < techniqueList.size) indices.index2 += 2
            else {
                indices.index2 = 1
                lastTechniqueWasPlayed = true
            }
            if (indices.index1 != 0) mp1!!.start()
        }
    }

    private fun resetCombo() {
        indices.index1 = 0
        indices.index2 = 1
        lastTechniqueWasPlayed = false
    }

    fun pause() {
        pauseMediaPlayer(mp1!!)
        pauseMediaPlayer(mp2!!)
    }

    private fun pauseMediaPlayer(mp: MediaPlayer) {
        if (mp.isPlaying) mp.pause()
    }

    fun resume() {
        // Todo: Test to see whether this approach works/what changes it needs
        initializeMediaPlayers()
    }

    fun destroyAllMediaPlayers() {
        mp1?.release()
        mp1 = null
        mp2?.release()
        mp2 = null
    }
}