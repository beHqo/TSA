package com.example.android.strikingarts.ui.workoutdetails

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.R
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.toImmutableList
import com.example.android.strikingarts.ui.components.CustomTextField
import com.example.android.strikingarts.ui.components.DoubleButtonBottomSheetBox
import com.example.android.strikingarts.ui.components.IntPickerBottomSheet
import com.example.android.strikingarts.ui.components.ProgressBar
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.example.android.strikingarts.ui.components.TimePicker
import com.example.android.strikingarts.ui.components.detailsitem.DetailsItem
import com.example.android.strikingarts.ui.components.util.SurviveProcessDeath
import com.example.android.strikingarts.ui.model.Time
import com.example.android.strikingarts.ui.parentlayouts.DetailsLayout

const val WORKOUT_NAME_FIELD = 441
const val WORKOUT_ROUNDS_FIELD = 442
const val WORKOUT_ROUND_LENGTH_FIELD = 443
const val WORKOUT_REST_LENGTH_FIELD = 444
const val WORKOUT_NOTIFICATION_INTERVAL_FIELD = 445

@Composable
fun WorkoutDetailsScreen(
    vm: WorkoutDetailsViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateToComboScreen: () -> Unit,
    showSnackbar: (String) -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit
) {
    val loadingScreen by vm.loadingScreen.collectAsStateWithLifecycle()

    if (loadingScreen) ProgressBar() else {
        val isWorkoutNew = vm.isWorkoutNew
        val name by vm.name.collectAsStateWithLifecycle()
        val rounds by vm.rounds.collectAsStateWithLifecycle()
        val roundLength by vm.roundLength.collectAsStateWithLifecycle()
        val restLength by vm.restLength.collectAsStateWithLifecycle()
        val subRounds by vm.subRound.collectAsStateWithLifecycle()
        val selectedItemsIdList by vm.selectedItemsIdList.collectAsStateWithLifecycle()

        val (bottomSheetVisible, setBottomSheetVisibility) = rememberSaveable { mutableStateOf(false) }
        val (bottomSheetContent, setBottomSheetContent) = rememberSaveable { mutableIntStateOf(0) }

        val errorState by remember { derivedStateOf { name.length > TEXTFIELD_NAME_MAX_CHARS || name.isEmpty() || rounds == 0 || roundLength.minutes == 0 && roundLength.seconds == 0 || restLength.minutes == 0 && restLength.seconds == 0 || subRounds != 0 && (roundLength.toSeconds() / 2) < subRounds } }

        val snackbarMessage = if (isWorkoutNew) stringResource(
            R.string.all_snackbar_insert, name
        ) else stringResource(R.string.all_snackbar_update, name)

        WorkoutDetailsScreen(
            name = name,
            onNameChange = vm::onNameChange,
            rounds = rounds,
            onRoundsChange = vm::onRoundsChange,
            roundLength = roundLength,
            onRoundLengthChange = vm::onRoundDurationChange,
            restLength = restLength,
            onRestLengthChange = vm::onRestDurationChange,
            subRound = subRounds,
            onSubRoundsChange = vm::onSubRoundsChange,
            selectedItemsIdList = selectedItemsIdList,
            onSaveButtonClick = vm::insertOrUpdateItem,
            saveButtonEnabled = !errorState,
            bottomSheetVisible = bottomSheetVisible,
            setBottomSheetVisibility = setBottomSheetVisibility,
            bottomSheetContent = bottomSheetContent,
            setBottomSheetContent = setBottomSheetContent,
            setSelectionModeValueGlobally = setSelectionModeValueGlobally,
            showSnackbar = { showSnackbar(snackbarMessage) },
            onNavigateToComboScreen = navigateToComboScreen,
            navigateUp = navigateUp
        )
    }

    SurviveProcessDeath(onStop = vm::surviveProcessDeath)
}

