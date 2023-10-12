package com.example.android.strikingarts.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.android.strikingarts.data.local.datastore.UserPreferencesKeys
import com.example.android.strikingarts.domain.interfaces.UserPreferencesRepository
import com.example.android.strikingarts.domain.model.Language
import com.example.android.strikingarts.domain.model.TechniqueRepresentationFormat
import com.example.android.strikingarts.domain.model.Theme
import com.example.android.strikingarts.domain.model.UserPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(private val userPreferencesStore: DataStore<Preferences>) :
    UserPreferencesRepository {
    override val userPreferencesFlow =
        userPreferencesStore.data.catch { Log.e(TAG, USER_PREFS_READ_LOG_TEXT, it) }
            .map { preferences -> preferences.toUserPreferences() }

    override suspend fun updateLanguage(language: Language) =
        safeUpdate("Given Language = ${language.name}") {
            it[UserPreferencesKeys.LANGUAGE] = language.name
        }

    override suspend fun updateTheme(theme: Theme) =
        safeUpdate("Given Theme = ${theme.name}") { it[UserPreferencesKeys.THEME] = theme.name }

    override suspend fun updateTechniqueRepresentationForm(techniqueRepresentationFormat: TechniqueRepresentationFormat) =
        safeUpdate("Given TechniqueForm = $techniqueRepresentationFormat") {
            it[UserPreferencesKeys.TECHNIQUE_FORM] = techniqueRepresentationFormat.name
        }

    override suspend fun updatePreparationDuration(durationSeconds: Int) =
        safeUpdate("Given preparationPeriodSeconds = $durationSeconds") {
            it[UserPreferencesKeys.PREPARATION_PERIOD] = durationSeconds
        }

    private fun Preferences.toUserPreferences(): UserPreferences = UserPreferences(
        language = Language.valueOf(
            this[UserPreferencesKeys.LANGUAGE] ?: Language.UNSPECIFIED.name
        ),
        theme = Theme.valueOf(this[UserPreferencesKeys.THEME] ?: Theme.UNSPECIFIED.name),
        preparationPeriodSeconds = this[UserPreferencesKeys.PREPARATION_PERIOD]
            ?: DEFAULT_PREPARATION_PERIOD_SECONDS,
        techniqueRepresentationFormat = TechniqueRepresentationFormat.valueOf(
            this[UserPreferencesKeys.TECHNIQUE_FORM]
                ?: TechniqueRepresentationFormat.UNSPECIFIED.name
        )
    )

    private suspend fun safeUpdate(logMessage: String, operation: (MutablePreferences) -> Unit) {
        try {
            userPreferencesStore.edit { operation(it) }
        } catch (e: Exception) {
            Log.e(TAG, "${SAFE_UPDATE_LOG_TEXT}$\n$logMessage", e)
        }
    }

    companion object {
        private const val DEFAULT_PREPARATION_PERIOD_SECONDS = 5

        private const val TAG = "UserPreferencesRepo"
        private const val SAFE_UPDATE_LOG_TEXT =
            "safeUpdate: Failed to update UserPreferences with the given value"
        private const val USER_PREFS_READ_LOG_TEXT =
            "userPreferencesFlow: Failed to read the data from UserPreferences"
    }
}