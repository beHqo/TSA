package com.github.tsa.ui.losersscreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.tsa.R
import com.github.tsa.ui.components.PrimaryText
import com.github.tsa.ui.components.ProgressBar
import com.github.tsa.ui.theme.designsystemmanager.PaddingManager
import com.github.tsa.ui.theme.designsystemmanager.TypographyManager

@Composable
fun LosersScreen(navigateToHomeScreen: () -> Unit, vm: LosersViewModel = hiltViewModel()) {
    val loadingScreen by vm.loadingScreen.collectAsStateWithLifecycle()

    if (loadingScreen) ProgressBar() else LosersScreen(navigateToHomeScreen)
}

@Composable
private fun LosersScreen(navigateToHomeScreen: () -> Unit) = Column(
    Modifier
        .fillMaxSize()
        .padding(PaddingManager.Large),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
) {
    BackHandler(onBack = navigateToHomeScreen)

    Text(
        text = stringResource(R.string.loser_give_up),
        textAlign = TextAlign.Center,
        style = TypographyManager.titleMedium,
        modifier = Modifier.padding(top = PaddingManager.Large)
    )

    PrimaryText(text = stringResource(R.string.loser_habit))

    Button(
        onClick = navigateToHomeScreen,
        modifier = Modifier.padding(top = PaddingManager.XXLarge, bottom = PaddingManager.Large)
    ) { Text(text = stringResource(R.string.loser_button), textAlign = TextAlign.Center) }
}
