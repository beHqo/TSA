package com.example.android.strikingarts.ui.combodetails

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.R
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.ui.components.CustomTextField
import com.example.android.strikingarts.ui.components.DelaySlider
import com.example.android.strikingarts.ui.components.ProgressBar
import com.example.android.strikingarts.ui.components.TEXTFIELD_DESC_MAX_CHARS
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.example.android.strikingarts.ui.components.descFieldError
import com.example.android.strikingarts.ui.components.detailsitem.DetailsItem
import com.example.android.strikingarts.ui.components.util.SurviveProcessDeath
import com.example.android.strikingarts.ui.parentlayouts.BottomSheetBox
import com.example.android.strikingarts.ui.parentlayouts.DetailsLayout
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.TypographyManager
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
    navigateUp: () -> Unit,
    navigateToTechniqueScreen: () -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit
) {
    val loadingScreen by model.loadingScreen.collectAsStateWithLifecycle()

    if (loadingScreen) ProgressBar() else {
        val name by model.name.collectAsStateWithLifecycle()
        val desc by model.desc.collectAsStateWithLifecycle()
        val delay by model.delaySeconds.collectAsStateWithLifecycle()
        val selectedItemsIdList by model.selectedItemsIdList.collectAsStateWithLifecycle()

        val (bottomSheetVisible, setBottomSheetVisibility) = rememberSaveable { mutableStateOf(false) }
        val (bottomSheetContent, setBottomSheetContent) = rememberSaveable { mutableIntStateOf(0) }

        val errorState by remember {
            derivedStateOf {
                name.length > TEXTFIELD_NAME_MAX_CHARS || desc.length > TEXTFIELD_DESC_MAX_CHARS || name.isEmpty() || desc.isEmpty() || selectedItemsIdList.size < 2
            }
        }

        ComboDetailsScreen(
            name = name,
            onNameChange = model::onNameChange,
            desc = desc,
            onDescChange = model::onDescChange,
            delay = delay,
            onDelayChange = model::onDelayChange,
            selectedItemsIdList = selectedItemsIdList,
            onSaveButtonClick = model::insertOrUpdateItem,
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

    SurviveProcessDeath(onStop = model::surviveProcessDeath)
}

@Composable
private fun ComboDetailsScreen(
    name: String,
    onNameChange: (String) -> Unit,
    desc: String,
    onDescChange: (String) -> Unit,
    delay: Int,
    onDelayChange: (Int) -> Unit,
    selectedItemsIdList: ImmutableList<Long>,
    onSaveButtonClick: () -> Unit,
    saveButtonEnabled: Boolean,
    bottomSheetVisible: Boolean,
    setBottomSheetVisibility: (Boolean) -> Unit,
    bottomSheetContent: Int,
    setBottomSheetContent: (Int) -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    navigateUp: () -> Unit,
    navigateToTechniqueScreen: () -> Unit
) = DetailsLayout(
    bottomSheetVisible = bottomSheetVisible,
    setBottomSheetVisibility = setBottomSheetVisibility,
    saveButtonEnabled = saveButtonEnabled,
    onSaveButtonClick = {
        onSaveButtonClick(); setSelectionModeValueGlobally(false); navigateUp()
    },
    onDiscardButtonClick = { setSelectionModeValueGlobally(false); navigateUp() },
    bottomSheetContent = {
        when (bottomSheetContent) {
            COMBO_NAME_FIELD -> ComboNameTextField(
                name, onNameChange, setBottomSheetVisibility
            )

            COMBO_DESC_FIELD -> ComboDescTextField(
                desc, onDescChange, setBottomSheetVisibility
            )

            COMBO_DELAY_COUNTER -> ComboDetailsSlider(
                delay, onDelayChange, setBottomSheetVisibility
            )
        }
    },
    columnContent = {
        ComboDetailsColumnContent(
            name = name,
            desc = desc,
            delay = delay,
            selectedItemIds = selectedItemsIdList,
            onBottomSheetContentChange = setBottomSheetContent,
            showBottomSheet = setBottomSheetVisibility,
            setSelectionModeValueGlobally = setSelectionModeValueGlobally,
            onNavigateToTechniqueScreen = navigateToTechniqueScreen
        )
    })

@Composable
private fun ComboDetailsColumnContent(
    name: String,
    desc: String,
    delay: Int,
    onBottomSheetContentChange: (Int) -> Unit,
    showBottomSheet: (Boolean) -> Unit,
    selectedItemIds: ImmutableList<Long>,
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    onNavigateToTechniqueScreen: () -> Unit
) {
    DetailsItem(
        startText = stringResource(R.string.all_name), endText = name
    ) { onBottomSheetContentChange(COMBO_NAME_FIELD); showBottomSheet(true) }
    Divider()
    DetailsItem(
        startText = stringResource(R.string.all_desc), endText = desc
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
    ) { setSelectionModeValueGlobally(true); onNavigateToTechniqueScreen() }
}

@Composable
private fun ComboNameTextField(
    name: String, onSaveButtonClick: (String) -> Unit, onDismissBottomSheet: (Boolean) -> Unit
) {
    var currentName by rememberSaveable { mutableStateOf(name) }
    val errorState by remember { derivedStateOf { currentName.length > TEXTFIELD_NAME_MAX_CHARS || currentName.isEmpty() } }

    BottomSheetBox(
        setBottomSheetVisibility = onDismissBottomSheet,
        onSaveButtonClick = { onSaveButtonClick(currentName) },
        saveButtonEnabled = !errorState
    ) {
        CustomTextField(value = currentName,
            onValueChange = { if (it.length <= TEXTFIELD_NAME_MAX_CHARS + 1) currentName = it },
            maxChars = TEXTFIELD_NAME_MAX_CHARS,
            label = stringResource(R.string.all_name),
            placeHolder = stringResource(R.string.combo_details_textfield_name_placeholder),
            helperText = stringResource(R.string.combo_details_textfield_helper),
            leadingIcon = { Icon(painterResource(R.drawable.ic_glove_filled_light), null) },
            onDoneImeAction = { onSaveButtonClick(currentName); onDismissBottomSheet(false) })
    }
}

@Composable
private fun ComboDescTextField(
    desc: String, onSaveButtonClick: (String) -> Unit, onDismissBottomSheet: (Boolean) -> Unit
) {
    var currentDesc by rememberSaveable { mutableStateOf(desc) }
    val errorState by remember { derivedStateOf { currentDesc.length > TEXTFIELD_DESC_MAX_CHARS || currentDesc.isEmpty() } }

    BottomSheetBox(
        setBottomSheetVisibility = onDismissBottomSheet,
        onSaveButtonClick = { onSaveButtonClick(currentDesc) },
        saveButtonEnabled = !errorState
    ) {
        CustomTextField(value = currentDesc,
            onValueChange = { if (it.length <= TEXTFIELD_DESC_MAX_CHARS + 1) currentDesc = it },
            maxChars = TEXTFIELD_DESC_MAX_CHARS,
            label = stringResource(R.string.all_desc),
            placeHolder = stringResource(R.string.combo_details_textfield_desc_placeholder),
            helperText = stringResource(R.string.combo_details_textfield_desc_helper),
            leadingIcon = { Icon(painterResource(R.drawable.ic_comment_filled_light), null) },
            errorList = descFieldError,
            onDoneImeAction = { onSaveButtonClick(currentDesc); onDismissBottomSheet(false) }
        )
    }
}

@Composable
private fun ComboDetailsSlider(
    delaySeconds: Int, onSaveButtonClick: (Int) -> Unit, onDismissBottomSheet: (Boolean) -> Unit
) {
    var currentDelaySeconds by rememberSaveable { mutableFloatStateOf(delaySeconds.toFloat()) }

    BottomSheetBox(
        setBottomSheetVisibility = onDismissBottomSheet,
        onSaveButtonClick = { onSaveButtonClick(currentDelaySeconds.roundToInt()) },
        saveButtonEnabled = true
    ) {
        DelaySlider(
            value = currentDelaySeconds,
            onValueChange = { currentDelaySeconds = it },
            valueRange = MIN_DELAY..MAX_DELAY,
            modifier = Modifier.padding(bottom = PaddingManager.Small)
        )

        Text(
            text = quantityStringResource(
                R.plurals.all_second,
                currentDelaySeconds.roundToInt(),
                currentDelaySeconds.roundToInt()
            ),
            style = TypographyManager.bodySmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}