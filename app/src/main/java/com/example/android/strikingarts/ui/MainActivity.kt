package com.example.android.strikingarts.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.example.android.strikingarts.ui.theme.StrikingArtsTheme
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.ElevationManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

            StrikingArtsTheme {

                window.navigationBarColor =
                    ColorManager.surfaceColorAtElevation(ElevationManager.Level2).toArgb()
                window.statusBarColor = ColorManager.surface.toArgb()

                if (!isSystemInDarkTheme()) {
                    windowInsetsController.isAppearanceLightNavigationBars = true
                    windowInsetsController.isAppearanceLightStatusBars = true
                }

                StrikingArtsApp()
            }
        }
    }
}
