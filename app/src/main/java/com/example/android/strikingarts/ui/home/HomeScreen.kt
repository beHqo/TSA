package com.example.android.strikingarts.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.R
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.WeekDay
import com.example.android.strikingarts.ui.components.MoreVertIconButton
import com.example.android.strikingarts.ui.components.ProgressBar
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.ContentAlphaManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.ElevationManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.TypographyManager

@Composable
fun HomeScreen(
    onProfileClick: () -> Unit,
    navigateToUserPreferencesScreen: () -> Unit,
    navigateToAboutScreen: () -> Unit,
    navigateToHelpScreen: () -> Unit,
    openRateAppDialog: () -> Unit,
    navigateToWorkoutPreviewScreen: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val loadingScreen by viewModel.loadingScreen.collectAsStateWithLifecycle()

    if (loadingScreen) ProgressBar() else {
        val weekDays = viewModel.weekDays
        val lastExecutedWorkoutName = viewModel.lastExecutedWorkoutName
        val elapsedDaysSinceLastExecutedWorkout = viewModel.elapsedDaysSinceLastExecutedWorkout
        val lastExecutedWorkoutId = viewModel.lastExecutedWorkoutId

        val lastExecutedWorkoutDateDisplayName = when (elapsedDaysSinceLastExecutedWorkout) {
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
                id = R.plurals.home_over_x_week_ago, count = 2, 1
            )

            in 22..30L -> pluralStringResource(
                id = R.plurals.home_over_x_week_ago, count = 3, 1
            )

            else -> viewModel.lastExecutedWorkoutDisplayNameForDate
        }

        HomeScreen(weekDays = weekDays,
            lastExecutedWorkoutName = lastExecutedWorkoutName,
            lastExecutedWorkoutDateDisplayName = lastExecutedWorkoutDateDisplayName,
            onProfileClick = onProfileClick,
            navigateToSettingScreen = navigateToUserPreferencesScreen,
            navigateToAboutScreen = navigateToAboutScreen,
            navigateToHelpScreen = navigateToHelpScreen,
            openRateAppDialog = openRateAppDialog,
            navigateToWorkoutPreviewScreen = { navigateToWorkoutPreviewScreen(lastExecutedWorkoutId) })
    }
}

@Composable
private fun HomeScreen(
    weekDays: ImmutableList<WeekDay>,
    lastExecutedWorkoutName: String,
    lastExecutedWorkoutDateDisplayName: String,
    onProfileClick: () -> Unit,
    navigateToSettingScreen: () -> Unit,
    navigateToAboutScreen: () -> Unit,
    navigateToHelpScreen: () -> Unit,
    openRateAppDialog: () -> Unit,
    navigateToWorkoutPreviewScreen: () -> Unit
) = Column(
    Modifier
        .fillMaxSize()
        .verticalScroll(state = rememberScrollState())
) {
    HomeScreenTopAppBar(
        onProfileClick,
        navigateToSettingScreen,
        navigateToAboutScreen,
        navigateToHelpScreen,
        openRateAppDialog
    )

    TrainingWeekGrid(weekDays)

    LastWorkoutSummary(
        lastExecutedWorkoutName, lastExecutedWorkoutDateDisplayName, navigateToWorkoutPreviewScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenTopAppBar(
    onProfileClick: () -> Unit,
    navigateToSettingScreen: () -> Unit,
    navigateToAboutScreen: () -> Unit,
    navigateToHelpScreen: () -> Unit,
    openRateAppDialog: () -> Unit
) = TopAppBar(
    colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorManager.primaryContainer),
    navigationIcon = {
        IconButton(onClick = onProfileClick) {
            Icon(Icons.Sharp.Person, stringResource(R.string.home_profile_Icon_button_desc))
        }
    },
    title = { Text(text = stringResource(R.string.all_home), maxLines = 1) },
    actions = {
        HomeScreenMoreVertDropdownMenu(
            navigateToSettingScreen, navigateToAboutScreen, navigateToHelpScreen, openRateAppDialog
        )
    },
    windowInsets = WindowInsets(0) // Force TopAppBar to be single-columned
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
private fun TrainingWeekGrid(weekDays: ImmutableList<WeekDay>) = Row(
    modifier = Modifier.horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.SpaceEvenly
) {
    for ((weekDayDisplayName, dateDisplayName, isTrainingDay) in weekDays) Box(
        modifier = Modifier.trainingWeekModifier()
    ) {
        TrainingWeekText(weekDayDisplayName, isTrainingDay, Modifier.align(Alignment.TopStart))
        TrainingWeekText(dateDisplayName, isTrainingDay, Modifier.align(Alignment.BottomEnd))
    }
}

@Composable
private fun Modifier.trainingWeekModifier() = this
    .padding(PaddingManager.Small)
    .width(TrainingDayCellWidthDp)
    .height(TrainingDayCellHeightDp)
    .background(ColorManager.secondaryContainer)
    .shadow(
        elevation = ElevationManager.Level2,
        spotColor = ColorManager.primary,
        ambientColor = ColorManager.primary,
        shape = RectangleShape
    )
    .padding(PaddingManager.Medium)

@Composable
private fun TrainingWeekText(text: String, isTrainingDay: Boolean, modifier: Modifier) = Text(
    text = text,
    color = if (isTrainingDay) ColorManager.onSecondaryContainer else ColorManager.onSurface,
    fontWeight = if (isTrainingDay) FontWeight.W600 else null,
    fontStyle = if (isTrainingDay) FontStyle.Italic else null,
    maxLines = 1,
    textAlign = TextAlign.Center,
    modifier = modifier
)

@Composable
private fun LastWorkoutSummary(
    lastExecutedWorkoutName: String,
    lastExecutedWorkoutDateDisplayName: String,
    navigateToWorkoutPreviewScreen: () -> Unit
) = Column(
    Modifier
        .padding(PaddingManager.Small)
        .background(ColorManager.secondaryContainer)
        .shadow(
            elevation = ElevationManager.Level2,
            spotColor = ColorManager.primary,
            ambientColor = ColorManager.primary,
            shape = RectangleShape
        )
        .clickable(onClick = navigateToWorkoutPreviewScreen)
        .padding(horizontal = PaddingManager.Medium)
) {
    Text(buildAnnotatedString {
        withStyle(
            WorkoutSummarySpanStyle.copy(
                color = ColorManager.onSurface.copy(ContentAlphaManager.medium),
                baselineShift = BaselineShift.Subscript
            )
        ) {
            appendLine(stringResource(R.string.home_most_recently_performed_workout))
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
    })
}

private val WorkoutSummarySpanStyle: SpanStyle
    @Composable get() = TypographyManager.bodySmall.copy().toSpanStyle()

private val TrainingDayCellWidthDp = 72.dp
private val TrainingDayCellHeightDp = 88.dp