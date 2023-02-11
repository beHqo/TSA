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
import com.example.android.strikingarts.ui.components.NameTextField
import com.example.android.strikingarts.ui.components.ProgressBar
import com.example.android.strikingarts.ui.components.TEXTFIELD_DESC_MAX_CHARS
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.example.android.strikingarts.ui.components.detailsitem.DetailsItem
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
    navigateToTechniqueScreen: () -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    navigateUp: () -> Unit
) {
    val loadingScreen by model.loadingScreen.collectAsState()

    if (loadingScreen) ProgressBar() else {
        val name by model.name.collectAsState()
        val desc by model.desc.collectAsState()
        val delay by model.delay.collectAsState()
        val selectedItemIds by model.selectedItemIds.collectAsState()

        val (bottomSheetVisible, setBottomSheetVisibility) = rememberSaveable { mutableStateOf(false) }
        val (bottomSheetContent, setBottomSheetContent) = rememberSaveable { mutableStateOf(0) }

        val errorState by remember {
            derivedStateOf {
                name.length > TEXTFIELD_NAME_MAX_CHARS || desc.length > TEXTFIELD_DESC_MAX_CHARS || name.isEmpty() || desc.isEmpty() || selectedItemIds.size < 2
            }
        }

        ComboDetailsScreen(
            name = name,
            onNameChange = model::onNameChange,
            desc = desc,
            onDescChange = model::onDescChange,
            delay = delay,
            onDelayChange = model::onDelayChange,
            selectedItemIds = selectedItemIds,
            insertOrUpdateItem = model::insertOrUpdateItem,
            saveButtonEnabled = !errorState,
            bottomSheetVisible = bottomSheetVisible,
            setBottomSheetVisibility = setBottomSheetVisibility,
            bottomSheetContent = bottomSheetContent,
            setBottomSheetContent = setBottomSheetContent,
            setSelectionModeValueGlobally = setSelectionModeValueGlobally,
            navigateUp = navigateUp,
            navigateToTechniqueScreen = navigateToTechniqueScreen
        )
    }
}

@Composable
private fun ComboDetailsScreen(
    name: String,
    onNameChange: (String) -> Unit,
    desc: String,
    onDescChange: (String) -> Unit,
    delay: Int,
    onDelayChange: (Int) -> Unit,
    selectedItemIds: ImmutableList<Long>,
    insertOrUpdateItem: () -> Unit,
    saveButtonEnabled: Boolean,
    bottomSheetVisible: Boolean,
    setBottomSheetVisibility: (Boolean) -> Unit,
    bottomSheetContent: Int,
    setBottomSheetContent: (Int) -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    navigateUp: () -> Unit,
    navigateToTechniqueScreen: () -> Unit
) {
    DetailsLayout(bottomSheetVisible = bottomSheetVisible,
        onDismissBottomSheet = setBottomSheetVisibility,
        saveButtonEnabled = saveButtonEnabled,
        onSaveButtonClick = {
            insertOrUpdateItem(); setSelectionModeValueGlobally(false); navigateUp()
        },
        onDiscardButtonClick = { setSelectionModeValueGlobally(false); navigateUp() },
        bottomSheetContent = {
            when (bottomSheetContent) {
                COMBO_NAME_FIELD -> ComboNameTextField(
                    name, setBottomSheetVisibility, onNameChange
                )

                COMBO_DESC_FIELD -> ComboDescTextField(
                    desc, setBottomSheetVisibility, onDescChange
                )

                COMBO_DELAY_COUNTER -> ComboDetailsSlider(
                    delay, setBottomSheetVisibility, onDelayChange
                )
            }
        },
        columnContent = {
            ComboDetailsColumnContent(
                name = name,
                desc = desc,
                delay = delay,
                selectedItemIds = selectedItemIds,
                onBottomSheetContentChange = setBottomSheetContent,
                showBottomSheet = setBottomSheetVisibility,
                onEnableSelectionMode = setSelectionModeValueGlobally,
                onNavigateToTechniqueScreen = navigateToTechniqueScreen
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
private fun ComboNameTextField(
    name: String, onDismissBottomSheet: (Boolean) -> Unit, onSaveButtonClick: (String) -> Unit
) {
    var currentName by rememberSaveable { mutableStateOf(name) }
    val errorState by remember { derivedStateOf { currentName.length > TEXTFIELD_NAME_MAX_CHARS || currentName.isEmpty() } }

    BottomSheetBox(
        onDismissBottomSheet = onDismissBottomSheet,
        onSaveButtonClick = { onSaveButtonClick(currentName) },
        saveButtonEnabled = !errorState
    ) {
        NameTextField(
            value = currentName,
            onValueChange = { if (it.length <= TEXTFIELD_NAME_MAX_CHARS + 1) currentName = it },
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
private fun ComboDescTextField(
    desc: String, onDismissBottomSheet: (Boolean) -> Unit, onSaveButtonClick: (String) -> Unit
) {
    var currentDesc by rememberSaveable { mutableStateOf(desc) }
    val errorState by remember { derivedStateOf { currentDesc.length > TEXTFIELD_DESC_MAX_CHARS || currentDesc.isEmpty() } }

    BottomSheetBox(
        onDismissBottomSheet = onDismissBottomSheet,
        onSaveButtonClick = { onSaveButtonClick(currentDesc) },
        saveButtonEnabled = !errorState
    ) {
        NameTextField(
            value = currentDesc,
            onValueChange = { if (it.length <= TEXTFIELD_DESC_MAX_CHARS + 1) currentDesc = it },
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
    delay: Int, onDismissBottomSheet: (Boolean) -> Unit, onSaveButtonClick: (Int) -> Unit
) {
    var currentDelay by rememberSaveable { mutableStateOf(delay.toFloat()) }

    BottomSheetBox(
        onDismissBottomSheet = onDismissBottomSheet,
        onSaveButtonClick = { onSaveButtonClick(currentDelay.roundToInt()) },
        saveButtonEnabled = true
    ) {
        DelaySlider(
            value = currentDelay,
            onValueChange = { currentDelay = it },
            valueRange = MIN_DELAY..MAX_DELAY,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = quantityStringResource(
                R.plurals.all_second, currentDelay.roundToInt(), currentDelay.roundToInt()
            ),
            style = MaterialTheme.typography.caption,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
