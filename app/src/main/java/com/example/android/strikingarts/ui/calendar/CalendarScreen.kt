package com.example.android.strikingarts.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.icons.sharp.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.R
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.toImmutableList
import com.example.android.strikingarts.ui.components.BackgroundDimmer
import com.example.android.strikingarts.ui.components.PrimaryText
import com.example.android.strikingarts.ui.components.util.SurviveProcessDeath
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager

private enum class CalendarScreenContentType { WEEK_DAY_NAME, EMPTY_SPACE, DATE }

private val MonthRowHeightDp = 56.dp

@Composable
fun CalendarScreen(
    model: CalendarViewModel = hiltViewModel(), navigateToWorkoutPreview: (Long) -> Unit
) {
    val weekDays = model.weekDays
    val month by model.month.collectAsStateWithLifecycle()
    val trainingDatesByMonthMap by model.trainingDatesByMonthMap.collectAsStateWithLifecycle()
    val workoutNames by model.workoutNames.collectAsStateWithLifecycle()

    val numberOfEmptyGridCells by remember {
        derivedStateOf {
            val firstDay = month.firstDay.dayOfWeek
            if (firstDay == 7) 0 else firstDay
        }
    }

    var workoutPreviewCardVisible by rememberSaveable { mutableStateOf(false) }
    val setWorkoutPreviewCardVisibility = { value: Boolean -> workoutPreviewCardVisible = value }

    if (workoutPreviewCardVisible) WorkoutPreviewDialog(
        workoutNames = workoutNames,
        onDismissDialog = setWorkoutPreviewCardVisibility,
        onClick = { navigateToWorkoutPreview(it) })

    BackgroundDimmer(
        visible = workoutPreviewCardVisible, setVisibility = setWorkoutPreviewCardVisibility
    )

    CalendarScreen(yearMonth = month.name,
        weekDays = weekDays,
        lastDayOfMonth = month.lastDay.dayOfMonth,
        firstDayOfMonthEpochDay = month.firstDay.epochDay,
        numberOfEmptyGridCells = numberOfEmptyGridCells,
        isDateTrainingDay = { trainingDatesByMonthMap[it] != null },
        getNextMonth = model::getNextMonth,
        getPreviousMonth = model::getPreviousMonth,
        setWorkoutPreviewCardVisibility = setWorkoutPreviewCardVisibility,
        retrieveWorkoutNames = { model.retrieveWorkoutNames(trainingDatesByMonthMap[it]) })

    SurviveProcessDeath(onStop = model::surviveProcessDeath)
}

@Composable
private fun CalendarScreen(
    yearMonth: String,
    weekDays: ImmutableList<String>,
    firstDayOfMonthEpochDay: Long,
    lastDayOfMonth: Int,
    numberOfEmptyGridCells: Int,
    isDateTrainingDay: (Long) -> Boolean,
    getNextMonth: () -> Unit,
    getPreviousMonth: () -> Unit,
    setWorkoutPreviewCardVisibility: (Boolean) -> Unit,
    retrieveWorkoutNames: (Long) -> Unit
) = Column(Modifier.fillMaxSize()) {
    MonthRow(
        yearMonth = yearMonth, getNextMonth = getNextMonth, getPreviousMonth = getPreviousMonth
    )

    CalendarGrid(
        weekDays = weekDays,
        firstDayOfMonthEpochDay = firstDayOfMonthEpochDay,
        lastDayOfMonth = lastDayOfMonth,
        numberOfEmptyGridCells = numberOfEmptyGridCells,
        isDateTrainingDay = isDateTrainingDay,
        setWorkoutPreviewCardVisibility = setWorkoutPreviewCardVisibility,
        retrieveWorkoutNames = retrieveWorkoutNames
    )
}

