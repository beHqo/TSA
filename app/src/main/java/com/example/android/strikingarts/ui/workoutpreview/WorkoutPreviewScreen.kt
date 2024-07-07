package com.example.android.strikingarts.ui.workoutpreview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.sharp.PlayArrow
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.R
import com.example.android.strikingarts.domain.model.Combo
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.ui.combo.ComboPreviewDialog
import com.example.android.strikingarts.ui.components.ConfirmDialog
import com.example.android.strikingarts.ui.components.DropdownIcon
import com.example.android.strikingarts.ui.components.MoreVertDropdownMenu
import com.example.android.strikingarts.ui.components.PrimaryText
import com.example.android.strikingarts.ui.components.ProgressBar
import com.example.android.strikingarts.ui.components.SecondaryText
import com.example.android.strikingarts.ui.compositionlocal.LocalUserPreferences
import com.example.android.strikingarts.ui.mapper.getTechniqueRepresentation
import com.example.android.strikingarts.ui.model.Time
import com.example.android.strikingarts.ui.model.toTime
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager
import com.example.android.strikingarts.ui.util.toComposeColor

private enum class ContentTypes { HEADER, DETAILS, LIST }

@Composable
fun WorkoutPreviewScreen(
    model: WorkoutPreviewViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateToTrainingScreen: (id: Long) -> Unit,
    navigateToWorkoutDetails: (id: Long) -> Unit
) {
    val loadingScreen by model.loadingScreen.collectAsStateWithLifecycle()

    if (loadingScreen) ProgressBar() else {
        val workoutListItem = model.workoutListItem
        val currentCombo by model.currentCombo.collectAsStateWithLifecycle()
        val deleteDialogVisible by model.deleteDialogVisible.collectAsStateWithLifecycle()
        val comboPreviewDialogVisible by model.comboPreviewDialogVisible.collectAsStateWithLifecycle()
        val currentColor by model.currentColor.collectAsStateWithLifecycle()

        ComboPreviewDialog(
            visible = comboPreviewDialogVisible,
            onDismiss = model::dismissComboPreviewDialog,
            comboName = currentCombo.name,
            comboText = currentCombo.getTechniqueRepresentation(LocalUserPreferences.current.techniqueRepresentationFormat),
            techniqueColor = currentColor.toComposeColor(),
            onPlay = model::playComboPreview
        )

        if (deleteDialogVisible) DeleteDialog(
            id = workoutListItem.id,
            onDismiss = model::setDeleteDialogVisibility,
            onDelete = model::onDelete,
            navigateUp = navigateUp
        )

        WorkoutPreviewScreen(
            name = workoutListItem.name,
            roundLength = workoutListItem.roundLengthSeconds.toTime(),
            restLength = workoutListItem.restLengthSeconds.toTime(),
            numberOfRounds = workoutListItem.rounds,
            comboList = workoutListItem.comboList,
            onComboClick = model::onComboClick,
            onPlay = { navigateToTrainingScreen(workoutListItem.id) },
            onEdit = { navigateToWorkoutDetails(workoutListItem.id) },
            setDeleteDialogVisibility = model::setDeleteDialogVisibility,
            navigateUp = navigateUp,
        )
    }
}

@Composable
private fun DeleteDialog(
    id: Long, onDismiss: (Boolean) -> Unit, onDelete: (Long) -> Unit, navigateUp: () -> Unit
) = ConfirmDialog(titleId = stringResource(R.string.all_delete),
    textId = stringResource(R.string.all_confirm_dialog_delete_singular),
    confirmButtonText = stringResource(R.string.all_delete),
    dismissButtonText = stringResource(R.string.all_cancel),
    onConfirm = { onDelete(id); navigateUp() },
    onDismiss = { onDismiss(false) })

