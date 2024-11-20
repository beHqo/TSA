package com.github.tsa.ui.techniquedetails

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.github.tsa.R
import com.github.tsa.domain.constant.transparentHexCode
import com.github.tsa.domain.model.MovementType
import com.github.tsa.domain.model.TechniqueType
import com.github.tsa.ui.components.ColorPicker
import com.github.tsa.ui.components.CustomTextField
import com.github.tsa.ui.components.DetailsItemSwitch
import com.github.tsa.ui.components.DoubleButtonBottomSheetBox
import com.github.tsa.ui.components.LocalSoundPickerDialog
import com.github.tsa.ui.components.NumTextField
import com.github.tsa.ui.components.PrimaryText
import com.github.tsa.ui.components.ProgressBar
import com.github.tsa.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.github.tsa.ui.components.detailsitem.ClickableDetailsItem
import com.github.tsa.ui.components.detailsitem.DetailsItem
import com.github.tsa.ui.components.detailsitem.SelectableDetailsItem
import com.github.tsa.ui.model.UriConditions
import com.github.tsa.ui.parentlayouts.DetailsLayout
import com.github.tsa.ui.techniquedetails.TechniqueDetailsViewModel.Companion.MAX_AUDIO_LENGTH_SEC
import com.github.tsa.ui.techniquedetails.TechniqueDetailsViewModel.Companion.MAX_FILE_SIZE_MB
import com.github.tsa.ui.techniquedetails.TechniqueDetailsViewModel.Companion.MIME_TYPE
import com.github.tsa.ui.theme.designsystemmanager.ColorManager
import com.github.tsa.ui.theme.designsystemmanager.PaddingManager
import com.github.tsa.ui.util.SurviveProcessDeath
import com.github.tsa.ui.util.getLocalizedName
import com.github.tsa.ui.util.toComposeColor
import com.github.tsa.ui.util.toHexCode

const val TECHNIQUE_NAME_FIELD = 221
const val TECHNIQUE_NUM_FIELD = 222
const val TECHNIQUE_TECHNIQUE_TYPE = 224
const val TECHNIQUE_COLOR_PICKER = 225
const val TECHNIQUE_SOUND_PICKER = 226

