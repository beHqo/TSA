package com.example.android.strikingarts.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.R
import com.example.android.strikingarts.domain.model.TrainingWeekDay
import com.example.android.strikingarts.ui.components.MoreVertIconButton
import com.example.android.strikingarts.ui.components.ProgressBar
import com.example.android.strikingarts.ui.compositionlocal.LocalUserPreferences
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.ContentAlphaManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.ElevationManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.ShapeManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.SizeManager.TrainingDayCellHeight
import com.example.android.strikingarts.ui.theme.designsystemmanager.SizeManager.TrainingDayCellWidth
import com.example.android.strikingarts.ui.theme.designsystemmanager.TypographyManager

@Composable
fun HomeScreen(
    onProfileClick: () -> Unit,
    navigateToUserPreferencesScreen: () -> Unit,
    navigateToAboutScreen: () -> Unit,
    navigateToHelpScreen: () -> Unit,
    openRateAppDialog: () -> Unit,
    navigateToWorkoutPreviewScreen: (Long) -> Unit,
    vm: HomeViewModel = hiltViewModel()
) {
    val loadingScreen by vm.loadingScreen.collectAsStateWithLifecycle()

    if (loadingScreen) ProgressBar() else {
        val isUserNew = vm.isUserNew

        HomeScreen(isUserNew = isUserNew, topAppBar = {
            HomeScreenTopAppBar(
                onProfileClick,
                navigateToUserPreferencesScreen,
                navigateToAboutScreen,
                navigateToHelpScreen,
                openRateAppDialog
            )
        }, emptyScreen = { EmptyScreen() }, content = {
            val weekDays = vm.weekDays

            val lastSuccessFullWorkout = vm.lastSuccessFullWorkout
            val elapsedDaysSinceLastSuccessfulWorkout = vm.elapsedDaysSinceLastSuccessfulWorkout
            val lastSuccessfulWorkoutDisplayNameForDate = vm.lastSuccessfulWorkoutDisplayNameForDate

            val lastFailedWorkout = vm.lastFailedWorkout
            val elapsedDaysSinceLastFailedWorkout = vm.elapsedDaysSinceLastFailedWorkout
            val lastFailedWorkoutDisplayNameForDate = vm.lastFailedWorkoutDisplayNameForDate

            val lastSuccessfulWorkoutElapsedDateDisplayName = getElapsedDateDisplayName(
                elapsedDaysSinceLastSuccessfulWorkout, lastSuccessfulWorkoutDisplayNameForDate
            )

            val lastFailedWorkoutElapsedDateDisplayName = getElapsedDateDisplayName(
                elapsedDaysSinceLastFailedWorkout, lastFailedWorkoutDisplayNameForDate
            )

            HomeScreenContent(weekDays = weekDays,
                lastSuccessfulWorkoutName = lastSuccessFullWorkout.workoutName,
                lastSuccessfulWorkoutDateDisplayName = lastSuccessfulWorkoutElapsedDateDisplayName,
                lastFailedWorkoutName = lastFailedWorkout.workoutName,
                lastFailedWorkoutDateDisplayName = lastFailedWorkoutElapsedDateDisplayName,
                navigateToSuccessfulWorkoutPreviewScreen = {
                    navigateToWorkoutPreviewScreen(lastSuccessFullWorkout.workoutId)
                },
                navigateToFailedWorkoutPreviewScreen = {
                    navigateToWorkoutPreviewScreen(lastFailedWorkout.workoutId)
                })
        })
    }
}

@Composable
private fun HomeScreen(
    isUserNew: Boolean,
    topAppBar: @Composable () -> Unit,
    emptyScreen: @Composable () -> Unit,
    content: @Composable () -> Unit
) = Column(
    Modifier
        .fillMaxSize()
        .verticalScroll(state = rememberScrollState())
) {
    topAppBar()

    if (isUserNew) {
        Spacer(Modifier.weight(1F))
        emptyScreen()
        Spacer(Modifier.weight(1F))
    } else content()
}