@OptIn(ExperimentalFoundationApi::class) //StickyHeader
@Composable
private fun WorkoutPreviewScreen(
    name: String,
    numberOfRounds: Int,
    roundLength: Time,
    restLength: Time,
    comboList: ImmutableList<Combo>,
    onComboClick: (Combo) -> Unit,
    onPlay: () -> Unit,
    onEdit: () -> Unit,
    setDeleteDialogVisibility: (Boolean) -> Unit,
    navigateUp: () -> Unit,
) = LazyColumn(Modifier.fillMaxWidth()) {
    stickyHeader(contentType = { ContentTypes.HEADER }) {
        WorkoutPreviewTopAppBar(
            workoutName = name,
            onPlay = onPlay,
            onEdit = onEdit,
            onDelete = { setDeleteDialogVisibility(true) },
            navigateUp = navigateUp
        )
    }

    item(contentType = { ContentTypes.DETAILS }) {
        WorkoutDetails(
            numberOfRounds = numberOfRounds, roundLength = roundLength, restLength = restLength
        )
    }

    itemsIndexed(items = comboList,
        contentType = { _, _ -> ContentTypes.LIST },
        key = { index, _ -> index }) { _, item ->
        var expanded by rememberSaveable { mutableStateOf(false) }
        val setExpandedValue = { value: Boolean -> expanded = value }

        ComboPreviewListItem(
            comboListItem = item,
            expanded = expanded,
            setExpandedValue = setExpandedValue,
            onComboClick = { onComboClick(item) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkoutPreviewTopAppBar(
    workoutName: String,
    onPlay: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    navigateUp: () -> Unit
) = TopAppBar(
    title = { Text(workoutName) },
    navigationIcon = {
        IconButton(onClick = navigateUp) {
            Icon(
                imageVector = Icons.AutoMirrored.Sharp.ArrowBack,
                contentDescription = stringResource(R.string.workout_preview_navigate_up)
            )
        }
    },
    actions = {
        IconButton(onClick = onPlay) {
            Icon(
                imageVector = Icons.Sharp.PlayArrow,
                contentDescription = stringResource(R.string.workout_preview_start_workout)
            )
        }
        MoreVertDropdownMenu(
            onDelete = onDelete,
            onEdit = onEdit,
            modifier = Modifier.padding(end = PaddingManager.Medium)
        )
    },
    colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorManager.primaryContainer),
    windowInsets = WindowInsets(0) // To Force the correct height on the TopAppbar
)


@Composable
private fun WorkoutDetails(numberOfRounds: Int, roundLength: Time, restLength: Time) = Row(
    Modifier
        .fillMaxWidth()
        .background(ColorManager.primaryContainer)
        .padding(PaddingManager.Large),
    horizontalArrangement = Arrangement.SpaceAround,
    verticalAlignment = Alignment.CenterVertically
) {
    RoundsText(numberOfRounds)

    PrimaryText(text = "⏳ ${roundLength.asString()}", textAlign = TextAlign.Center, maxLines = 2)

    PrimaryText(text = "💤 ${restLength.asString()}", textAlign = TextAlign.Center, maxLines = 2)
}

@Composable
private fun ComboPreviewListItem(
    comboListItem: Combo,
    expanded: Boolean,
    setExpandedValue: (Boolean) -> Unit,
    onComboClick: () -> Unit,
) = Column(
    Modifier
        .fillMaxWidth()
        .heightIn(min = 56.dp)
        .clickable(onClick = onComboClick)
        .padding(PaddingManager.Large)
) {
    Box(
        Modifier.fillMaxWidth()
    ) {
        PrimaryText(comboListItem.name)

        DropdownIcon(
            expanded,
            Modifier
                .align(Alignment.TopEnd)
                .clickable { setExpandedValue(!expanded) })
    }
    AnimatedVisibility(expanded) {
        SecondaryText(
            text = comboListItem.getTechniqueRepresentation(LocalUserPreferences.current.techniqueRepresentationFormat),
            maxLines = Int.MAX_VALUE,
            modifier = Modifier.padding(end = PaddingManager.Large)
        )
    }
}

@Composable
private fun RoundsText(numberOfRounds: Int) {
    val str = pluralStringResource(R.plurals.workout_preview_rounds, numberOfRounds, numberOfRounds)
    val numberOfRoundsStr = "$numberOfRounds"
    val indexOfRounds = str.indexOf(numberOfRoundsStr)
    val roundsLength = numberOfRoundsStr.length

    val roundsAnnotatedString = buildAnnotatedString {
        addStyle(
            style = SpanStyle(color = ColorManager.primary, fontWeight = FontWeight.Bold),
            start = indexOfRounds,
            end = indexOfRounds + roundsLength
        )
        append(str)
    }

    PrimaryText(roundsAnnotatedString)
}