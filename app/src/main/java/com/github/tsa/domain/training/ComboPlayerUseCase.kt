package com.github.tsa.domain.training

import com.github.tsa.domain.mapper.getAudioDuration
import com.github.tsa.domain.mapper.getAudioStringList
import com.github.tsa.domain.mediaplayer.ComboAudioPlayer
import com.github.tsa.domain.model.Combo
import com.github.tsa.domain.model.TimerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ComboPlayerUseCase @Inject constructor(
    private val comboAudioPlayer: ComboAudioPlayer,
    private val comboVisualPlayerUseCase: ComboVisualPlayerUseCase
) {
    private var comboPlayerJob: Job? = null

    val currentColorString = comboVisualPlayerUseCase.currentColorString
    val currentCombo = comboVisualPlayerUseCase.currentCombo

    suspend fun startPlayback(
        comboList: List<Combo>,
        currentComboIndex: StateFlow<Int>,
        timerState: StateFlow<TimerState>,
        isRoundTimerActive: StateFlow<Boolean>,
        incrementComboIndex: () -> Unit
    ) = coroutineScope {
        comboPlayerJob = launch {
            while (currentComboIndex.value <= comboList.lastIndex && timerState.value.timerStatus.isTimerRunning() && isRoundTimerActive.value) {
                val currentCombo = comboList[currentComboIndex.value]

                comboAudioPlayer.setupMediaPlayers(
                    currentCombo.id, currentCombo.getAudioStringList()
                )

                val comboAudioJob = launch {
                    comboAudioPlayer.play(currentCombo.getAudioDuration() + currentCombo.delayMillis)
                }

                val comboVisualJob = launch { comboVisualPlayerUseCase.display(currentCombo) }

                comboAudioJob.join()
                comboVisualJob.join()

                incrementComboIndex()
            }
        }
    }

    fun pause() {
        dismissComboPlayerJob()

        comboVisualPlayerUseCase.pause()

        comboAudioPlayer.pause()
    }

    fun release() {
        dismissComboPlayerJob()

        comboAudioPlayer.release()
        comboVisualPlayerUseCase.release()
    }

    private fun dismissComboPlayerJob() {
        comboPlayerJob?.cancel()
        comboPlayerJob = null
    }
}