package com.example.android.strikingarts.ui.training

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.components.ConfirmDialog
import com.example.android.strikingarts.ui.components.PrimaryText
import com.example.android.strikingarts.ui.components.ProgressBar
import com.example.android.strikingarts.ui.components.util.SurviveProcessDeath
import com.example.android.strikingarts.ui.compositionlocal.LocalUserPreferences
import com.example.android.strikingarts.ui.mapper.getTechniqueRepresentation
import com.example.android.strikingarts.ui.model.Time
import com.example.android.strikingarts.ui.model.toTime
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.TypographyManager
import com.example.android.strikingarts.ui.util.toComposeColor

@Composable
fun TrainingScreen(
    vm: TrainingViewModel = hiltViewModel(),
    navigateToWinnersScreen: (id: Long) -> Unit,
    navigateToLosersScreen: (id: Long) -> Unit
) {
    val loadingScreen by vm.loadingScreen.collectAsStateWithLifecycle()

    var quitDialogVisible by rememberSaveable { mutableStateOf(false) }
    val setQuitDialogVisibility = { value: Boolean -> quitDialogVisible = value }

    BackHandler { vm.pause(); setQuitDialogVisibility(true) }

    if (loadingScreen) {
        vm.updateOnSessionComplete(navigateToWinnersScreen)
        vm.updatePreparationPeriod(LocalUserPreferences.current.preparationPeriodSeconds)

        ProgressBar()
    } else {
        val workoutListItem = vm.workoutListItem
        val isTimerRunning by vm.timerState.collectAsStateWithLifecycle()
        val currentTimeSeconds by vm.timerFlow.collectAsStateWithLifecycle()
        val currentRound by vm.currentRound.collectAsStateWithLifecycle()
        val currentCombo by vm.currentCombo.collectAsStateWithLifecycle()
        val currentComboIndex by vm.currentComboIndex.collectAsStateWithLifecycle()
        val currentColorString by vm.currentColor.collectAsStateWithLifecycle()

        val totalNumberOfCombos = workoutListItem.comboList.size

        val currentTechniqueForm = LocalUserPreferences.current.techniqueRepresentationFormat
        val currentComboText by remember {
            derivedStateOf { currentCombo.getTechniqueRepresentation(currentTechniqueForm) }
        }

        TrainingScreen(
            comboText = currentComboText,
            rounds = workoutListItem.rounds,
            currentRound = currentRound,
            totalNumberOfCombos = totalNumberOfCombos,
            currentComboIndex = currentComboIndex,
            time = currentTimeSeconds.toTime(),
            isTimerRunning = isTimerRunning.timerStatus.isTimerRunning(),
            pauseTimer = vm::pause,
            resumeTimer = vm::resume,
            setQuitDialogVisibility = setQuitDialogVisibility,
            backgroundColor = currentColorString.toComposeColor()
        )

        if (quitDialogVisible) ConfirmQuitDialog(
            setQuitDialogVisibility = setQuitDialogVisibility,
            navigateToLosersScreen = { navigateToLosersScreen(workoutListItem.id) },
            stopTimer = vm::stop,
            resumeTimer = vm::resume
        )

        SurviveProcessDeath(vm::onProcessDeath)
    }
}

@Composable
private fun ConfirmQuitDialog(
    setQuitDialogVisibility: (Boolean) -> Unit,
    navigateToLosersScreen: () -> Unit,
    stopTimer: () -> Unit,
    resumeTimer: () -> Unit
) = ConfirmDialog(titleId = stringResource(R.string.training_quit),
    textId = stringResource(R.string.training_confirm_dialog_quit),
    confirmButtonText = stringResource(R.string.training_quit),
    dismissButtonText = stringResource(R.string.training_continue),
    onConfirm = { setQuitDialogVisibility(false); stopTimer(); navigateToLosersScreen() },
    onDismiss = { setQuitDialogVisibility(false); resumeTimer() })

@Composable
private fun TrainingScreen(
    comboText: String,
    rounds: Int,
    currentRound: Int,
    totalNumberOfCombos: Int,
    currentComboIndex: Int,
    time: Time,
    isTimerRunning: Boolean,
    pauseTimer: () -> Unit,
    resumeTimer: () -> Unit,
    setQuitDialogVisibility: (Boolean) -> Unit,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) = Column(modifier.fillMaxSize()) {
    val surfaceColor = ColorManager.surface

    // ComboCounter
    PrimaryText(buildAnnotatedStringWithSlash(
        startText = "${if (currentComboIndex < totalNumberOfCombos) currentComboIndex + 1 else currentComboIndex}",
        endText = "$totalNumberOfCombos"
    ),
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind { drawRect(backgroundColor) }
            .padding(PaddingManager.Large))
    // ComboText
    Box(
        Modifier
            .fillMaxWidth()
            .weight(1F)
            .drawBehind { drawRect(backgroundColor) }
            .padding(
                horizontal = PaddingManager.Large
            )) {
        Text(
            text = comboText,
            style = TypographyManager.titleLarge.copy(fontWeight = FontWeight.Bold),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
    }

    // Separator
    Box(modifier = Modifier
        .drawBehind {
            drawRect(Brush.verticalGradient(listOf(backgroundColor, surfaceColor)))
        }
        .height(PaddingManager.XLarge)
        .fillMaxWidth())

    // TimerColumn
    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingManager.Large)
    ) {
        // TimerText
        Text(
            text = AnnotatedString(
                text = time.asString(), spanStyles = listOf(
                    AnnotatedString.Range(SpanStyle(color = ColorManager.primary), 3, 4)
                )
            ), style = TypographyManager.displaySmall.copy(
                fontWeight = FontWeight.Bold, fontSize = 64.sp
            ), modifier = Modifier.padding(bottom = PaddingManager.Small)
        )

        // RoundInfoText
        Text(
            text = buildAnnotatedString {
                append("$currentRound")
                withStyle(SpanStyle(color = ColorManager.primary)) { append(" / ") }
                append("$rounds")
            }, style = TypographyManager.displaySmall.copy(
                fontWeight = FontWeight.SemiBold, fontSize = 56.sp
            ), modifier = Modifier.padding(bottom = PaddingManager.Medium)
        )

        // ButtonRow
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = PaddingManager.Large),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { if (isTimerRunning) pauseTimer() else resumeTimer() }) {
                Text(
                    stringResource(if (isTimerRunning) R.string.all_pause else R.string.all_play).toUpperCase(
                        Locale.current
                    )
                )
            }
            Button(
                onClick = { pauseTimer(); setQuitDialogVisibility(true) },
                colors = ButtonDefaults.buttonColors(containerColor = ColorManager.error)
            ) { Text(text = stringResource(R.string.all_quit).toUpperCase(Locale.current)) }
        }
    }
}

@Composable
private fun buildAnnotatedStringWithSlash(
    startText: String, endText: String, slashColor: Color = ColorManager.primary
) = buildAnnotatedString {
    append(startText)
    withStyle(SpanStyle(color = slashColor)) { append(" / ") }
    append(endText)
}