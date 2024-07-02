package com.example.android.strikingarts.ui.techniquedetails

import android.net.Uri
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.common.constants.transparentHexCode
import com.example.android.strikingarts.domain.model.AudioAttributes
import com.example.android.strikingarts.domain.model.MovementType
import com.example.android.strikingarts.domain.model.SilenceAudioAttributes
import com.example.android.strikingarts.domain.model.Technique
import com.example.android.strikingarts.domain.model.TechniqueType
import com.example.android.strikingarts.domain.model.UriAudioAttributes
import com.example.android.strikingarts.domain.usecase.audioattributes.RetrieveAudioAttributesUseCase
import com.example.android.strikingarts.domain.usecase.technique.RetrieveTechniqueUseCase
import com.example.android.strikingarts.domain.usecase.technique.UpsertTechniqueUseCase
import com.example.android.strikingarts.ui.audioplayers.soundpool.SoundPoolWrapper
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.example.android.strikingarts.ui.model.UriConditions
import com.example.android.strikingarts.ui.navigation.Screen.Arguments.TECHNIQUE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TechniqueDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val retrieveTechniqueUseCase: RetrieveTechniqueUseCase,
    private val upsertTechniqueUseCase: UpsertTechniqueUseCase,
    private val retrieveAudioAttributesUseCase: RetrieveAudioAttributesUseCase,
    private val soundPoolWrapper: SoundPoolWrapper
) : ViewModel() {
    private val techniqueId = savedStateHandle[TECHNIQUE_ID] ?: 0L

    private lateinit var technique: Technique
    var isTechniqueNew = true; private set

    private val _loadingScreen = MutableStateFlow(true)
    private val _name = MutableStateFlow("")
    private val _num = MutableStateFlow("")
    private val _movementType = MutableStateFlow(MovementType.OFFENSE)
    private val _techniqueType = MutableStateFlow(TechniqueType.PUNCH)
    private val _color = MutableStateFlow(transparentHexCode)
    private val _audioAttributes: MutableStateFlow<AudioAttributes> =
        MutableStateFlow(SilenceAudioAttributes)
    private val _uriCondition = MutableStateFlow(UriConditions.VALID)

    val loadingScreen = _loadingScreen.asStateFlow()
    val name = _name.asStateFlow()
    val num = _num.asStateFlow()
    val movementType = _movementType.asStateFlow()
    val techniqueType = _techniqueType.asStateFlow()
    val color = _color.asStateFlow()
    val uriCondition = _uriCondition.asStateFlow()
    val audioAttributes = _audioAttributes.asStateFlow()

    init {
        viewModelScope.launch { initialUiUpdate() }
    }

    private suspend fun initialUiUpdate() {
        if (techniqueId == 0L) technique = Technique() else {
            technique = retrieveTechniqueUseCase(techniqueId); isTechniqueNew = false
        }

        _name.update { savedStateHandle[NAME] ?: technique.name }
        _num.update { savedStateHandle[NUM] ?: technique.num }
        _movementType.update { savedStateHandle[MOVEMENT_TYPE] ?: technique.movementType }
        _techniqueType.update { savedStateHandle[TECHNIQUE_TYPE] ?: technique.techniqueType }
        _color.update { technique.color }

        if (_movementType.value == MovementType.OFFENSE) _audioAttributes.update {
            savedStateHandle[AUDIO_ATTRIBUTES] ?: technique.audioAttributes
        }

        _loadingScreen.update { false }
    }

    fun onNameChange(value: String) {
        if (value.length <= TEXTFIELD_NAME_MAX_CHARS + 1) _name.update { value.trim() }
    }

    fun onNumChange(value: String) {
        if (value.isDigitsOnly()) _num.update { value.trim() }
    }

    fun onMovementTypeChange(newMovementType: MovementType) {
        _movementType.update { newMovementType }

        if (newMovementType == MovementType.DEFENSE) {
            _techniqueType.update { TechniqueType.HAND_BLOCK }
            _num.update { "" }
            _audioAttributes.update { SilenceAudioAttributes }
        } else {
            _techniqueType.update { TechniqueType.PUNCH }
            _color.update { transparentHexCode }
        }
    }

    fun onTechniqueTypeChange(newTechniqueType: TechniqueType) {
        _techniqueType.update { newTechniqueType }
    }

    fun onColorChange(newColor: String) {
        _color.update { newColor }
    }

    fun setAssetAudioString(assetAudioString: String) {
        val assetAudioAttributes = retrieveAudioAttributesUseCase(assetAudioString)

        _audioAttributes.update { assetAudioAttributes }
    }

    fun resetUriString() {
        _audioAttributes.update { technique.audioAttributes }
    }

    fun handleSelectedUri(uri: Uri?) {
        if (uri == null) {
            Log.e(TAG, "checkUriCondition: uri is null."); return
        }

        val currentSoundAttributes = checkUriAndRetrieveSoundAttributes(uri.toString())

        val uriCondition = checkUriCondition(currentSoundAttributes)
        _uriCondition.update { uriCondition }

        if (_uriCondition.value == UriConditions.VALID) _audioAttributes.update { currentSoundAttributes }
    }

    private fun checkUriAndRetrieveSoundAttributes(uriString: String): UriAudioAttributes =
        retrieveAudioAttributesUseCase(uriString) as UriAudioAttributes

    private fun checkUriCondition(soundAttributes: UriAudioAttributes): UriConditions =
        if (soundAttributes.durationMillis > MAX_AUDIO_LENGTH_MILLIE) UriConditions.DURATION_ERROR
        else if (soundAttributes.sizeByte > MAX_FILE_SIZE_BYTE) UriConditions.SIZE_ERROR
        else UriConditions.VALID

    fun play(audioString: String) = viewModelScope.launch { soundPoolWrapper.play(audioString) }

    fun insertOrUpdateItem() {
        viewModelScope.launch {
            upsertTechniqueUseCase(
                Technique(
                    id = techniqueId,
                    name = _name.value,
                    num = _num.value,
                    techniqueType = _techniqueType.value,
                    movementType = _movementType.value,
                    color = _color.value,
                    audioAttributes = _audioAttributes.value
                )
            )
        }
    }

    fun surviveProcessDeath() {
        savedStateHandle[NAME] = _name.value
        savedStateHandle[NUM] = _num.value
        savedStateHandle[TECHNIQUE_TYPE] = _techniqueType.value
        savedStateHandle[MOVEMENT_TYPE] = _movementType.value
        savedStateHandle[URI_CONDITION] = _uriCondition.value
        savedStateHandle[AUDIO_ATTRIBUTES] = _audioAttributes.value
        savedStateHandle[COLOR] = _color.value
    }

    override fun onCleared() {
        soundPoolWrapper.release()
        super.onCleared()
    }

    companion object {
        private const val NAME = "name"
        private const val NUM = "num"
        private const val TECHNIQUE_TYPE = "technique_type"
        private const val MOVEMENT_TYPE = "movement_type"
        private const val URI_CONDITION = "uri_condition"
        private const val AUDIO_ATTRIBUTES = "sound_attributes"
        private const val COLOR = "color"

        internal const val MIME_TYPE = "audio/*"
        internal const val MAX_FILE_SIZE_MB = 1
        private const val MAX_FILE_SIZE_BYTE = MAX_FILE_SIZE_MB * 1000 * 1024
        internal const val MAX_AUDIO_LENGTH_SEC = 3
        internal const val MAX_AUDIO_LENGTH_MILLIE = MAX_AUDIO_LENGTH_SEC * 1000L

        private const val TAG = "TechniqueDetailsViewMod"
    }
}