package com.example.android.strikingarts.ui.techniquedetails

import android.net.Uri
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.strikingarts.domain.common.ImmutableSet
import com.example.android.strikingarts.domain.model.AudioAttributes
import com.example.android.strikingarts.domain.model.SilenceAudioAttributes
import com.example.android.strikingarts.domain.model.TechniqueCategory.DEFENSE
import com.example.android.strikingarts.domain.model.TechniqueCategory.OFFENSE
import com.example.android.strikingarts.domain.model.TechniqueCategory.defenseTypes
import com.example.android.strikingarts.domain.model.TechniqueCategory.offenseTypes
import com.example.android.strikingarts.domain.model.TechniqueListItem
import com.example.android.strikingarts.domain.model.UriAudioAttributes
import com.example.android.strikingarts.domain.usecase.technique.RetrieveAudioAttributesUseCase
import com.example.android.strikingarts.domain.usecase.technique.RetrieveTechniqueUseCase
import com.example.android.strikingarts.domain.usecase.technique.UpsertTechniqueUseCase
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
    private val retrieveTechniqueUseCase: RetrieveTechniqueUseCase,
    private val upsertTechniqueUseCase: UpsertTechniqueUseCase,
    private val retrieveAudioAttributesUseCase: RetrieveAudioAttributesUseCase,
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
    private val _audioAttributes: MutableStateFlow<AudioAttributes> =
        MutableStateFlow(SilenceAudioAttributes)
    private val _uriCondition = MutableStateFlow(UriConditions.VALID)
    private val _techniqueTypeList = MutableStateFlow(ImmutableSet<String>())

    val loadingScreen = _loadingScreen.asStateFlow()
    val name = _name.asStateFlow()
    val num = _num.asStateFlow()
    val movementType = _movementType.asStateFlow()
    val techniqueType = _techniqueType.asStateFlow()
    val color = _color.asStateFlow()
    val uriCondition = _uriCondition.asStateFlow()
    val audioAttributes = _audioAttributes.asStateFlow()
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
            _audioAttributes.update {
                savedStateHandle[AUDIO_ATTRIBUTES] ?: technique.value.audioAttributes
            }
            _techniqueTypeList.update { ImmutableSet(offenseTypes.keys) }
        } else _techniqueTypeList.update { ImmutableSet(defenseTypes.keys) }

        _loadingScreen.update { false }
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
            _audioAttributes.update { SilenceAudioAttributes }
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

    fun setAssetAudioString(assetAudioString: String) {
        val assetAudioAttributes = retrieveAudioAttributesUseCase(assetAudioString)

        _audioAttributes.update { assetAudioAttributes }
        savedStateHandle[AUDIO_ATTRIBUTES] = assetAudioAttributes
    }

    fun resetUriString() {
        _audioAttributes.update { technique.value.audioAttributes }
        savedStateHandle[AUDIO_ATTRIBUTES] = technique.value.audioAttributes
    }

    fun handleSelectedUri(uri: Uri?) {
        if (uri == null) {
            Log.e(TAG, "checkUriCondition: uri is null."); return
        }

        val currentSoundAttributes = checkUriAndRetrieveSoundAttributes(uri.toString())

        val uriCondition = checkUriCondition(currentSoundAttributes)
        _uriCondition.update { uriCondition }
        savedStateHandle[URI_CONDITION] = uriCondition

        if (_uriCondition.value == UriConditions.VALID) {
            _audioAttributes.update { currentSoundAttributes }
            savedStateHandle[AUDIO_ATTRIBUTES] = currentSoundAttributes
        }
    }

    private fun checkUriAndRetrieveSoundAttributes(uriString: String): UriAudioAttributes =
        retrieveAudioAttributesUseCase(uriString) as UriAudioAttributes

    private fun checkUriCondition(soundAttributes: UriAudioAttributes): UriConditions =
        if (soundAttributes.durationMilli > MAX_AUDIO_LENGTH_MILLIE) UriConditions.DURATION_ERROR
        else if (soundAttributes.sizeByte > MAX_FILE_SIZE_BYTE) UriConditions.SIZE_ERROR
        else UriConditions.VALID

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
                    audioAttributes = _audioAttributes.value,
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
        private const val URI_CONDITION = "uri_condition"
        private const val AUDIO_ATTRIBUTES = "sound_attributes"

        internal const val MIME_TYPE = "audio/*"
        internal const val MAX_FILE_SIZE_MB = 1
        internal const val MAX_FILE_SIZE_BYTE = MAX_FILE_SIZE_MB * 1000 * 1024
        internal const val MAX_AUDIO_LENGTH_SEC = 3
        internal const val MAX_AUDIO_LENGTH_MILLIE = MAX_AUDIO_LENGTH_SEC * 1000L

        internal const val TRANSPARENT_COLOR_VALUE = "0"

        private const val TAG = "TechniqueDetailsViewMod"
    }
}