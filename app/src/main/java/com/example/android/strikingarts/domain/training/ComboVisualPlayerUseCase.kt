package com.example.android.strikingarts.domain.training

import android.util.Log
import com.example.android.strikingarts.di.DefaultDispatcher
import com.example.android.strikingarts.domain.constant.transparentHexCode
import com.example.android.strikingarts.domain.model.Combo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "ComboVisualPlayer"

class ComboVisualPlayerUseCase @Inject constructor(
    @DefaultDispatcher private val defaultDispatchers: CoroutineDispatcher
) {
    private var comboVisualPlayerJob: Job? = null

    private val _currentCombo = MutableStateFlow(Combo())
    private val _currentColorString = MutableStateFlow(transparentHexCode)
    private val _isPlaying = MutableStateFlow(false)

    val currentCombo = _currentCombo.asStateFlow()
    val currentColorString = _currentColorString.asStateFlow()
    val isPlaying = _isPlaying.asStateFlow()

    private suspend fun displayCombo(combo: Combo) {
        withContext(defaultDispatchers) {
            Log.d(TAG, "displayCombo: Started the combo display playback.")

            comboVisualPlayerJob = launch {
                _isPlaying.update { true }

                _currentCombo.update { combo }

                for (technique in combo.techniqueList) {
                    ensureActive()

                    _currentColorString.update { technique.color }

                    delay(technique.audioAttributes.durationMillis)
                }

                delay(combo.delayMillis)
            }
        }
    }

    suspend fun display(combo: Combo) = displayCombo(combo)

    fun pause() {
        Log.d(TAG, "pause: Called")

        dismissComboVisualPlayerJob()

        _currentCombo.update { Combo() }
        _currentColorString.update { transparentHexCode }

        _isPlaying.update { false }
    }

    fun release() {
        Log.d(TAG, "releaseResources: Called.")

        dismissComboVisualPlayerJob()

        _isPlaying.update { false }
    }

    private fun dismissComboVisualPlayerJob() {
        comboVisualPlayerJob?.cancel()
        comboVisualPlayerJob = null
    }
}