@Composable
fun TechniqueDetailsScreen(
    vm: TechniqueDetailsViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    showSnackbar: (String) -> Unit
) {
    val loadingScreen by vm.loadingScreen.collectAsStateWithLifecycle()

    if (loadingScreen) ProgressBar() else {
        val isTechniqueNew = vm.isTechniqueNew
        val name by vm.name.collectAsStateWithLifecycle()
        val num by vm.num.collectAsStateWithLifecycle()
        val techniqueType by vm.techniqueType.collectAsStateWithLifecycle()
        val movementType by vm.movementType.collectAsStateWithLifecycle()
        val color by vm.color.collectAsStateWithLifecycle()
        val uriCondition by vm.uriCondition.collectAsStateWithLifecycle()
        val audioAttributes by vm.audioAttributes.collectAsStateWithLifecycle()

        val colorPickerController = rememberColorPickerController()

        val uriSizeErrorString =
            stringResource(R.string.technique_details_file_picker_size_error, MAX_FILE_SIZE_MB)
        val uriDurationErrorString = stringResource(
            R.string.technique_details_file_picker_duration_error, MAX_AUDIO_LENGTH_SEC
        )
        val uriMissingErrorString = stringResource(R.string.technique_details_uri_missing)

        val soundMessage by remember {
            derivedStateOf {
                when (uriCondition) {
                    UriConditions.SIZE_ERROR -> uriSizeErrorString
                    UriConditions.DURATION_ERROR -> uriDurationErrorString
                    UriConditions.MISSING -> uriMissingErrorString
                    UriConditions.VALID -> audioAttributes.name
                }
            }
        }

        val errorColor = ColorManager.error
        val validColor = ColorManager.onBackground
        val soundMessageColor by remember { derivedStateOf { if (uriCondition != UriConditions.VALID) errorColor else validColor } }

        val soundPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument(), onResult = vm::handleSelectedUri
        )

        val (bottomSheetVisible, setBottomSheetVisibility) = rememberSaveable { mutableStateOf(false) }
        val (bottomSheetContent, setBottomSheetContent) = rememberSaveable {
            mutableIntStateOf(TECHNIQUE_NAME_FIELD)
        }

        val errorState by remember { derivedStateOf { name.length > TEXTFIELD_NAME_MAX_CHARS || num.isNotEmpty() && !num.isDigitsOnly() || name.isEmpty() || movementType == MovementType.DEFENSE && color == transparentHexCode || uriCondition != UriConditions.VALID } }

        val (localSoundPickerDialogVisible, setLocalSoundPickerDialogVisibility) = rememberSaveable {
            mutableStateOf(false)
        }

        if (localSoundPickerDialogVisible) LocalSoundPickerDialog(
            onDismiss = setLocalSoundPickerDialogVisibility,
            setAudioFileName = vm::setResourceAudioString,
            playTechnique = vm::play
        )

        val snackbarMessage = if (isTechniqueNew) stringResource(
            R.string.all_snackbar_insert, name
        ) else stringResource(R.string.all_snackbar_update, name)

        val techniqueTypeList by remember { derivedStateOf { if (movementType == MovementType.OFFENSE) TechniqueType.offenseTypes else TechniqueType.defenseTypes } }

        TechniqueDetailsScreen(
            name = name,
            onNameChange = vm::onNameChange,
            num = num,
            onNumChange = vm::onNumChange,
            techniqueType = techniqueType,
            onTechniqueTypeChange = vm::onTechniqueTypeChange,
            movementType = movementType,
            onMovementTypeChange = vm::onMovementTypeChange,
            techniqueTypeList = techniqueTypeList,
            color = color,
            onColorChange = vm::onColorChange,
            launchSoundPicker = { soundPickerLauncher.launch(arrayOf(MIME_TYPE)) },
            setLocalSoundPickerDialogVisibility = setLocalSoundPickerDialogVisibility,
            uriCondition = uriCondition,
            soundMessage = soundMessage,
            soundMessageColor = soundMessageColor,
            resetUriString = vm::resetUriString,
            colorPickerController = colorPickerController,
            saveButtonEnabled = !errorState,
            bottomSheetVisible = bottomSheetVisible,
            setBottomSheetVisibility = setBottomSheetVisibility,
            bottomSheetContent = bottomSheetContent,
            setBottomSheetContent = setBottomSheetContent,
            onSaveButtonClick = vm::insertOrUpdateItem,
            showSnackbar = { showSnackbar(snackbarMessage) },
            navigateUp = navigateUp
        )
    }

    SurviveProcessDeath(onStop = vm::surviveProcessDeath)
}

@Composable
private fun TechniqueDetailsScreen(
    name: String,
    onNameChange: (String) -> Unit,
    num: String,
    onNumChange: (String) -> Unit,
    techniqueType: TechniqueType,
    onTechniqueTypeChange: (TechniqueType) -> Unit,
    movementType: MovementType,
    onMovementTypeChange: (MovementType) -> Unit,
    techniqueTypeList: List<TechniqueType>,
    color: String,
    onColorChange: (String) -> Unit,
    launchSoundPicker: () -> Unit,
    setLocalSoundPickerDialogVisibility: (Boolean) -> Unit,
    uriCondition: UriConditions,
    soundMessage: String,
    soundMessageColor: Color,
    resetUriString: () -> Unit,
    colorPickerController: ColorPickerController,
    saveButtonEnabled: Boolean,
    bottomSheetVisible: Boolean,
    setBottomSheetVisibility: (Boolean) -> Unit,
    bottomSheetContent: Int,
    setBottomSheetContent: (Int) -> Unit,
    onSaveButtonClick: () -> Unit,
    showSnackbar: () -> Unit,
    navigateUp: () -> Unit
) = DetailsLayout(
    bottomSheetVisible = bottomSheetVisible,
    setBottomSheetVisibility = setBottomSheetVisibility,
    saveButtonEnabled = saveButtonEnabled,
    onSaveButtonClick = { onSaveButtonClick(); showSnackbar(); navigateUp() },
    onDiscardButtonClick = navigateUp,
    bottomSheetContent = {
        when (bottomSheetContent) {
            TECHNIQUE_NAME_FIELD -> TechniqueNameTextField(
                name, onNameChange, setBottomSheetVisibility
            )

            TECHNIQUE_TECHNIQUE_TYPE -> TechniqueTypePicker(
                techniqueType, techniqueTypeList, onTechniqueTypeChange, setBottomSheetVisibility
            )

            TECHNIQUE_NUM_FIELD -> TechniqueNumField(
                num, onNumChange, setBottomSheetVisibility
            )

            TECHNIQUE_COLOR_PICKER -> TechniqueColorPicker(
                colorPickerController, onColorChange, setBottomSheetVisibility
            )

            TECHNIQUE_SOUND_PICKER -> SoundPicker(
                soundMessage = soundMessage,
                soundMessageColor = soundMessageColor,
                uriCondition = uriCondition,
                resetUriString = resetUriString,
                launchSoundPicker = launchSoundPicker,
                setLocalSoundPickerDialogVisibility = setLocalSoundPickerDialogVisibility,
                onDismissBottomSheet = setBottomSheetVisibility
            )
        }
    }) {
    TechniqueDetailsColumnContent(
        name = name,
        num = num,
        movementType = movementType,
        onMovementTypeChange = onMovementTypeChange,
        techniqueType = techniqueType,
        color = color,
        soundName = soundMessage,
        setBottomSheetContent = setBottomSheetContent,
        setBottomSheetVisibility = setBottomSheetVisibility
    )
}