@Composable
private fun WorkoutDetailsScreen(
    name: String,
    onNameChange: (String) -> Unit,
    rounds: Int,
    onRoundsChange: (Int) -> Unit,
    roundLength: Time,
    onRoundLengthChange: (Time) -> Unit,
    restLength: Time,
    onRestLengthChange: (Time) -> Unit,
    subRound: Int,
    onSubRoundsChange: (Int) -> Unit,
    selectedItemsIdList: ImmutableList<Long>,
    onSaveButtonClick: () -> Unit,
    saveButtonEnabled: Boolean,
    bottomSheetVisible: Boolean,
    setBottomSheetVisibility: (Boolean) -> Unit,
    bottomSheetContent: Int,
    setBottomSheetContent: (Int) -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    showSnackbar: () -> Unit,
    onNavigateToComboScreen: () -> Unit,
    navigateUp: () -> Unit
) = DetailsLayout(bottomSheetVisible = bottomSheetVisible,
    setBottomSheetVisibility = setBottomSheetVisibility,
    saveButtonEnabled = saveButtonEnabled,
    onSaveButtonClick = { onSaveButtonClick(); showSnackbar(); setSelectionModeValueGlobally(false); navigateUp() },
    onDiscardButtonClick = { setSelectionModeValueGlobally(false); navigateUp() },
    bottomSheetContent = {
        when (bottomSheetContent) {
            WORKOUT_NAME_FIELD -> WorkoutNameTextField(name, onNameChange, setBottomSheetVisibility)

            WORKOUT_ROUNDS_FIELD -> WorkoutRoundsNumField(
                rounds, onRoundsChange, setBottomSheetVisibility
            )

            WORKOUT_ROUND_LENGTH_FIELD -> WorkoutRoundLengthTimePicker(
                roundLength, onRoundLengthChange, setBottomSheetVisibility
            )

            WORKOUT_REST_LENGTH_FIELD -> WorkoutRestLengthTimePicker(
                restLength, onRestLengthChange, setBottomSheetVisibility
            )

            WORKOUT_NOTIFICATION_INTERVAL_FIELD -> WorkoutSubRoundsPicker(
                roundLength, subRound, onSubRoundsChange, setBottomSheetVisibility
            )
        }
    },
    columnContent = {
        WorkoutDetailsColumnContent(
            name = name,
            rounds = rounds,
            roundLength = roundLength,
            restLength = restLength,
            breakpoints = subRound,
            selectedItemIds = selectedItemsIdList,
            onBottomSheetContentChange = setBottomSheetContent,
            showBottomSheet = setBottomSheetVisibility,
            onEnableSelectionMode = setSelectionModeValueGlobally,
            onNavigateToComboScreen = onNavigateToComboScreen
        )
    })

@Composable
fun WorkoutDetailsColumnContent(
    name: String,
    rounds: Int,
    roundLength: Time,
    restLength: Time,
    breakpoints: Int,
    onBottomSheetContentChange: (Int) -> Unit,
    showBottomSheet: (Boolean) -> Unit,
    selectedItemIds: ImmutableList<Long>,
    onEnableSelectionMode: (Boolean) -> Unit,
    onNavigateToComboScreen: () -> Unit
) {
    DetailsItem(startText = stringResource(R.string.all_name), endText = name) {
        onBottomSheetContentChange(WORKOUT_NAME_FIELD); showBottomSheet(true)
    }
    HorizontalDivider()

    DetailsItem(startText = stringResource(R.string.workout_details_rounds), endText = "$rounds") {
        onBottomSheetContentChange(WORKOUT_ROUNDS_FIELD); showBottomSheet(true)
    }
    HorizontalDivider()

    DetailsItem(
        startText = stringResource(R.string.workout_details_round_length),
        endText = roundLength.asString()
    ) { onBottomSheetContentChange(WORKOUT_ROUND_LENGTH_FIELD); showBottomSheet(true) }
    HorizontalDivider()

    DetailsItem(
        startText = stringResource(R.string.workout_details_rest_length),
        endText = restLength.asString()
    ) { onBottomSheetContentChange(WORKOUT_REST_LENGTH_FIELD); showBottomSheet(true) }
    HorizontalDivider()

    DetailsItem(
        startText = stringResource(R.string.workout_details_sub_rounds), endText = "$breakpoints"
    ) { onBottomSheetContentChange(WORKOUT_NOTIFICATION_INTERVAL_FIELD); showBottomSheet(true) }
    HorizontalDivider()

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

    DoubleButtonBottomSheetBox(
        setBottomSheetVisibility = onDismissBottomSheet,
        onSaveButtonClick = { onSaveButtonClick(currentName) },
        saveButtonEnabled = !errorState
    ) {
        CustomTextField(value = currentName,
            onValueChange = { if (it.length <= TEXTFIELD_NAME_MAX_CHARS + 1) currentName = it },
            maxChars = TEXTFIELD_NAME_MAX_CHARS,
            label = stringResource(R.string.all_name),
            placeHolder = stringResource(R.string.workout_details_name_placeholder_textfield),
            helperText = stringResource(R.string.workout_details_name_helper_textfield),
            onDoneImeAction = { onSaveButtonClick(currentName); onDismissBottomSheet(false) })
    }
}

