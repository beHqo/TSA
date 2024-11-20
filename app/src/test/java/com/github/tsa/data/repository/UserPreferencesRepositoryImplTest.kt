package com.github.tsa.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.github.tsa.data.local.datastore.UserPreferencesKeys
import com.github.tsa.domain.model.Language
import com.github.tsa.domain.model.TechniqueRepresentationFormat
import com.github.tsa.domain.model.Theme
import com.github.tsa.domain.model.UserPreferences
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

private const val DATASTORE_FILE_NAME = "test_datastore.preferences_pb"

class UserPreferencesRepositoryTest {
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @get:Rule
    val tempFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope,
        produceFile = { tempFolder.newFile(DATASTORE_FILE_NAME) })
    private val repository = UserPreferencesRepositoryImpl(testDataStore)

    @Test
    fun testInitialUserPreferences() = runTest(testDispatcher) {
        val expected = testDataStore.data.first().toUserPreferences()
        val actual = repository.userPreferencesFlow.first()

        expected shouldBe actual
    }

    @Test
    fun updateLanguage() = runTest(testDispatcher) {
        val persian = Language.PERSIAN
        repository.updateLanguage(persian)

        testDataStore.edit { preferences ->
            preferences[UserPreferencesKeys.LANGUAGE] = persian.name
        }

        val expected = testDataStore.data.first()[UserPreferencesKeys.LANGUAGE]
        val actual = repository.userPreferencesFlow.first().language.name

        expected shouldBe actual
    }

    @Test
    fun toggleDynamicColors() = runTest(testDispatcher) {
        val dynamicColorsEnabled = true
        repository.toggleDynamicColors(dynamicColorsEnabled)

        testDataStore.edit { preferences ->
            preferences[UserPreferencesKeys.DYNAMIC_COLORS] = dynamicColorsEnabled
        }

        val expected = testDataStore.data.first()[UserPreferencesKeys.DYNAMIC_COLORS]
        val actual = repository.userPreferencesFlow.first().dynamicColorsEnabled

        expected shouldBe actual
    }

    @Test
    fun updateTheme() = runTest(testDispatcher) {
        val darkTheme = Theme.DARK
        repository.updateTheme(darkTheme)

        testDataStore.edit { preferences ->
            preferences[UserPreferencesKeys.THEME] = darkTheme.name
        }

        val expected = testDataStore.data.first()[UserPreferencesKeys.THEME]
        val actual = repository.userPreferencesFlow.first().theme.name

        expected shouldBe actual
    }

    @Test
    fun updateTechniqueRepresentationForm() = runTest(testDispatcher) {
        val techniqueRepresentation = TechniqueRepresentationFormat.NUM
        repository.updateTechniqueRepresentationForm(techniqueRepresentation)

        testDataStore.edit { preferences ->
            preferences[UserPreferencesKeys.TECHNIQUE_FORM] = techniqueRepresentation.name
        }

        val expected = testDataStore.data.first()[UserPreferencesKeys.TECHNIQUE_FORM]
        val actual = repository.userPreferencesFlow.first().techniqueRepresentationFormat.name

        expected shouldBe actual
    }

    @Test
    fun updatePreparationDuration() = runTest(testDispatcher) {
        val preparationPeriod = 7
        repository.updatePreparationDuration(preparationPeriod)

        testDataStore.edit { preferences ->
            preferences[UserPreferencesKeys.PREPARATION_PERIOD] = preparationPeriod
        }

        val expected = testDataStore.data.first()[UserPreferencesKeys.PREPARATION_PERIOD]
        val actual = repository.userPreferencesFlow.first().preparationPeriodSeconds

        expected shouldBe actual
    }

    @Test
    fun updateQuittersData() = runTest(testDispatcher) {
        val showQuittersData = true
        repository.updateQuittersData(showQuittersData)

        testDataStore.edit { preferences ->
            preferences[UserPreferencesKeys.SHOW_QUITTERS_DATA] = showQuittersData
        }

        val expected = testDataStore.data.first()[UserPreferencesKeys.SHOW_QUITTERS_DATA]
        val actual = repository.userPreferencesFlow.first().showQuittersData

        expected shouldBe actual
    }

    // Copied from UserPreferencesRepositoryImpl
    private fun Preferences.toUserPreferences(): UserPreferences = UserPreferences(
        language = Language.valueOf(
            this[UserPreferencesKeys.LANGUAGE] ?: Language.UNSPECIFIED.name
        ),
        theme = Theme.valueOf(this[UserPreferencesKeys.THEME] ?: Theme.UNSPECIFIED.name),
        preparationPeriodSeconds = this[UserPreferencesKeys.PREPARATION_PERIOD] ?: 5,
        techniqueRepresentationFormat = TechniqueRepresentationFormat.valueOf(
            this[UserPreferencesKeys.TECHNIQUE_FORM]
                ?: TechniqueRepresentationFormat.UNSPECIFIED.name
        ),
        showQuittersData = this[UserPreferencesKeys.SHOW_QUITTERS_DATA] ?: false
    )
}