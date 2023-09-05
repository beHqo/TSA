package com.example.android.strikingarts.ui.winnersscreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.R
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.TechniqueCategory.DEFENSE
import com.example.android.strikingarts.domain.model.TechniqueCategory.OFFENSE
import com.example.android.strikingarts.domain.model.TechniqueListItem
import com.example.android.strikingarts.domain.model.toImmutableList
import com.example.android.strikingarts.ui.components.DoubleTextButtonRow
import com.example.android.strikingarts.ui.components.PrimaryText
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager

@Composable
fun WinnersScreen(
    winnersViewModel: WinnersViewModel = hiltViewModel(),
    navigateToHomeScreen: () -> Unit,
    navigateToWorkoutPreview: (id: Long) -> Unit
) {
    val workoutListItem by winnersViewModel.workoutListItem.collectAsStateWithLifecycle()

    val techniqueList = workoutListItem.comboList.flatMap { it.techniqueList }.toImmutableList()
    val numberOfStrikes = techniqueList.map { it.movementType == OFFENSE }.size
    val numberOfDefensiveTechniques = techniqueList.map { it.movementType == DEFENSE }.size
    val mostRepeatedTechnique = getTheMostRepeatedTechnique(techniqueList)

    BackHandler(onBack = navigateToHomeScreen)

    WinnersScreen(numberOfStrikes = numberOfStrikes,
        numberOfDefensiveTechniques = numberOfDefensiveTechniques,
        mostRepeatedTechnique = mostRepeatedTechnique,
        navigateToHomeScreen = navigateToHomeScreen,
        navigateToWorkoutPreview = { navigateToWorkoutPreview(workoutListItem.id) })
}

@Composable
private fun WinnersScreen(
    numberOfStrikes: Int,
    numberOfDefensiveTechniques: Int,
    mostRepeatedTechnique: String?,
    navigateToHomeScreen: () -> Unit,
    navigateToWorkoutPreview: () -> Unit
) = Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(PaddingManager.Large),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    PrimaryText(
        text = stringResource(R.string.winner_congratulations),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = PaddingManager.Large)
    )

    if (mostRepeatedTechnique != null) PrimaryText(
        text = stringResource(
            R.string.winner_details,
            numberOfStrikes,
            numberOfDefensiveTechniques,
            mostRepeatedTechnique
        ), textAlign = TextAlign.Center
    )

    Spacer(
        Modifier
            .weight(1F)
            .padding(vertical = PaddingManager.Large)
    )

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

private fun getTheMostRepeatedTechnique(techniqueList: ImmutableList<TechniqueListItem>): String? =
    techniqueList.maxByOrNull { it.id }?.name