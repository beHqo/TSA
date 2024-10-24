package com.example.android.strikingarts.ui.winnersscreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
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
import com.example.android.strikingarts.ui.model.Time
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.TypographyManager
import com.example.android.strikingarts.ui.util.localized

@Composable
fun WinnersScreen(
    vm: WinnersViewModel = hiltViewModel(),
    navigateToHomeScreen: () -> Unit,
    navigateToWorkoutPreview: (id: Long) -> Unit
) {
    BackHandler(onBack = navigateToHomeScreen)

    val loadingScreen by vm.loadingScreen.collectAsStateWithLifecycle()

    if (loadingScreen) ProgressBar() else {
        val workout = vm.workout

        val comboListSize = vm.comboListSize

        WinnersScreen(
            navigateToHomeScreen = navigateToHomeScreen,
            navigateToWorkoutPreview = { navigateToWorkoutPreview(workout.id) }) {
            if (comboListSize > 0) {
                val numberOfStrikes = vm.numberOfStrikes
                val numberOfDefensiveTechniques = vm.numberOfDefensiveTechniques
                val mostRepeatedTechniqueOrEmpty = vm.mostRepeatedTechniqueOrEmpty

                WorkoutDetails(
                    numberOfStrikes, numberOfDefensiveTechniques, mostRepeatedTechniqueOrEmpty
                )
            } else {
                NoComboWorkoutDetails(
                    workoutTime = vm.workoutTime, totalRounds = workout.rounds
                )
            }
        }

    }
}

@Composable
private fun WinnersScreen(
    navigateToHomeScreen: () -> Unit,
    navigateToWorkoutPreview: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) = Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(PaddingManager.Large),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
) {
    Text(
        text = stringResource(R.string.winners_congrats),
        style = TypographyManager.headlineLarge,
        color = ColorManager.onSurface,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.weight(1F))

    content()

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

@Composable
private fun WorkoutDetails(
    numberOfStrikes: Int, numberOfDefensiveTechniques: Int, mostRepeatedTechniqueOrEmpty: String
) {
    DetailsItem(
        startText = stringResource(R.string.winners_strikes_sum),
        endText = numberOfStrikes.localized()
    )
    HorizontalDivider()

    DetailsItem(
        startText = stringResource(R.string.winners_defense_techniques_sum),
        endText = numberOfDefensiveTechniques.localized()
    )
    HorizontalDivider()

    DetailsItem(
        startText = stringResource(R.string.winners_most_repeated_technique),
        endText = mostRepeatedTechniqueOrEmpty
    )
}

@Composable
private fun NoComboWorkoutDetails(workoutTime: Time, totalRounds: Int) {
    DetailsItem(
        startText = stringResource(R.string.winners_total_rounds),
        endText = totalRounds.localized()
    )
    HorizontalDivider()
    DetailsItem(
        startText = stringResource(R.string.winners_total_workout_time),
        endText = workoutTime.asString()
    )
}