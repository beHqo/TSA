package com.example.android.strikingarts.ui.combodetails

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.components.DelaySlider
import com.example.android.strikingarts.ui.components.DetailsItem
import com.example.android.strikingarts.ui.components.NameTextField
import com.example.android.strikingarts.ui.components.TEXTFIELD_DESC_MAX_CHARS
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.example.android.strikingarts.ui.parentlayouts.BottomSheetBox
import com.example.android.strikingarts.ui.parentlayouts.DetailsLayout
import com.example.android.strikingarts.utils.ImmutableList
import com.example.android.strikingarts.utils.quantityStringResource
import kotlin.math.roundToInt

const val MIN_DELAY = 1F
const val MAX_DELAY = 15F

const val COMBO_NAME_FIELD = 331
const val COMBO_DESC_FIELD = 332
const val COMBO_DELAY_COUNTER = 333

@Composable
fun ComboDetailsScreen(
    model: ComboDetailsViewModel = hiltViewModel(),
    onNavigateToTechniqueScreen: () -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    onNavigateUp: () -> Unit
) {
    val state by model.uiState.collectAsState()

    val (bottomSheetVisible, bottomSheetVisibilityChange) = rememberSaveable { mutableStateOf(false) }

    val (bottomSheetContent, onBottomSheetContentChange) = rememberSaveable { mutableStateOf(0) }

    val errorState by remember {
        derivedStateOf {
            state.name.length > TEXTFIELD_NAME_MAX_CHARS || state.desc.length > TEXTFIELD_DESC_MAX_CHARS || state.name.isEmpty() || state.desc.isEmpty() || state.selectedItemIds.size < 2
        }
    }

    DetailsLayout(bottomSheetVisible = bottomSheetVisible,
        onDismissBottomSheet = bottomSheetVisibilityChange,
        saveButtonEnabled = !errorState,
        onSaveButtonClick = {
            model.insertOrUpdateItem(); setSelectionModeValueGlobally(false); onNavigateUp()
        },
        onDiscardButtonClick = { setSelectionModeValueGlobally(false); onNavigateUp() },
        bottomSheetContent = {
            when (bottomSheetContent) {
                COMBO_NAME_FIELD -> ComboNameTextField(
                    bottomSheetVisibilityChange, model::onNameChange
                )

                COMBO_DESC_FIELD -> ComboDescTextField(
                    bottomSheetVisibilityChange, model::onDescChange
                )

                COMBO_DELAY_COUNTER -> ComboDetailsSlider(
                    bottomSheetVisibilityChange, model::onDelayChange
                )
            }
        },
        columnContent = {
            ComboDetailsColumnContent(
                name = state.name,
                desc = state.desc,
                delay = state.delay,
                selectedItemIds = ImmutableList(state.selectedItemIds),
                onBottomSheetContentChange = onBottomSheetContentChange,
                showBottomSheet = bottomSheetVisibilityChange,
                onEnableSelectionMode = setSelectionModeValueGlobally,
                onNavigateToTechniqueScreen = onNavigateToTechniqueScreen
            )
        })
}

@Composable
private fun ComboDetailsColumnContent(
    name: String,
    desc: String,
    delay: Int,
    selectedItemIds: ImmutableList<Long>,
    onBottomSheetContentChange: (Int) -> Unit,
    showBottomSheet: (Boolean) -> Unit,
    onEnableSelectionMode: (Boolean) -> Unit,
    onNavigateToTechniqueScreen: () -> Unit
) {
    DetailsItem(
        startText = stringResource(R.string.combo_details_textfield_name_label), endText = name
    ) { onBottomSheetContentChange(COMBO_NAME_FIELD); showBottomSheet(true) }
    Divider()
    DetailsItem(
        startText = stringResource(R.string.combo_details_textfield_desc_label), endText = desc
    ) { onBottomSheetContentChange(COMBO_DESC_FIELD); showBottomSheet(true) }
    Divider()
    DetailsItem(
        startText = stringResource(R.string.combo_details_recovery),
        endText = quantityStringResource(R.plurals.all_second, delay, delay)
    ) { onBottomSheetContentChange(COMBO_DELAY_COUNTER); showBottomSheet(true) }
    Divider()
    DetailsItem(
        startText = stringResource(R.string.combo_details_button_add_technique),
        endText = if (selectedItemIds.isEmpty()) "" else stringResource(R.string.all_details_item_tap_to_change)
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
private fun ComboDetailsSlider(
    onDismissBottomSheet: (Boolean) -> Unit, onSaveButtonClick: (Int) -> Unit
) {
    var delay by rememberSaveable { mutableStateOf(1F) }

    BottomSheetBox(
        onDismissBottomSheet = onDismissBottomSheet,
        onSaveButtonClick = { onSaveButtonClick(delay.roundToInt()) },
        saveButtonEnabled = true
    ) {
        DelaySlider(
            value = delay,
            onValueChange = { delay = it },
            valueRange = MIN_DELAY..MAX_DELAY,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = quantityStringResource(
                R.plurals.all_second, delay.roundToInt(), delay.roundToInt()
            ),
            style = MaterialTheme.typography.caption,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
