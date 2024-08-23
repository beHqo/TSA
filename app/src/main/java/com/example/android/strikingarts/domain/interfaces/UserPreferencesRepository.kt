package com.example.android.strikingarts.domain.interfaces

import com.example.android.strikingarts.domain.model.Language
import com.example.android.strikingarts.domain.model.TechniqueRepresentationFormat
import com.example.android.strikingarts.domain.model.Theme
import com.example.android.strikingarts.domain.model.UserPreferences
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