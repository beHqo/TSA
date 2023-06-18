package com.example.android.strikingarts.ui.techniquedetails

import android.net.Uri
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.common.ImmutableSet
import com.example.android.strikingarts.domain.model.TechniqueCategory.DEFENSE
import com.example.android.strikingarts.domain.model.TechniqueCategory.OFFENSE
import com.example.android.strikingarts.domain.model.TechniqueCategory.defenseTypes
import com.example.android.strikingarts.domain.model.TechniqueCategory.offenseTypes
import com.example.android.strikingarts.domain.model.TechniqueListItem
import com.example.android.strikingarts.domain.usecase.technique.RetrieveSoundAttributesUseCase
import com.example.android.strikingarts.domain.usecase.technique.RetrieveTechniqueUseCase
import com.example.android.strikingarts.domain.usecase.technique.UpsertTechniqueUseCase
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.example.android.strikingarts.ui.model.SoundAttributes
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
    private val retrieveTechniqueUseCase: RetrieveTechniqueUseCase,
    private val upsertTechniqueUseCase: UpsertTechniqueUseCase,
    private val retrieveSoundAttributesUseCase: RetrieveSoundAttributesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val techniqueId = savedStateHandle[TECHNIQUE_ID] ?: 0L

    private val technique = MutableStateFlow(TechniqueListItem())

    private val _loadingScreen = MutableStateFlow(true)
    private val _name = MutableStateFlow("")
    private val _num = MutableStateFlow("")
    private val _movementType = MutableStateFlow("")
    private val _techniqueType = MutableStateFlow("")
    private val _color = MutableStateFlow(TRANSPARENT_COLOR_VALUE)
    private val _uriString = MutableStateFlow("")
    private val _audioFileName = MutableStateFlow("")
    private val _uriCondition = MutableStateFlow(UriConditions.VALID)
    private val _soundAttributes = MutableStateFlow(SoundAttributes())
    private val _techniqueTypeList = MutableStateFlow(ImmutableSet<String>())

    val loadingScreen = _loadingScreen.asStateFlow()
    val name = _name.asStateFlow()
    val num = _num.asStateFlow()
    val movementType = _movementType.asStateFlow()
    val techniqueType = _techniqueType.asStateFlow()
    val color = _color.asStateFlow()
    val uriString = _uriString.asStateFlow()
    val audioFileName = _audioFileName.asStateFlow()
    val uriCondition = _uriCondition.asStateFlow()
    val soundAttributes = _soundAttributes.asStateFlow()
    val techniqueTypeList = _techniqueTypeList.asStateFlow()

    init {
        initializeTechniqueAndDisplayState()
    }

    private fun initializeTechniqueAndDisplayState() {
        if (techniqueId == 0L) return else viewModelScope.launch {
            technique.update { retrieveTechniqueUseCase(techniqueId) }

            initialUiUpdate()
        }
    }

    private fun initialUiUpdate() {
        _name.update { savedStateHandle[NAME] ?: technique.value.name }
        _num.update { savedStateHandle[NUM] ?: technique.value.num }
        _movementType.update { savedStateHandle[MOVEMENT_TYPE] ?: technique.value.movementType }
        _techniqueType.update { savedStateHandle[TECHNIQUE_TYPE] ?: technique.value.techniqueType }
        _color.update { technique.value.color }

        if (_movementType.value == OFFENSE) {
            _uriString.update { savedStateHandle[URI] ?: technique.value.audioUriString }
            if (_uriString.value.isNotEmpty()) _soundAttributes.update {
                checkUriAndRetrieveSoundAttributes(Uri.parse(_uriString.value))
            }
            _techniqueTypeList.update { ImmutableSet(offenseTypes.keys) }
        } else _techniqueTypeList.update { ImmutableSet(defenseTypes.keys) }

        _loadingScreen.update { false }
    }

    private fun checkUriAndRetrieveSoundAttributes(uri: Uri): SoundAttributes {
        val currentSoundAttributes = try {
            retrieveSoundAttributesUseCase(uri)
        } catch (e: Exception) {
            Log.e(TAG, "handleSelectedUri: Failed to retrieve sound attributes", e)
            _uriCondition.update { UriConditions.MISSING }
            savedStateHandle[URI_CONDITION] = _uriCondition.value

            SoundAttributes()
        }

        return currentSoundAttributes
    }

    fun onNameChange(value: String) {
        if (value.length <= TEXTFIELD_NAME_MAX_CHARS + 1) {
            _name.update { value }

            savedStateHandle[NAME] = value
        }
    }

    fun onNumChange(value: String) {
        if (value.isDigitsOnly()) {
            _num.update { value }

            savedStateHandle[NUM] = value
        }
    }

    fun onMovementTypeChange(newMovementType: String) {
        _movementType.update { newMovementType }
        _techniqueType.update { "" }

        if (newMovementType == DEFENSE) {
            _techniqueTypeList.update { ImmutableSet(defenseTypes.keys) }
            _num.update { "" }
            _soundAttributes.update { SoundAttributes() }
            _uriString.update { "" }
            _audioFileName.update { "" }
        } else if (newMovementType == OFFENSE) {
            _techniqueTypeList.update { ImmutableSet(offenseTypes.keys) }
            _color.update { TRANSPARENT_COLOR_VALUE }
        }

        savedStateHandle[MOVEMENT_TYPE] = newMovementType
    }

    fun onTechniqueTypeChange(newTechniqueType: String) {
        _techniqueType.update { newTechniqueType }
        savedStateHandle[TECHNIQUE_TYPE] = newTechniqueType
    }

    fun onColorChange(newColor: String) {
        _color.update { newColor }
    }

    fun onAudioFileNameChange(fileName: String) {
        _uriString.update { "" }
        savedStateHandle[URI] = ""

        _soundAttributes.update { SoundAttributes() }
        savedStateHandle[SOUND_ATTRIBUTES] = SoundAttributes()

        _audioFileName.update { fileName }
        savedStateHandle[AUDIO_FILE_NAME] = fileName
    }

    fun resetUriString() {
        if (_movementType.value == OFFENSE) {
            if (technique.value.audioUriString.isEmpty()) {
                _audioFileName.update { technique.value.audioAssetFileName }
                _soundAttributes.update { SoundAttributes() }
            } else {
                _uriString.update { technique.value.audioUriString }
                _soundAttributes.update { retrieveSoundAttributesUseCase(Uri.parse(_uriString.value)) }
            }
        }
    }

    fun handleSelectedUri(uri: Uri?) {
        if (uri == null) {
            Log.e(TAG, "checkUriCondition: uri is null."); return
        }

        val currentSoundAttributes = checkUriAndRetrieveSoundAttributes(uri)

        val uriCondition = checkUriCondition(currentSoundAttributes)
        _uriCondition.update { uriCondition }
        savedStateHandle[URI_CONDITION] = uriCondition

        if (_uriCondition.value == UriConditions.VALID) {
            _uriString.update { uri.toString() }
            savedStateHandle[URI] = uri.toString()

            _soundAttributes.update { currentSoundAttributes }
            savedStateHandle[SOUND_ATTRIBUTES] = currentSoundAttributes

            _audioFileName.update { "" }
            savedStateHandle[AUDIO_FILE_NAME] = ""
        }
    }

    private fun checkUriCondition(soundAttributes: SoundAttributes): UriConditions {
        return if (soundAttributes.durationMilli > MAX_AUDIO_LENGTH_MILLIE) UriConditions.DURATION_ERROR
        else if (soundAttributes.size > MAX_FILE_SIZE_BYTE) UriConditions.SIZE_ERROR
        else UriConditions.VALID
    }

    fun insertOrUpdateItem() {
        viewModelScope.launch {
            upsertTechniqueUseCase(
                TechniqueListItem(
                    id = techniqueId,
                    name = _name.value,
                    num = _num.value,
                    techniqueType = _techniqueType.value,
                    movementType = _movementType.value,
                    color = _color.value,
                    audioUriString = _uriString.value,
                    audioAssetFileName = _audioFileName.value,
                    canBeBodyshot = _movementType.value == OFFENSE,
                    canBeFaint = _movementType.value == OFFENSE
                )
            )
        }
    }

    companion object {
        private const val NAME = "name"
        private const val NUM = "num"
        private const val TECHNIQUE_TYPE = "technique_type"
        private const val MOVEMENT_TYPE = "movement_type"
        private const val AUDIO_FILE_NAME = "audioFileName"
        private const val URI = "uri"
        private const val URI_CONDITION = "uri_condition"
        private const val SOUND_ATTRIBUTES = "sound_attributes"

        internal const val MIME_TYPE = "audio/*"
        internal const val MAX_FILE_SIZE_MB = 1
        internal const val MAX_FILE_SIZE_BYTE = MAX_FILE_SIZE_MB * 1000 * 1024
        internal const val MAX_AUDIO_LENGTH_SEC = 3
        internal const val MAX_AUDIO_LENGTH_MILLIE = MAX_AUDIO_LENGTH_SEC * 1000L

        internal const val TRANSPARENT_COLOR_VALUE = "0"

        private const val TAG = "TechniqueDetailsViewMod"
    }
}