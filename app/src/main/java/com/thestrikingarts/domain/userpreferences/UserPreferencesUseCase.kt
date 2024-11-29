package com.thestrikingarts.domain.userpreferences

import com.thestrikingarts.domain.model.Language
import com.thestrikingarts.domain.model.TechniqueRepresentationFormat
import com.thestrikingarts.domain.model.Theme
import javax.inject.Inject

class UserPreferencesUseCase @Inject constructor(private val repository: UserPreferencesRepository) {
    val userPreferencesFlow = repository.userPreferencesFlow

    suspend fun toggleDynamicColors(enabled: Boolean) = repository.toggleDynamicColors(enabled)

    suspend fun updateLanguage(language: Language) = repository.updateLanguage(language)

    suspend fun updateTheme(theme: Theme) = repository.updateTheme(theme)

    suspend fun updateTechniqueRepresentationForm(techniqueForm: TechniqueRepresentationFormat) =
        repository.updateTechniqueRepresentationForm(techniqueForm)

    suspend fun updatePreparationDuration(durationSeconds: Int) =
        repository.updatePreparationDuration(durationSeconds)

    suspend fun updateShowQuittersData(value: Boolean) = repository.updateQuittersData(value)
}