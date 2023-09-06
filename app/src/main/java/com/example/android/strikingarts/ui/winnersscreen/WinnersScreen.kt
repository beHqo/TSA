package com.example.android.strikingarts.ui.winnersscreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.components.DoubleTextButtonRow
import com.example.android.strikingarts.ui.components.ProgressBar
import com.example.android.strikingarts.ui.components.detailsitem.DetailsItem
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.TypographyManager

@Composable
fun WinnersScreen(
    winnersViewModel: WinnersViewModel = hiltViewModel(),
    navigateToHomeScreen: () -> Unit,
    navigateToWorkoutPreview: (id: Long) -> Unit
) {
    val loadingScreen by winnersViewModel.loadingScreen.collectAsStateWithLifecycle()

    if (loadingScreen) ProgressBar() else {
        val workoutListItem by winnersViewModel.workoutListItem.collectAsStateWithLifecycle()

        val comboListSize = winnersViewModel.comboListSize
        val numberOfStrikes = winnersViewModel.numberOfStrikes
        val numberOfDefensiveTechniques = winnersViewModel.numberOfDefensiveTechniques
        val mostRepeatedTechniqueOrEmpty = winnersViewModel.mostRepeatedTechniqueOrEmpty

        BackHandler(onBack = navigateToHomeScreen)

        WinnersScreen(comboListSize = comboListSize,
            numberOfStrikes = numberOfStrikes,
            numberOfDefensiveTechniques = numberOfDefensiveTechniques,
            mostRepeatedTechnique = mostRepeatedTechniqueOrEmpty,
            navigateToHomeScreen = navigateToHomeScreen,
            navigateToWorkoutPreview = { navigateToWorkoutPreview(workoutListItem.id) })
    }
}

@Composable
private fun WinnersScreen(
    comboListSize: Int,
    numberOfStrikes: Int,
    numberOfDefensiveTechniques: Int,
    mostRepeatedTechnique: String,
    navigateToHomeScreen: () -> Unit,
    navigateToWorkoutPreview: () -> Unit
) = Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(PaddingManager.Large),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
) {
    Text(
        text = stringResource(R.string.winners_congratz),
        style = TypographyManager.headlineLarge,
        color = ColorManager.onSurface,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.weight(1F))

    if (comboListSize > 0) {
        DetailsItem(
            startText = stringResource(R.string.winners_strikes_sum),
            endText = "$numberOfStrikes"
        )
        DetailsItem(
            startText = stringResource(R.string.winners_defense_techniques_sum),
            endText = "$numberOfDefensiveTechniques"
        )
        DetailsItem(
            startText = stringResource(R.string.winners_most_repeated_technique),
            endText = mostRepeatedTechnique
        )
    }

    Spacer(Modifier.weight(1F))

    DoubleTextButtonRow(
        modifier = Modifier.fillMaxWidth(),
        leftButtonText = stringResource(R.string.winner_done),
        rightButtonText = stringResource(R.string.winner_perform_again),
        leftButtonEnabled = true,
        rightButtonEnabled = true,
        onLeftButtonClick = navigateToHomeScreen,
        onRightButtonClick = navigateToWorkoutPreview
    )
}