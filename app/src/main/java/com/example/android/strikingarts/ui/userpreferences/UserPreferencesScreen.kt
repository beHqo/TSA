package com.example.android.strikingarts.ui.userpreferences

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.R
import com.example.android.strikingarts.domain.model.Language
import com.example.android.strikingarts.domain.model.TechniqueRepresentationFormat
import com.example.android.strikingarts.domain.model.Theme
import com.example.android.strikingarts.domain.model.UserPreferences
import com.example.android.strikingarts.ui.components.DoneTextButton
import com.example.android.strikingarts.ui.components.DoubleButtonBottomSheetBox
import com.example.android.strikingarts.ui.components.ModalBottomSheet
import com.example.android.strikingarts.ui.components.SecondaryText
import com.example.android.strikingarts.ui.components.SingleButtonBottomSheetBox
import com.example.android.strikingarts.ui.components.TimePicker
import com.example.android.strikingarts.ui.components.detailsitem.DetailsItem
import com.example.android.strikingarts.ui.components.detailsitem.SelectableDetailsItem
import com.example.android.strikingarts.ui.model.toTime
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager

private enum class BottomSheetContent {
    Theme, Language, TechniqueRepresentation, PreparationPeriod
}

@Composable
fun UserPreferencesScreen(vm: UserPreferencesViewModel = hiltViewModel(), navigateUp: () -> Unit) {
    val userPreferences by vm.userPreferencesFlow.collectAsStateWithLifecycle(UserPreferences())

    var bottomSheetVisible by rememberSaveable { mutableStateOf(false) }
    val setBottomSheetVisibility = { value: Boolean -> bottomSheetVisible = value }

    var bottomSheetContent by rememberSaveable { mutableStateOf(BottomSheetContent.Theme) }
    val setBottomSheetContent = { value: BottomSheetContent -> bottomSheetContent = value }

    UserPreferencesScreen(
        dynamicColorsEnabled = userPreferences.dynamicColorsEnabled,
        currentThemeName = stringResource(userPreferences.theme.getNameAsStringRes()),
        currentLanguageName = stringResource(userPreferences.language.getNameAsStringRes()),
        currentTechniqueFormName = stringResource(userPreferences.techniqueRepresentationFormat.getNameAsStringRes()),
        currentPreparationPeriod = userPreferences.preparationPeriodSeconds,
        showQuittersData = userPreferences.showQuittersData,
        toggleDynamicColors = vm::toggleDynamicColors,
        updateQuittersData = vm::updateShowQuittersData,
        setBottomSheetVisibility = setBottomSheetVisibility,
        setBottomSheetContent = setBottomSheetContent,
        navigateUp = navigateUp
    )

    ModalBottomSheet(
        bottomSheetVisible = bottomSheetVisible, setBottomSheetVisibility = setBottomSheetVisibility
    ) {
        when (bottomSheetContent) {
            BottomSheetContent.Theme -> ThemeSelectionBottomSheet(
                initialTheme = userPreferences.theme,
                setBottomSheetVisibility = setBottomSheetVisibility,
                setNewTheme = vm::updateTheme,
            )

            BottomSheetContent.Language -> LanguageSelectionBottomSheet(
                initialLanguage = userPreferences.language,
                setBottomSheetVisibility = setBottomSheetVisibility,
                setNewLanguage = vm::updateLanguage,
            )

            BottomSheetContent.TechniqueRepresentation -> TechniqueFormSelectionBottomSheet(
                initialTechniqueRepresentationFormat = userPreferences.techniqueRepresentationFormat,
                setBottomSheetVisibility = setBottomSheetVisibility,
                setNewTechniqueForm = vm::updateTechniqueRepresentationForm,
            )

            BottomSheetContent.PreparationPeriod -> PreparationPeriodSelectionBottomSheet(
                initialDurationSeconds = userPreferences.preparationPeriodSeconds,
                setBottomSheetVisibility = setBottomSheetVisibility,
                setNewDuration = vm::updatePreparationDuration
            )
        }
    }
}

@Composable
private fun UserPreferencesScreen(
    dynamicColorsEnabled: Boolean,
    currentThemeName: String,
    currentLanguageName: String,
    currentTechniqueFormName: String,
    currentPreparationPeriod: Int,
    showQuittersData: Boolean,
    toggleDynamicColors: (Boolean) -> Unit,
    updateQuittersData: (Boolean) -> Unit,
    setBottomSheetVisibility: (Boolean) -> Unit,
    setBottomSheetContent: (BottomSheetContent) -> Unit,
    navigateUp: () -> Unit
) = Column(Modifier.fillMaxSize()) {
    DetailsItem(
        startText = stringResource(R.string.user_prefs_language), endText = currentLanguageName
    ) {
        setBottomSheetVisibility(true); setBottomSheetContent(BottomSheetContent.Language)
    }
    HorizontalDivider()

    DetailsItem(
        startText = stringResource(R.string.user_prefs_dynamic_colors),
        selected = dynamicColorsEnabled,
        onSelectionChange = toggleDynamicColors
    )
    HorizontalDivider()

    DetailsItem(startText = stringResource(R.string.user_prefs_theme), endText = currentThemeName) {
        setBottomSheetVisibility(true); setBottomSheetContent(BottomSheetContent.Theme)
    }
    HorizontalDivider()

    DetailsItem(
        startText = stringResource(R.string.user_prefs_technique_form),
        endText = currentTechniqueFormName
    ) {
        setBottomSheetVisibility(true)
        setBottomSheetContent(BottomSheetContent.TechniqueRepresentation)
    }
    HorizontalDivider()

    DetailsItem(
        startText = stringResource(R.string.user_prefs_preparation_period),
        endText = currentPreparationPeriod.toTime().asString()
    ) {
        setBottomSheetVisibility(true); setBottomSheetContent(BottomSheetContent.PreparationPeriod)
    }
    HorizontalDivider()

    DetailsItem(
        startText = stringResource(R.string.user_preferences_show_quitters_data),
        selected = showQuittersData,
        onSelectionChange = updateQuittersData
    )
    HorizontalDivider()

    Spacer(Modifier.weight(1F))

    DoneTextButton(
        setBottomSheetVisibility = setBottomSheetVisibility,
        onButtonClick = navigateUp,
        modifier = Modifier
            .padding(
                top = PaddingManager.Medium,
                bottom = PaddingManager.Large,
                end = PaddingManager.Large
            )
            .align(Alignment.End)
    )
}

