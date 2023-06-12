package com.example.android.strikingarts.ui.workoutdetails

import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.NotificationAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.R
import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.ui.components.CustomTextField
import com.example.android.strikingarts.ui.components.NumTextField
import com.example.android.strikingarts.ui.components.ProgressBar
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.example.android.strikingarts.ui.components.TimePicker
import com.example.android.strikingarts.ui.components.detailsitem.DetailsItem
import com.example.android.strikingarts.ui.components.numfieldError
import com.example.android.strikingarts.ui.components.removePrefixZeros
import com.example.android.strikingarts.ui.model.TextFieldError
import com.example.android.strikingarts.ui.model.Time
import com.example.android.strikingarts.ui.parentlayouts.BottomSheetBox
import com.example.android.strikingarts.ui.parentlayouts.DetailsLayout

const val WORKOUT_NAME_FIELD = 441
const val WORKOUT_ROUNDS_FIELD = 442
const val WORKOUT_ROUND_LENGTH_FIELD = 443
const val WORKOUT_REST_LENGTH_FIELD = 444
const val WORKOUT_NOTIFICATION_INTERVAL_FIELD = 445

@Composable
fun WorkoutDetailsScreen(
    model: WorkoutDetailsViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateToComboScreen: () -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit
) {
    val loadingScreen by model.loadingScreen.collectAsStateWithLifecycle()

    if (loadingScreen) ProgressBar() else {
        val name by model.name.collectAsStateWithLifecycle()
        val rounds by model.rounds.collectAsStateWithLifecycle()
        val roundLength by model.roundLength.collectAsStateWithLifecycle()
        val restLength by model.restLength.collectAsStateWithLifecycle()
        val notificationIntervals by model.breakpoints.collectAsStateWithLifecycle()
        val selectedItemsIdList by model.selectedItemsIdList.collectAsStateWithLifecycle()

        val (bottomSheetVisible, setBottomSheetVisibility) = rememberSaveable { mutableStateOf(false) }
        val (bottomSheetContent, setBottomSheetContent) = rememberSaveable { mutableStateOf(0) }

        val errorState by remember { derivedStateOf { name.length > TEXTFIELD_NAME_MAX_CHARS || name.isEmpty() || !rounds.isDigitsOnly() || rounds.isEmpty() || rounds == "0" || roundLength.minutes == 0 && roundLength.seconds == 0 || restLength.minutes == 0 && restLength.seconds == 0 || !notificationIntervals.isDigitsOnly() || notificationIntervals.isEmpty() || notificationIntervals.isNotEmpty() && notificationIntervals.toInt() > roundLength.toSeconds() / 2 } }

        WorkoutDetailsScreen(
            name = name,
            onNameChange = model::onNameChange,
            rounds = rounds,
            onRoundsChange = model::onRoundsChange,
            roundLength = roundLength,
            onRoundLengthChange = model::onRoundDurationChange,
            restLength = restLength,
            onRestLengthChange = model::onRestDurationChange,
            notificationIntervals = notificationIntervals,
            onBreakpointsChange = model::onNotificationIntervalsChange,
            selectedItemsIdList = selectedItemsIdList,
            onSaveButtonClick = model::insertOrUpdateItem,
            saveButtonEnabled = !errorState,
            bottomSheetVisible = bottomSheetVisible,
            setBottomSheetVisibility = setBottomSheetVisibility,
            bottomSheetContent = bottomSheetContent,
            setBottomSheetContent = setBottomSheetContent,
            setSelectionModeValueGlobally = setSelectionModeValueGlobally,
            navigateUp = navigateUp,
            onNavigateToComboScreen = navigateToComboScreen
        )
    }
}

