package com.github.tsa.ui.userpreferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.tsa.domain.model.Language
import com.github.tsa.domain.model.TechniqueRepresentationFormat
import com.github.tsa.domain.model.Theme
import com.github.tsa.domain.userpreferences.UserPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPreferencesViewModel @Inject constructor(private val userPreferencesUseCase: UserPreferencesUseCase) :
    ViewModel() {
    val userPreferencesFlow = userPreferencesUseCase.userPreferencesFlow

    fun toggleDynamicColors(enabled: Boolean) {
        viewModelScope.launch { userPreferencesUseCase.toggleDynamicColors(enabled) }
    }

    fun updateLanguage(language: Language) {
        viewModelScope.launch { userPreferencesUseCase.updateLanguage(language) }
    }

    fun updateTheme(theme: Theme) {
        viewModelScope.launch { userPreferencesUseCase.updateTheme(theme) }
    }

    fun updateTechniqueRepresentationForm(techniqueForm: TechniqueRepresentationFormat) {
        viewModelScope.launch {
            userPreferencesUseCase.updateTechniqueRepresentationForm(techniqueForm)
        }
    }

    fun updatePreparationDuration(durationSeconds: Int) {
        viewModelScope.launch { userPreferencesUseCase.updatePreparationDuration(durationSeconds) }
    }

    fun updateShowQuittersData(value: Boolean) {
        viewModelScope.launch { userPreferencesUseCase.updateShowQuittersData(value) }
    }
}