@Composable
private fun LanguageSelectionBottomSheet(
    initialLanguage: Language,
    setBottomSheetVisibility: (Boolean) -> Unit,
    setNewLanguage: (Language) -> Unit
) = SingleButtonBottomSheetBox(setBottomSheetVisibility = setBottomSheetVisibility) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(initialLanguage.ordinal) }

    for (language in Language.entries) SelectableDetailsItem(
        text = stringResource(language.getNameAsStringRes()),
        selected = language.ordinal == selectedIndex
    ) { selectedIndex = language.ordinal; setNewLanguage(language); setNewLocale(language.tag) }
}

private fun setNewLocale(languageTag: String) {
    if (languageTag.isEmpty()) AppCompatDelegate.setApplicationLocales(LocaleListCompat.getDefault())
    else AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageTag))
}

@Composable
private fun ThemeSelectionBottomSheet(
    initialTheme: Theme, setBottomSheetVisibility: (Boolean) -> Unit, setNewTheme: (Theme) -> Unit
) = SingleButtonBottomSheetBox(setBottomSheetVisibility = setBottomSheetVisibility) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(initialTheme.ordinal) }

    for (theme in Theme.entries) SelectableDetailsItem(
        text = stringResource(theme.getNameAsStringRes()), selected = theme.ordinal == selectedIndex
    ) { selectedIndex = theme.ordinal; setNewTheme(theme) }
}


@Composable
private fun TechniqueFormSelectionBottomSheet(
    initialTechniqueRepresentationFormat: TechniqueRepresentationFormat,
    setBottomSheetVisibility: (Boolean) -> Unit,
    setNewTechniqueForm: (TechniqueRepresentationFormat) -> Unit
) = SingleButtonBottomSheetBox(setBottomSheetVisibility = setBottomSheetVisibility) {
    var selectedIndex by rememberSaveable {
        mutableIntStateOf(initialTechniqueRepresentationFormat.ordinal)
    }

    for (techniqueForm in TechniqueRepresentationFormat.entries) SelectableDetailsItem(
        text = stringResource(techniqueForm.getNameAsStringRes()),
        selected = techniqueForm.ordinal == selectedIndex
    ) { selectedIndex = techniqueForm.ordinal; setNewTechniqueForm(techniqueForm) }
}

@Composable
fun PreparationPeriodSelectionBottomSheet(
    initialDurationSeconds: Int,
    setBottomSheetVisibility: (Boolean) -> Unit,
    setNewDuration: (Int) -> Unit
) {
    var durationTime by rememberSaveable { mutableStateOf(initialDurationSeconds.toTime()) }

    DoubleButtonBottomSheetBox(
        setBottomSheetVisibility = setBottomSheetVisibility,
        onSaveButtonClick = { setNewDuration(durationTime.toSeconds()) }) {

        TimePicker(
            value = durationTime,
            onValueChange = { durationTime = it },
            minutesRange = (0..5).toList(),
            secondsRange = (1..59).toList(),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        SecondaryText(
            text = stringResource(R.string.user_prefs_preparation_period_helper_text),
            modifier = Modifier.padding(horizontal = PaddingManager.Medium)
        )
    }
}

private fun Theme.getNameAsStringRes(): Int = when (this) {
    Theme.UNSPECIFIED -> R.string.user_prefs_system_default
    Theme.LIGHT -> R.string.user_prefs_theme_light
    Theme.DARK -> R.string.user_prefs_theme_dark
}

private fun Language.getNameAsStringRes(): Int = when (this) {
    Language.UNSPECIFIED -> R.string.user_prefs_system_default
    Language.ENGLISH -> R.string.user_prefs_language_english
    Language.PERSIAN -> R.string.user_prefs_language_persian
//    Language.FRENCH -> R.string.user_prefs_language_french
//    Language.GERMAN -> R.string.user_prefs_language_german
}

private fun TechniqueRepresentationFormat.getNameAsStringRes(): Int = when (this) {
    TechniqueRepresentationFormat.UNSPECIFIED -> R.string.user_prefs_technique_form_unspecified
    TechniqueRepresentationFormat.NAME -> R.string.user_prefs_technique_form_name
    TechniqueRepresentationFormat.NUM -> R.string.user_prefs_technique_form_number
}