@Composable
fun TechniqueDetailsColumnContent(
    name: String,
    num: String,
    movementType: MovementType,
    onMovementTypeChange: (MovementType) -> Unit,
    techniqueType: TechniqueType,
    color: String,
    soundName: String,
    setBottomSheetContent: (Int) -> Unit,
    setBottomSheetVisibility: (Boolean) -> Unit
) {
    DetailsItemSwitch(
        initialValue = movementType,
        startingItem = MovementType.OFFENSE,
        endingItem = MovementType.DEFENSE,
        startingText = stringResource(R.string.all_offense),
        endingText = stringResource(R.string.all_defense),
        onSelectionChange = onMovementTypeChange
    )
    HorizontalDivider()

    DetailsItem(
        startText = stringResource(R.string.technique_details_technique_category),
        endText = techniqueType.getLocalizedName()
    ) { setBottomSheetContent(TECHNIQUE_TECHNIQUE_TYPE); setBottomSheetVisibility(true) }
    HorizontalDivider()

    DetailsItem(
        startText = stringResource(R.string.technique_details_textfield_name_helper), endText = name
    ) { setBottomSheetContent(TECHNIQUE_NAME_FIELD); setBottomSheetVisibility(true) }
    HorizontalDivider()

    DetailsItem(
        startText = stringResource(R.string.technique_details_numfield_helper),
        endText = num
    ) { setBottomSheetContent(TECHNIQUE_NUM_FIELD); setBottomSheetVisibility(true) }
    HorizontalDivider()

    DetailsItem(
        startText = stringResource(R.string.technique_details_sound),
        endText = soundName
    ) {
        setBottomSheetContent(TECHNIQUE_SOUND_PICKER); setBottomSheetVisibility(true)
    }
    HorizontalDivider()

    DetailsItem(
        startText = stringResource(R.string.technique_details_modify_technique_color),
        color = color.toComposeColor()
    ) { setBottomSheetContent(TECHNIQUE_COLOR_PICKER); setBottomSheetVisibility(true) }
}

@Composable
private fun TechniqueTypePicker(
    techniqueType: TechniqueType,
    techniqueTypeList: List<TechniqueType>,
    onTechniqueTypeChange: (TechniqueType) -> Unit,
    onDismissBottomSheet: (Boolean) -> Unit
) {
    var currentTechniqueType by rememberSaveable { mutableStateOf(techniqueType) }

    DoubleButtonBottomSheetBox(
        setBottomSheetVisibility = onDismissBottomSheet,
        saveButtonEnabled = true,
        onSaveButtonClick = { onTechniqueTypeChange(currentTechniqueType) }) {
        techniqueTypeList.forEach {
            SelectableDetailsItem(
                text = it.getLocalizedName(),
                selected = it == currentTechniqueType,
                onSelectionChange = { currentTechniqueType = it })
        }
    }
}

