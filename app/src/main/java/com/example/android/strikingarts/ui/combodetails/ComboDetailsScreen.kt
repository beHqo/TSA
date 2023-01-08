package com.example.android.strikingarts.ui.combodetails

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.BottomSheetBox
import com.example.android.strikingarts.DetailsLayout
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.components.CounterAnimation
import com.example.android.strikingarts.ui.components.CountingIconButton
import com.example.android.strikingarts.ui.components.DelaySlider
import com.example.android.strikingarts.ui.components.DetailsItem
import com.example.android.strikingarts.ui.components.NameTextField
import com.example.android.strikingarts.ui.components.TEXTFIELD_DESC_MAX_CHARS
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.example.android.strikingarts.utils.ImmutableList
import com.example.android.strikingarts.utils.quantityStringResource
import kotlin.math.roundToInt

const val MAX_DELAY = 15
const val MIN_DELAY = 1

const val NAME_FIELD = "NameField"
const val DESC_FIELD = "DescField"
const val DELAY_COUNTER = "DelayCounter"

@Composable
fun ComboDetailsScreen(
    model: ComboDetailsViewModel = hiltViewModel(),
    onNavigateToTechniqueScreen: () -> Unit,
    onEnableSelectionMode: (Boolean) -> Unit,
    onNavigateUp: () -> Unit
) {
    val state by model.uiState.collectAsState()

    var bottomSheetVisible by rememberSaveable { mutableStateOf(false) }
    val bottomSheetVisibilityChange = { value: Boolean -> bottomSheetVisible = value }

    var bottomSheetContent by rememberSaveable { mutableStateOf("") }
    val onBottomSheetContentChange = { value: String -> bottomSheetContent = value }

    val errorState by remember {
        derivedStateOf {
            state.name.length > TEXTFIELD_NAME_MAX_CHARS || state.desc.length > TEXTFIELD_DESC_MAX_CHARS || state.name.isEmpty() || state.desc.isEmpty() || state.selectedItemIds.size < 2
        }
    }

    DetailsLayout(
        bottomSheetVisible = bottomSheetVisible,
        onDismissBottomSheet = bottomSheetVisibilityChange,
        columnContent = {
            ComboDetailsColumnContent(
                name = state.name,
                desc = state.desc,
                delay = state.delay,
                selectedItemIds = ImmutableList(state.selectedItemIds),
                onBottomSheetContentChange = onBottomSheetContentChange,
                showBottomSheet = bottomSheetVisibilityChange,
                onEnableSelectionMode = onEnableSelectionMode,
                onNavigateToTechniqueScreen = onNavigateToTechniqueScreen
            )
        },
        saveButtonEnabled = !errorState,
        onSaveButtonClick = { model.insertOrUpdateItem(); onNavigateUp() },
        bottomSheet = {
            when (bottomSheetContent) {
                NAME_FIELD -> ComboNameTextField(bottomSheetVisibilityChange, model::onNameChange)
                DESC_FIELD -> ComboDescTextField(bottomSheetVisibilityChange, model::onDescChange)
                DELAY_COUNTER -> DelayCounter(bottomSheetVisibilityChange, model::onDelayChange)
            }
        },
        onDiscardButtonClick = onNavigateUp
    )
}

@Composable
private fun ComboDetailsColumnContent(
    name: String,
    desc: String,
    delay: Int,
    selectedItemIds: ImmutableList<Long>,
    onBottomSheetContentChange: (String) -> Unit,
    showBottomSheet: (Boolean) -> Unit,
    onEnableSelectionMode: (Boolean) -> Unit,
    onNavigateToTechniqueScreen: () -> Unit
) {
    DetailsItem(
        startText = stringResource(R.string.combo_details_textfield_name_label), endText = name
    ) { onBottomSheetContentChange(NAME_FIELD); showBottomSheet(true) }
    Divider()
    DetailsItem(
        startText = stringResource(R.string.combo_details_textfield_desc_label), endText = desc
    ) { onBottomSheetContentChange(DESC_FIELD); showBottomSheet(true) }
    Divider()
    DetailsItem(
        startText = stringResource(R.string.combo_details_recovery),
        endText = quantityStringResource(R.plurals.all_second, delay, delay)
    ) { onBottomSheetContentChange(DELAY_COUNTER); showBottomSheet(true) }
    Divider()
    DetailsItem(
        startText = stringResource(R.string.combo_details_button_add_technique),
        endText = if (selectedItemIds.isEmpty()) stringResource(R.string.all_tap_to_set)
        else stringResource(R.string.all_tap_to_change)
    ) { onEnableSelectionMode(true); onNavigateToTechniqueScreen() }
}