@Composable
private fun HomeScreenContent(
    weekDays: List<TrainingWeekDay>,
    lastSuccessfulWorkoutName: String,
    lastSuccessfulWorkoutDateDisplayName: String,
    lastFailedWorkoutName: String,
    lastFailedWorkoutDateDisplayName: String,
    navigateToSuccessfulWorkoutPreviewScreen: () -> Unit,
    navigateToFailedWorkoutPreviewScreen: () -> Unit,
) {
    TrainingWeekGrid(weekDays)

    LastWorkoutSummary(
        stringResource(R.string.home_most_recently_performed_workout),
        lastSuccessfulWorkoutName,
        lastSuccessfulWorkoutDateDisplayName,
        navigateToSuccessfulWorkoutPreviewScreen
    )

    if (LocalUserPreferences.current.showQuittersData && lastFailedWorkoutName.isNotEmpty()) LastWorkoutSummary(
        stringResource(R.string.home_last_unfinished_workout),
        lastExecutedWorkoutName = lastFailedWorkoutName,
        lastExecutedWorkoutDateDisplayName = lastFailedWorkoutDateDisplayName,
        navigateToFailedWorkoutPreviewScreen
    )
}

@Composable
private fun EmptyScreen() = Box(
    Modifier.fillMaxSize(), contentAlignment = Alignment.Center
) {
    Text(
        text = stringResource(R.string.home_screen_empty_text),
        color = ColorManager.onPrimaryContainer,
        style = TypographyManager.titleMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingManager.Large)
            .shadow(elevation = ElevationManager.Level3, shape = ShapeManager.Medium)
            .clip(ShapeManager.Medium)
            .background(ColorManager.primaryContainer)
            .padding(PaddingManager.Large)
    )
}

@Composable
private fun getElapsedDateDisplayName(
    elapsedDaysSinceLastFailedWorkout: Long, lastSuccessfulWorkoutDisplayNameForDate: String
) = when (elapsedDaysSinceLastFailedWorkout) {
    0L -> stringResource(R.string.home_today)
    1L -> stringResource(R.string.home_yesterday)
    2L -> stringResource(R.string.home_x_days_ago, 2)
    3L -> stringResource(R.string.home_x_days_ago, 3)
    4L -> stringResource(R.string.home_x_days_ago, 4)
    5L -> stringResource(R.string.home_x_days_ago, 5)
    6L -> stringResource(R.string.home_x_days_ago, 6)
    7L -> pluralStringResource(id = R.plurals.home_x_week_ago, count = 1)
    14L -> pluralStringResource(id = R.plurals.home_x_week_ago, count = 2, 2)
    21L -> pluralStringResource(id = R.plurals.home_x_week_ago, count = 3, 3)

    in 7..13L -> pluralStringResource(
        id = R.plurals.home_over_x_week_ago, count = 1, 1
    )

    in 14..20L -> pluralStringResource(
        id = R.plurals.home_over_x_week_ago, count = 2, 2
    )

    in 22..30L -> pluralStringResource(
        id = R.plurals.home_over_x_week_ago, count = 3, 3
    )

    else -> lastSuccessfulWorkoutDisplayNameForDate
}

@OptIn(ExperimentalMaterial3Api::class) //TopAppBar
@Composable
private fun HomeScreenTopAppBar(
    onProfileClick: () -> Unit,
    navigateToSettingScreen: () -> Unit,
    navigateToAboutScreen: () -> Unit,
    navigateToHelpScreen: () -> Unit,
    openRateAppDialog: () -> Unit
) = TopAppBar(navigationIcon = {
    IconButton(onClick = onProfileClick) {
        Icon(Icons.Rounded.Person, stringResource(R.string.home_profile_icon_button_desc))
    }
}, title = { Text(text = stringResource(R.string.all_home), maxLines = 1) }, actions = {
    HomeScreenMoreVertDropdownMenu(
        navigateToSettingScreen, navigateToAboutScreen, navigateToHelpScreen, openRateAppDialog
    )
}, windowInsets = WindowInsets(0) // Force TopAppBar to be single-columned
)

@Composable
private fun HomeScreenMoreVertDropdownMenu(
    navigateToSettingScreen: () -> Unit,
    navigateToAboutScreen: () -> Unit,
    navigateToHelpScreen: () -> Unit,
    openRateAppDialog: () -> Unit
) = Box(Modifier.padding(end = PaddingManager.Medium)) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    MoreVertIconButton { expanded = true }

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.home_settings)) },
            onClick = { expanded = false; navigateToSettingScreen() })

        DropdownMenuItem(
            text = { Text(stringResource(R.string.home_about)) },
            onClick = { expanded = false; navigateToAboutScreen() })

        DropdownMenuItem(
            text = { Text(stringResource(R.string.home_help)) },
            onClick = { expanded = false; navigateToHelpScreen() })

        DropdownMenuItem(
            text = { Text(stringResource(R.string.home_rate)) },
            onClick = { expanded = false; openRateAppDialog() })
    }
}