@Composable
private fun TechniqueNameTextField(
    name: String, onNameChange: (String) -> Unit, onDismissBottomSheet: (Boolean) -> Unit,
) {
    var currentName by rememberSaveable { mutableStateOf(name) }
    val errorState by remember {
        derivedStateOf { currentName.length > TEXTFIELD_NAME_MAX_CHARS || currentName.isEmpty() }
    }

    DoubleButtonBottomSheetBox(
        setBottomSheetVisibility = onDismissBottomSheet,
        saveButtonEnabled = !errorState,
        onSaveButtonClick = { onNameChange(currentName) }) {
        CustomTextField(value = currentName,
            onValueChange = { if (it.length <= TEXTFIELD_NAME_MAX_CHARS + 1) currentName = it },
            maxChars = TEXTFIELD_NAME_MAX_CHARS,
            label = stringResource(R.string.all_name),
            placeHolder = stringResource(R.string.technique_details_textfield_name_hint),
            helperText = stringResource(R.string.technique_details_textfield_name_helper),
            onDoneImeAction = { onNameChange(currentName); onDismissBottomSheet(false) })
    }
}

@Composable
private fun TechniqueNumField(
    num: String, onNumChange: (String) -> Unit, onDismissBottomSheet: (Boolean) -> Unit
) {
    var currentNum by rememberSaveable { mutableStateOf(num) }
    val errorState by remember { derivedStateOf { !currentNum.isDigitsOnly() || currentNum.isEmpty() } }

    DoubleButtonBottomSheetBox(
        setBottomSheetVisibility = onDismissBottomSheet,
        saveButtonEnabled = !errorState,
        onSaveButtonClick = { onNumChange(currentNum) }) {
        NumTextField(value = currentNum,
            onValueChange = { if (it.isDigitsOnly()) currentNum = it },
            label = stringResource(R.string.technique_details_numfield_label),
            placeHolder = stringResource(R.string.technique_details_numfield_hint),
            leadingIcon = { Icon(painterResource(R.drawable.rounded_label_24), null) },
            helperText = stringResource(R.string.technique_details_numfield_helper),
            onDoneImeAction = { onNumChange(currentNum); onDismissBottomSheet(false) })
    }
}

@Composable
private fun TechniqueColorPicker(
    colorPickerController: ColorPickerController,
    onColorChange: (String) -> Unit,
    onDismissBottomSheet: (Boolean) -> Unit,
) = DoubleButtonBottomSheetBox(
    setBottomSheetVisibility = onDismissBottomSheet,
    saveButtonEnabled = true,
    onSaveButtonClick = { onColorChange(colorPickerController.selectedColor.value.toHexCode()) }) {
    ColorPicker(colorPickerController)
}

@Composable
private fun SoundPicker(
    soundMessage: String,
    soundMessageColor: Color,
    uriCondition: UriConditions,
    resetUriString: () -> Unit,
    launchSoundPicker: () -> Unit,
    setLocalSoundPickerDialogVisibility: (Boolean) -> Unit,
    onDismissBottomSheet: (Boolean) -> Unit
) {
    val errorState by remember(soundMessage) {
        derivedStateOf { soundMessage.isEmpty() || uriCondition != UriConditions.VALID }
    }

    DoubleButtonBottomSheetBox(
        setBottomSheetVisibility = onDismissBottomSheet,
        saveButtonEnabled = !errorState,
        onDiscardButtonClick = { resetUriString() }) {
        PrimaryText(
            text = soundMessage.ifEmpty { stringResource(R.string.technique_details_select_an_audio_file) },
            color = soundMessageColor,
            maxLines = Int.MAX_VALUE,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = PaddingManager.Large)
                .align(Alignment.CenterHorizontally)
        )
        ClickableDetailsItem(stringResource(R.string.technique_details_select_pre_recorded_audio)) {
            setLocalSoundPickerDialogVisibility(true)
        }
        ClickableDetailsItem(
            text = stringResource(R.string.technique_details_select_local_audio),
            onClick = launchSoundPicker
        )
    }
}