@Composable
private fun WorkoutDetailsScreen(
    name: String,
    onNameChange: (String) -> Unit,
    rounds: String,
    onRoundsChange: (String) -> Unit,
    roundLength: Time,
    onRoundLengthChange: (Time) -> Unit,
    restLength: Time,
    onRestLengthChange: (Time) -> Unit,
    notificationIntervals: String,
    onBreakpointsChange: (String) -> Unit,
    selectedItemsIdList: ImmutableList<Long>,
    onSaveButtonClick: () -> Unit,
    saveButtonEnabled: Boolean,
    bottomSheetVisible: Boolean,
    setBottomSheetVisibility: (Boolean) -> Unit,
    bottomSheetContent: Int,
    setBottomSheetContent: (Int) -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    navigateUp: () -> Unit,
    onNavigateToComboScreen: () -> Unit
) = DetailsLayout(bottomSheetVisible = bottomSheetVisible,
    onDismissBottomSheet = setBottomSheetVisibility,
    saveButtonEnabled = saveButtonEnabled,
    onSaveButtonClick = { onSaveButtonClick(); setSelectionModeValueGlobally(false); navigateUp() },
    onDiscardButtonClick = { setSelectionModeValueGlobally(false); navigateUp() },
    bottomSheetContent = {
        when (bottomSheetContent) {
            WORKOUT_NAME_FIELD -> WorkoutNameTextField(name, onNameChange, setBottomSheetVisibility)

            WORKOUT_ROUNDS_FIELD -> WorkoutRoundsNumField(
                rounds, onRoundsChange, setBottomSheetVisibility
            )

            WORKOUT_ROUND_LENGTH_FIELD -> WorkoutRoundLengthNumField(
                roundLength, onRoundLengthChange, setBottomSheetVisibility
            )

            WORKOUT_REST_LENGTH_FIELD -> WorkoutRestLengthNumField(
                restLength, onRestLengthChange, setBottomSheetVisibility
            )

            WORKOUT_NOTIFICATION_INTERVAL_FIELD -> WorkoutBreakpointsNumField(
                notificationIntervals, onBreakpointsChange, roundLength, setBottomSheetVisibility
            )
        }
    },
    columnContent = {
        WorkoutDetailsColumnContent(
            name = name,
            rounds = rounds,
            roundLength = roundLength,
            restLength = restLength,
            notificationIntervals = notificationIntervals,
            selectedItemIds = selectedItemsIdList,
            onBottomSheetContentChange = setBottomSheetContent,
            showBottomSheet = setBottomSheetVisibility,
            onEnableSelectionMode = setSelectionModeValueGlobally,
            onNavigateToComboScreen = onNavigateToComboScreen
        )
    }
)

@Composable
fun WorkoutDetailsColumnContent(
    name: String,
    rounds: String,
    roundLength: Time,
    restLength: Time,
    notificationIntervals: String,
    onBottomSheetContentChange: (Int) -> Unit,
    showBottomSheet: (Boolean) -> Unit,
    selectedItemIds: ImmutableList<Long>,
    onEnableSelectionMode: (Boolean) -> Unit,
    onNavigateToComboScreen: () -> Unit
) {
    DetailsItem(startText = stringResource(R.string.all_name), endText = name) {
        onBottomSheetContentChange(WORKOUT_NAME_FIELD); showBottomSheet(true)
    }
    Divider()
    DetailsItem(startText = stringResource(R.string.workout_details_rounds), endText = rounds) {
        onBottomSheetContentChange(WORKOUT_ROUNDS_FIELD); showBottomSheet(true)
    }
    Divider()
    DetailsItem(
        startText = stringResource(R.string.workout_details_round_length),
        endText = roundLength.asString()
    ) { onBottomSheetContentChange(WORKOUT_ROUND_LENGTH_FIELD); showBottomSheet(true) }
    Divider()
    DetailsItem(
        startText = stringResource(R.string.workout_details_rest_length),
        endText = restLength.asString()
    ) { onBottomSheetContentChange(WORKOUT_REST_LENGTH_FIELD); showBottomSheet(true) }
    Divider()
    DetailsItem(
        startText = stringResource(R.string.workout_details_notification_intervals),
        endText = notificationIntervals
    ) { onBottomSheetContentChange(WORKOUT_NOTIFICATION_INTERVAL_FIELD); showBottomSheet(true) }
    Divider()
    DetailsItem(
        startText = stringResource(R.string.workout_details_button_add_combos),
        endText = if (selectedItemIds.isEmpty()) "" else stringResource(R.string.all_details_item_tap_to_change)
    ) { onEnableSelectionMode(true); onNavigateToComboScreen() }
}

@Composable
private fun WorkoutNameTextField(
    name: String, onSaveButtonClick: (String) -> Unit, onDismissBottomSheet: (Boolean) -> Unit
) {
    var currentName by rememberSaveable { mutableStateOf(name) }
    val errorState by remember { derivedStateOf { currentName.length > TEXTFIELD_NAME_MAX_CHARS || currentName.isEmpty() } }

    BottomSheetBox(
        onDismissBottomSheet = onDismissBottomSheet,
        onSaveButtonClick = { onSaveButtonClick(currentName) },
        saveButtonEnabled = !errorState
    ) {
        CustomTextField(value = currentName,
            onValueChange = { if (it.length <= TEXTFIELD_NAME_MAX_CHARS + 1) currentName = it },
            maxChars = TEXTFIELD_NAME_MAX_CHARS,
            label = stringResource(R.string.all_name),
            placeHolder = stringResource(R.string.workout_details_name_placeholder_textfield),
            helperText = stringResource(R.string.workout_details_name_helper_textfield),
            leadingIcon = { Icon(painterResource(R.drawable.ic_glove_filled_light), null) },
            onDoneImeAction = { onSaveButtonClick(currentName); onDismissBottomSheet(false) })
    }
}

