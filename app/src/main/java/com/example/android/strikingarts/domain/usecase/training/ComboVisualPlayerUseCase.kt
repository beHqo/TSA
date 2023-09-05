package com.example.android.strikingarts.domain.usecase.training

import android.util.Log
import com.example.android.strikingarts.domain.model.ComboListItem
import com.example.android.strikingarts.hilt.module.DefaultDispatcher
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

private const val TAG = "ComboDisplayUseCase"

class ComboVisualPlayerUseCase @Inject constructor(
    @DefaultDispatcher private val defaultDispatchers: CoroutineDispatcher
) {
    private var comboVisualPlayerJob: Job? = null

    private val _currentComboText = MutableStateFlow("")
    private val _currentColorString = MutableStateFlow("0")
    private val _isPlaying = MutableStateFlow(false)

    val currentComboText = _currentComboText.asStateFlow()
    val currentColorString = _currentColorString.asStateFlow()
    val isPlaying = _isPlaying.asStateFlow()

    private suspend fun displayCombo(combo: ComboListItem) {
        withContext(defaultDispatchers) {
            Log.d(TAG, "displayCombo: Started the combo display playback.")

            comboVisualPlayerJob = launch {
                _isPlaying.update { true }

                _currentComboText.update { combo.techniqueList.joinToString { it.name } } //todo: or num

                for (technique in combo.techniqueList) {
                    ensureActive()

                    _currentColorString.update { technique.color }

                    delay(technique.audioAttributes.durationMillis)
                }

                delay(combo.delayMillis)
            }
        }
    }

    suspend fun display(combo: ComboListItem) = displayCombo(combo)

    fun pause() {
        Log.d(TAG, "pause: Called.")

        dismissComboVisualPlayerJob()

        _currentComboText.update { "" }
        _currentColorString.update { "0" }

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