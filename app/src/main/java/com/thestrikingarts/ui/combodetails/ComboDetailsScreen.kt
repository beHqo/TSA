package com.thestrikingarts.ui.combodetails

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thestrikingarts.R
import com.thestrikingarts.ui.components.CustomTextField
import com.thestrikingarts.ui.components.DelaySlider
import com.thestrikingarts.ui.components.DoubleButtonBottomSheetBox
import com.thestrikingarts.ui.components.ProgressBar
import com.thestrikingarts.ui.components.TEXTFIELD_DESC_MAX_CHARS
import com.thestrikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.thestrikingarts.ui.components.descFieldError
import com.thestrikingarts.ui.components.detailsitem.DetailsItem
import com.thestrikingarts.ui.parentlayouts.DetailsLayout
import com.thestrikingarts.ui.theme.designsystemmanager.PaddingManager
import com.thestrikingarts.ui.theme.designsystemmanager.TypographyManager
import com.thestrikingarts.ui.util.SurviveProcessDeath
import kotlin.math.roundToInt

const val MIN_DELAY = 1F
const val MAX_DELAY = 15F

const val COMBO_NAME_FIELD = 331
const val COMBO_DESC_FIELD = 332
const val COMBO_DELAY_COUNTER = 333

@Composable
fun ComboDetailsScreen(
    vm: ComboDetailsViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateToTechniqueScreen: () -> Unit,
    showSnackbar: (String) -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit
) {
    val loadingScreen by vm.loadingScreen.collectAsStateWithLifecycle()

    if (loadingScreen) ProgressBar() else {
        val isComboNew = vm.isComboNew
        val name by vm.name.collectAsStateWithLifecycle()
        val desc by vm.desc.collectAsStateWithLifecycle()
        val delay by vm.delaySeconds.collectAsStateWithLifecycle()
        val selectedItemsIdList by vm.selectedItemsIdList.collectAsStateWithLifecycle()

        val (bottomSheetVisible, setBottomSheetVisibility) = rememberSaveable { mutableStateOf(false) }
        val (bottomSheetContent, setBottomSheetContent) = rememberSaveable { mutableIntStateOf(0) }

        val errorState by remember {
            derivedStateOf {
                name.length > TEXTFIELD_NAME_MAX_CHARS || desc.length > TEXTFIELD_DESC_MAX_CHARS || name.isEmpty() || desc.isEmpty() || selectedItemsIdList.isEmpty()
            }
        }

        val snackbarMessage = if (isComboNew) stringResource(
            R.string.all_snackbar_insert, name
        ) else stringResource(R.string.all_snackbar_update, name)

        ComboDetailsScreen(
            name = name,
            onNameChange = vm::onNameChange,
            desc = desc,
            onDescChange = vm::onDescChange,
            delay = delay,
            onDelayChange = vm::onDelayChange,
            selectedItemsIdList = selectedItemsIdList,
            onSaveButtonClick = vm::insertOrUpdateItem,
            saveButtonEnabled = !errorState,
            bottomSheetVisible = bottomSheetVisible,
            setBottomSheetVisibility = setBottomSheetVisibility,
            bottomSheetContent = bottomSheetContent,
            setBottomSheetContent = setBottomSheetContent,
            setSelectionModeValueGlobally = setSelectionModeValueGlobally,
            showSnackbar = { showSnackbar(snackbarMessage) },
            navigateToTechniqueScreen = navigateToTechniqueScreen,
            navigateUp = navigateUp
        )
    }

    SurviveProcessDeath(onStop = vm::surviveProcessDeath)
}

@Composable
private fun ComboDetailsScreen(
    name: String,
    onNameChange: (String) -> Unit,
    desc: String,
    onDescChange: (String) -> Unit,
    delay: Int,
    onDelayChange: (Int) -> Unit,
    selectedItemsIdList: List<Long>,
    onSaveButtonClick: () -> Unit,
    saveButtonEnabled: Boolean,
    bottomSheetVisible: Boolean,
    setBottomSheetVisibility: (Boolean) -> Unit,
    bottomSheetContent: Int,
    setBottomSheetContent: (Int) -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    showSnackbar: () -> Unit,
    navigateToTechniqueScreen: () -> Unit,
    navigateUp: () -> Unit
) = DetailsLayout(
    bottomSheetVisible = bottomSheetVisible,
    setBottomSheetVisibility = setBottomSheetVisibility,
    saveButtonEnabled = saveButtonEnabled,
    onSaveButtonClick = {
        onSaveButtonClick(); setSelectionModeValueGlobally(false); showSnackbar(); navigateUp()
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
    selectedItemIds: List<Long>,
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    onNavigateToTechniqueScreen: () -> Unit
) {
    DetailsItem(
        startText = stringResource(R.string.all_name), endText = name
    ) { onBottomSheetContentChange(COMBO_NAME_FIELD); showBottomSheet(true) }
    HorizontalDivider()
    DetailsItem(
        startText = stringResource(R.string.all_desc), endText = desc
    ) { onBottomSheetContentChange(COMBO_DESC_FIELD); showBottomSheet(true) }
    HorizontalDivider()
    DetailsItem(
        startText = stringResource(R.string.combo_details_recovery),
        endText = pluralStringResource(R.plurals.all_second, delay, delay)
    ) { onBottomSheetContentChange(COMBO_DELAY_COUNTER); showBottomSheet(true) }
    HorizontalDivider()
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

    DoubleButtonBottomSheetBox(
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
            onDoneImeAction = { onSaveButtonClick(currentName); onDismissBottomSheet(false) })
    }
}

@Composable
private fun ComboDescTextField(
    desc: String, onSaveButtonClick: (String) -> Unit, onDismissBottomSheet: (Boolean) -> Unit
) {
    var currentDesc by rememberSaveable { mutableStateOf(desc) }
    val errorState by remember { derivedStateOf { currentDesc.length > TEXTFIELD_DESC_MAX_CHARS || currentDesc.isEmpty() } }

    DoubleButtonBottomSheetBox(
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
            leadingIcon = { Icon(painterResource(R.drawable.rounded_comment_24), null) },
            errorList = descFieldError,
            onDoneImeAction = { onSaveButtonClick(currentDesc); onDismissBottomSheet(false) })
    }
}

@Composable
private fun ComboDetailsSlider(
    delaySeconds: Int, onSaveButtonClick: (Int) -> Unit, onDismissBottomSheet: (Boolean) -> Unit
) {
    var currentDelaySeconds by rememberSaveable { mutableFloatStateOf(delaySeconds.toFloat()) }

    DoubleButtonBottomSheetBox(
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
            text = pluralStringResource(
                R.plurals.all_second,
                currentDelaySeconds.roundToInt(),
                currentDelaySeconds.roundToInt()
            ),
            style = TypographyManager.bodySmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}