@Composable
private fun MonthRow(yearMonth: String, getNextMonth: () -> Unit, getPreviousMonth: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(MonthRowHeightDp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = getPreviousMonth) {
            Icon(Icons.Sharp.ArrowBack, stringResource(R.string.calendar_previous_month))
        }

        PrimaryText(text = yearMonth)

        IconButton(onClick = getNextMonth) {
            Icon(
                Icons.Sharp.ArrowForward, stringResource(R.string.calendar_next_month)
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    weekDays: ImmutableList<String>,
    firstDayOfMonthEpochDay: Long,
    lastDayOfMonth: Int,
    numberOfEmptyGridCells: Int,
    isDateTrainingDay: (Long) -> Boolean,
    setWorkoutPreviewCardVisibility: (Boolean) -> Unit,
    retrieveWorkoutNames: (Long) -> Unit
) {
    val onSurfaceColor = ColorManager.onSurface

    LazyVerticalGrid(columns = GridCells.Fixed(7)) {
        itemsIndexed(items = weekDays,
            key = { _, weekDay -> weekDay },
            contentType = { _, _ -> CalendarScreenContentType.WEEK_DAY_NAME }) { index, weekDay ->
            WeekDayGridCell(
                modifier = if (index == 0) Modifier.drawDividers(onSurfaceColor) else Modifier,
                name = weekDay
            )
        }

        items(
            count = numberOfEmptyGridCells,
            contentType = { CalendarScreenContentType.EMPTY_SPACE }) {
            Box(modifier = Modifier.aspectRatio(1F))
        }

        itemsIndexed(items = (1..lastDayOfMonth).toImmutableList(),
            key = { index, _ -> index },
            contentType = { _, _ -> CalendarScreenContentType.DATE }) { index, item ->
            val currentEpochDay = firstDayOfMonthEpochDay + index

            WeekDayGridCell(
                name = item.toString(),
                onClickEnabled = isDateTrainingDay(currentEpochDay),
                onClick = {
                    setWorkoutPreviewCardVisibility(true); retrieveWorkoutNames(currentEpochDay)
                })
        }
    }
}

@Composable
private fun WeekDayGridCell(
    modifier: Modifier = Modifier,
    name: String,
    onClickEnabled: Boolean = false,
    onClick: () -> Unit = {}
) = Box(modifier
    .aspectRatio(1F)
    .then(if (onClickEnabled) Modifier
        .background(ColorManager.primary)
        .clickable { onClick() } else Modifier)) {
    Text(
        text = name,
        maxLines = 1,
        color = if (onClickEnabled) ColorManager.onPrimary else Color.Unspecified,
        modifier = Modifier
            .align(Alignment.TopStart)
            .padding(start = PaddingManager.Medium, top = PaddingManager.Small)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkoutPreviewDialog(
    workoutNames: ImmutableList<Pair<Long, String>>,
    onDismissDialog: (Boolean) -> Unit,
    onClick: (Long) -> Unit
) = AlertDialog(
    onDismissRequest = { onDismissDialog(false) },
    modifier = Modifier
        .background(ColorManager.surface)
        .padding(PaddingManager.Medium)
) {
    Column(verticalArrangement = Arrangement.spacedBy(PaddingManager.Medium)) {
        workoutNames.forEach {
            PrimaryText(text = it.second, modifier = Modifier.clickable { onClick(it.first) })
        }
    }
}

@Composable
private fun Modifier.drawDividers(color: Color) =
    this.drawBehind { drawVerticalDividers(color); drawHorizontalDividers(color) }

private fun DrawScope.drawHorizontalDividers(color: Color) {
    var yOffset = 0F

    repeat(8) {
        drawLine(
            color = color,
            start = Offset(x = 0F, y = yOffset),
            end = Offset(x = this.size.width * 7, y = yOffset)
        )

        yOffset += this.size.height
    }
}


private fun DrawScope.drawVerticalDividers(color: Color) {
    var xOffset = this.size.width

    repeat(6) {
        drawLine(
            color = color,
            start = Offset(x = xOffset, y = 0F),
            end = Offset(x = xOffset, y = this.size.height * 7)
        )

        xOffset += this.size.width
    }
}