@Composable
private fun TrainingWeekGrid(trainingWeekDays: List<TrainingWeekDay>) = Row(
    modifier = Modifier.horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.SpaceEvenly
) {
    for (trainingWeekDay in trainingWeekDays) Box(modifier = Modifier.trainingWeekModifier()) {
        WeekDayText(
            text = trainingWeekDay.weekDayDisplayName,
            isTrainingDay = trainingWeekDay.isTrainingDay,
            isWorkoutAborted = trainingWeekDay.userQuitMidWorkout,
            modifier = Modifier.align(Alignment.TopStart)
        )
        WeekDayText(
            text = trainingWeekDay.dateDisplayName,
            isTrainingDay = trainingWeekDay.isTrainingDay,
            isWorkoutAborted = trainingWeekDay.userQuitMidWorkout,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

@Composable
private fun Modifier.trainingWeekModifier() = this
    .padding(PaddingManager.Medium)
    .width(TrainingDayCellWidth)
    .height(TrainingDayCellHeight)
    .shadow(elevation = ElevationManager.Level3, shape = ShapeManager.Medium)
    .clip(ShapeManager.Medium)
    .background(ColorManager.primaryContainer)
    .padding(PaddingManager.Medium)

@Composable
private fun WeekDayText(
    text: String, isTrainingDay: Boolean, isWorkoutAborted: Boolean, modifier: Modifier
) {
    val showQuittersData = LocalUserPreferences.current.showQuittersData
    val userQuitMidWorkout = remember { showQuittersData && isWorkoutAborted }

    when {
        isTrainingDay && !userQuitMidWorkout || isTrainingDay && !showQuittersData -> TrainingDayText(
            text, modifier
        )

        userQuitMidWorkout -> QuittersDayText(text, modifier)
        else -> NormalDayText(text, modifier)
    }
}

@Composable
private fun NormalDayText(text: String, modifier: Modifier) = Text(
    text = text,
    color = ColorManager.onPrimaryContainer,
    maxLines = 1,
    textAlign = TextAlign.Center,
    modifier = modifier
)

@Composable
private fun TrainingDayText(text: String, modifier: Modifier) = Text(
    text = text,
    color = ColorManager.onPrimaryContainer,
    fontWeight = FontWeight.W600,
    fontStyle = FontStyle.Italic,
    maxLines = 1,
    textAlign = TextAlign.Center,
    modifier = modifier
)

@Composable
private fun QuittersDayText(text: String, modifier: Modifier) = Text(
    text = text,
    color = ColorManager.error,
    fontWeight = FontWeight.W600,
    fontStyle = FontStyle.Italic,
    maxLines = 1,
    textAlign = TextAlign.Center,
    modifier = modifier
)

@Composable
private fun LastWorkoutSummary(
    helperText: String,
    lastExecutedWorkoutName: String,
    lastExecutedWorkoutDateDisplayName: String,
    navigateToWorkoutPreviewScreen: () -> Unit
) = Text(
    buildAnnotatedString {
        withStyle(
            WorkoutSummarySpanStyle.copy(
                color = ColorManager.onSurface.copy(ContentAlphaManager.medium),
                baselineShift = BaselineShift.Subscript
            )
        ) {
            appendLine(helperText)
        }

        withStyle(TypographyManager.titleMedium.toSpanStyle()) {
            appendLine(lastExecutedWorkoutName)
        }

        withStyle(WorkoutSummarySpanStyle.copy(baselineShift = BaselineShift.Superscript)) {
            append(
                stringResource(
                    R.string.home_last_time_performed, lastExecutedWorkoutDateDisplayName
                )
            )
        }
    },
    color = ColorManager.onSecondaryContainer,
    modifier = Modifier
        .fillMaxWidth()
        .padding(PaddingManager.Medium)
        .shadow(elevation = ElevationManager.Level2, shape = ShapeManager.Medium)
        .clip(ShapeManager.Medium)
        .background(ColorManager.secondaryContainer)
        .clickable(onClick = navigateToWorkoutPreviewScreen)
        .padding(horizontal = PaddingManager.Medium)
        .padding(top = PaddingManager.Medium) // to center the entire text vertically
)

private val WorkoutSummarySpanStyle: SpanStyle
    @Composable get() = TypographyManager.bodySmall.copy().toSpanStyle()