@Composable
fun ComboNameTextField(
    onDismissBottomSheet: (Boolean) -> Unit, onSaveButtonClick: (String) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    val errorState by remember { derivedStateOf { name.length > TEXTFIELD_NAME_MAX_CHARS || name.isEmpty() } }

    BottomSheetBox(
        onDismissBottomSheet = onDismissBottomSheet,
        onSaveButtonClick = { onSaveButtonClick(name) },
        saveButtonEnabled = !errorState
    ) {
        NameTextField(
            modifier = Modifier.padding(bottom = 16.dp),
            value = name,
            onValueChange = { if (it.length <= TEXTFIELD_NAME_MAX_CHARS + 1) name = it },
            maxChars = TEXTFIELD_NAME_MAX_CHARS,
            label = stringResource(R.string.combo_details_textfield_name_label),
            placeHolder = stringResource(R.string.combo_details_textfield_name_placeholder),
            helperText = stringResource(R.string.combo_details_textfield_helper),
            leadingIcon = R.drawable.ic_glove_filled_light,
            imeAction = ImeAction.Next
        )
    }
}

@Composable
fun ComboDescTextField(
    onDismissBottomSheet: (Boolean) -> Unit, onSaveButtonClick: (String) -> Unit
) {
    var desc by rememberSaveable { mutableStateOf("") }
    val errorState by remember { derivedStateOf { desc.length > TEXTFIELD_DESC_MAX_CHARS || desc.isEmpty() } }

    BottomSheetBox(
        onDismissBottomSheet = onDismissBottomSheet,
        onSaveButtonClick = { onSaveButtonClick(desc) },
        saveButtonEnabled = !errorState
    ) {
        NameTextField(
            modifier = Modifier.padding(bottom = 32.dp),
            value = desc,
            onValueChange = { if (it.length <= TEXTFIELD_DESC_MAX_CHARS + 1) desc = it },
            maxChars = TEXTFIELD_DESC_MAX_CHARS,
            label = stringResource(R.string.combo_details_textfield_desc_label),
            placeHolder = stringResource(R.string.combo_details_textfield_desc_placeholder),
            helperText = stringResource(R.string.combo_details_textfield_desc_helper),
            leadingIcon = R.drawable.ic_comment_filled_light,
            imeAction = ImeAction.Next
        )
    }
}

@Composable
private fun DelayCounter(
    onDismissBottomSheet: (Boolean) -> Unit,
    onSaveButtonClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var delay by rememberSaveable { mutableStateOf(1) }

    BottomSheetBox(
        onDismissBottomSheet = onDismissBottomSheet,
        onSaveButtonClick = { onSaveButtonClick(delay) },
        saveButtonEnabled = true
    ) {
        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            CountingIconButton(imageVector = Icons.Sharp.Add,
                contentDescription = stringResource(R.string.combo_details_increase),
                enabled = delay < MAX_DELAY,
                onClick = { delay += 1 })
            CounterAnimation(delay)
            CountingIconButton(imageVector = Icons.Sharp.Remove,
                contentDescription = stringResource(R.string.combo_details_decrease),
                enabled = delay > MIN_DELAY,
                onClick = { delay -= 1 })
        }
    }
}

@Composable
private fun ComboDetailsSlider(
    delay: Float, onDelayChange: (Float) -> Unit, onSliderStopped: () -> Unit
) {
    Text(
        text = stringResource(R.string.combo_details_recovery),
        style = MaterialTheme.typography.subtitle2,
        fontSize = 16.sp,
        modifier = Modifier.padding(bottom = 4.dp)
    )
    DelaySlider(
        value = delay, onValueChange = onDelayChange,
        onValueChangeFinished = onSliderStopped, valueRange = 1F..15F,
    )
    Text(
        modifier = Modifier.padding(bottom = 24.dp),
        text = quantityStringResource(R.plurals.all_second, delay.roundToInt(), delay.roundToInt()),
        style = MaterialTheme.typography.caption
    )
}

//@OptIn(ExperimentalMaterialApi::class)
//@Composable
//fun ComboDetailsScreen(
//    model: ComboDetailsViewModel = hiltViewModel(),
//    scrollState: ScrollState = rememberScrollState(),
//    onNavigateToTechniqueScreen: () -> Unit,
//    onEnableSelectionMode: (Boolean) -> Unit,
//    onNavigateUp: () -> Unit
//) {
//    val state by model.uiState.collectAsState()
//    val errorState by remember {
//        derivedStateOf {
//            state.name.length > TEXTFIELD_NAME_MAX_CHARS || state.desc.length > TEXTFIELD_DESC_MAX_CHARS || state.name.isEmpty() || state.desc.isEmpty() || state.selectedItemIds.size < 2
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(scrollState),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
////        Button(onClick = {
////            onNavigateToTechniqueScreen(); onEnableSelectionMode(true); model.clearSelectedItemsId()
////        }) { Text(text = stringResource(R.string.combo_details_button_add_technique)) }
//
//        Spacer(modifier = Modifier.weight(1F))
//        DoubleButtonsRow(modifier = Modifier
//            .fillMaxWidth()
//            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
//            leftButtonText = stringResource(R.string.all_cancel),
//            rightButtonText = stringResource(R.string.all_save),
//            leftButtonEnabled = true,
//            rightButtonEnabled = !errorState,
//            onLeftButtonClick = { /*TODO*/ },
//            onRightButtonClick = { model.insertOrUpdateItem(); onNavigateUp() })
//    }
//}
//