package com.thestrikingarts.domain.userpreferences

import com.thestrikingarts.domain.model.Language
import com.thestrikingarts.domain.model.TechniqueRepresentationFormat
import com.thestrikingarts.domain.model.Theme
import com.thestrikingarts.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userPreferencesFlow: Flow<UserPreferences>

    suspend fun updateLanguage(language: Language)

    suspend fun toggleDynamicColors(enabled: Boolean)

    suspend fun updateTheme(theme: Theme)

    suspend fun updateTechniqueRepresentationForm(techniqueRepresentationFormat: TechniqueRepresentationFormat)

    suspend fun updatePreparationDuration(durationSeconds: Int)

    suspend fun updateQuittersData(value: Boolean)
}