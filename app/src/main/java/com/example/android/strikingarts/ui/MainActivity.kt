package com.example.android.strikingarts.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.domain.model.Theme
import com.example.android.strikingarts.domain.model.UserPreferences
import com.example.android.strikingarts.domain.usecase.userpreferences.RetrieveUserPreferencesFlowUseCase
import com.example.android.strikingarts.ui.compositionlocal.LocalUserPreferences
import com.example.android.strikingarts.ui.theme.StrikingArtsTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var retrieveUserPreferencesFlowUseCase: RetrieveUserPreferencesFlowUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        setContent {
            val userPreferences by retrieveUserPreferencesFlowUseCase.data.collectAsStateWithLifecycle(
                initialValue = UserPreferences()
            )

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

            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

            StrikingArtsTheme(
                dynamicColor = userPreferences.dynamicColorsEnabled, darkTheme = isDarkThemeEnabled
            ) {
                windowInsetsController.isAppearanceLightStatusBars = !isDarkThemeEnabled
                windowInsetsController.isAppearanceLightNavigationBars = !isDarkThemeEnabled

                CompositionLocalProvider(LocalUserPreferences provides userPreferences) {
                    StrikingArtsApp()
                }
            }
        }
    }
}