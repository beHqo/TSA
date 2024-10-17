package com.example.android.strikingarts.ui.main

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.LocaleListCompat
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.domain.model.Language
import com.example.android.strikingarts.domain.model.Theme
import com.example.android.strikingarts.ui.StrikingArtsApp
import com.example.android.strikingarts.ui.compositionlocal.LocalUserPreferences
import com.example.android.strikingarts.ui.theme.StrikingArtsTheme
import com.example.android.strikingarts.ui.util.findActivity

@Composable
fun MainComposable(vm: MainViewModel = hiltViewModel()) {
    val userPreferences by vm.userPreferences.collectAsStateWithLifecycle()
    LocalUserPreferences = staticCompositionLocalOf { userPreferences }

    val isSystemInDarkTheme = isSystemInDarkTheme()
    val isDarkThemeEnabled by remember {
        derivedStateOf {
            when (userPreferences.theme) {
                Theme.DARK -> true
                Theme.LIGHT -> false
                Theme.UNSPECIFIED -> isSystemInDarkTheme
            }
        }
    }

    val language = userPreferences.language
    if (language == Language.UNSPECIFIED) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(Language.ENGLISH.tag))
        }
    } else AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language.tag))

    val activity = LocalContext.current.findActivity()
    if (activity != null) {
        val window = activity.window

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        windowInsetsController.isAppearanceLightStatusBars = !isDarkThemeEnabled
        windowInsetsController.isAppearanceLightNavigationBars = !isDarkThemeEnabled
    }

    StrikingArtsTheme(
        dynamicColor = userPreferences.dynamicColorsEnabled, darkTheme = isDarkThemeEnabled
    ) {
        CompositionLocalProvider(LocalUserPreferences provides userPreferences) {
            StrikingArtsApp()
        }
    }
}