@Composable
private fun WorkoutRoundsNumField(
    rounds: Int, onSaveButtonClick: (Int) -> Unit, onDismissBottomSheet: (Boolean) -> Unit
) {
    val (currentRounds, setRoundsAmount) = rememberSaveable { mutableIntStateOf(rounds) }

    DoubleButtonBottomSheetBox(
        setBottomSheetVisibility = onDismissBottomSheet,
        onSaveButtonClick = { onSaveButtonClick(currentRounds) },
    ) {
        IntPickerBottomSheet(
            range = (1..50).toImmutableList(),
            helperText = stringResource(R.string.workout_details_number_of_rounds),
            quantity = currentRounds,
            setQuantity = setRoundsAmount
        )
    }
}

@Composable
private fun WorkoutRoundLengthTimePicker(
    roundLength: Time, onSaveButtonClick: (Time) -> Unit, onDismissBottomSheet: (Boolean) -> Unit
) {
    var currentLength by rememberSaveable { mutableStateOf(roundLength) }
    val errorState by remember { derivedStateOf { currentLength.minutes == 0 && currentLength.seconds == 0 } }

    DoubleButtonBottomSheetBox(
        setBottomSheetVisibility = onDismissBottomSheet,
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
private fun WorkoutRestLengthTimePicker(
    restLength: Time, onSaveButtonClick: (Time) -> Unit, onDismissBottomSheet: (Boolean) -> Unit
) {
    var currentLength by rememberSaveable { mutableStateOf(restLength) }
    val errorState by remember { derivedStateOf { currentLength.minutes == 0 && currentLength.seconds == 0 } }

    DoubleButtonBottomSheetBox(
        setBottomSheetVisibility = onDismissBottomSheet,
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
private fun WorkoutSubRoundsPicker(
    roundLength: Time,
    subRounds: Int,
    onSaveButtonClick: (Int) -> Unit,
    onDismissBottomSheet: (Boolean) -> Unit,
) {
    val (currentSubRoundAmount, setSubRoundAmount) = rememberSaveable { mutableIntStateOf(subRounds) }
    val errorState by remember(currentSubRoundAmount) { derivedStateOf { currentSubRoundAmount != 0 && (roundLength.toSeconds() / 2) < currentSubRoundAmount } }

    DoubleButtonBottomSheetBox(
        setBottomSheetVisibility = onDismissBottomSheet,
        onSaveButtonClick = { onSaveButtonClick(currentSubRoundAmount) },
        saveButtonEnabled = !errorState
    ) {
        IntPickerBottomSheet(range = mutableListOf<Int>().apply { add(0); addAll(2..50) }
            .toImmutableList(),
//            range = List(51) { it }.drop(2).toImmutableList(),
            isError = errorState,
            errorText = stringResource(R.string.workout_details_breakpoint_error),
            helperText = stringResource(R.string.workout_details_breakpoint_helper),
            quantity = currentSubRoundAmount,
            setQuantity = setSubRoundAmount)
    }
}