@Composable
private fun WorkoutRoundsNumField(
    rounds: String, onSaveButtonClick: (String) -> Unit, onDismissBottomSheet: (Boolean) -> Unit
) {
    var currentRounds by rememberSaveable { mutableStateOf(rounds) }
    val errorState by remember { derivedStateOf { !currentRounds.isDigitsOnly() || currentRounds.isEmpty() || currentRounds == "0" } }
    val textfieldErrorList = ImmutableList(
        numfieldError.plus(TextFieldError(R.string.workout_details_rounds_error_zero) { currentRounds == "0" })
    )

    BottomSheetBox(
        onDismissBottomSheet = onDismissBottomSheet,
        onSaveButtonClick = { onSaveButtonClick(currentRounds) },
        saveButtonEnabled = !errorState
    ) {
        NumTextField(value = currentRounds,
            onValueChange = { if (it.isDigitsOnly()) currentRounds = it.removePrefixZeros() },
            label = stringResource(R.string.workout_details_rounds),
            placeHolder = stringResource(R.string.all_five),
            leadingIcon = { Icon(painterResource(R.drawable.ic_workout), null) },
            helperText = stringResource(R.string.workout_details_number_of_rounds),
            errorList = textfieldErrorList,
            onDoneImeAction = { onSaveButtonClick(currentRounds); onDismissBottomSheet(false) })
    }
}

@Composable
private fun WorkoutRoundLengthNumField(
    roundLength: Time, onSaveButtonClick: (Time) -> Unit, onDismissBottomSheet: (Boolean) -> Unit
) {
    var currentLength by rememberSaveable { mutableStateOf(roundLength) }
    val errorState by remember { derivedStateOf { currentLength.minutes == 0 && currentLength.seconds == 0 } }

    BottomSheetBox(
        onDismissBottomSheet = onDismissBottomSheet,
        onSaveButtonClick = { onSaveButtonClick(currentLength) },
        saveButtonEnabled = !errorState
    ) {
        TimePicker(
            value = currentLength,
            onValueChange = { currentLength = it },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun WorkoutRestLengthNumField(
    restLength: Time, onSaveButtonClick: (Time) -> Unit, onDismissBottomSheet: (Boolean) -> Unit
) {
    var currentLength by rememberSaveable { mutableStateOf(restLength) }
    val errorState by remember { derivedStateOf { currentLength.minutes == 0 && currentLength.seconds == 0 } }

    BottomSheetBox(
        onDismissBottomSheet = onDismissBottomSheet,
        onSaveButtonClick = { onSaveButtonClick(currentLength) },
        saveButtonEnabled = !errorState
    ) {
        TimePicker(
            value = currentLength,
            onValueChange = { currentLength = it },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun WorkoutBreakpointsNumField(
    notificationIntervals: String,
    onSaveButtonClick: (String) -> Unit,
    roundLength: Time,
    onDismissBottomSheet: (Boolean) -> Unit
) {
    var currentIntervals by rememberSaveable { mutableStateOf(notificationIntervals) }
    val errorState by remember { derivedStateOf { !currentIntervals.isDigitsOnly() || currentIntervals.isEmpty() || currentIntervals.isNotEmpty() && currentIntervals.toInt() > roundLength.toSeconds() / 2 } }
    val textfieldErrorList =
        ImmutableList(numfieldError.plus(TextFieldError(R.string.workout_details_textfield_interval_error) { it.isNotEmpty() && it.toInt() > roundLength.toSeconds() / 2 }))

    BottomSheetBox(
        onDismissBottomSheet = onDismissBottomSheet,
        onSaveButtonClick = { onSaveButtonClick(currentIntervals) },
        saveButtonEnabled = !errorState
    ) {
        NumTextField(value = currentIntervals,
            onValueChange = { if (it.isDigitsOnly()) currentIntervals = it.removePrefixZeros() },
            label = stringResource(R.string.workout_details_breakpoints),
            placeHolder = "1",
            leadingIcon = { Icon(Icons.Sharp.NotificationAdd, null) },
            helperText = stringResource(R.string.workout_details_intervals_numfield),
            errorList = textfieldErrorList,
            onDoneImeAction = { onSaveButtonClick(currentIntervals); onDismissBottomSheet(false) })
    }
}