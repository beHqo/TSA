package com.example.android.strikingarts.ui.techniquedetails

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
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
import com.example.android.strikingarts.R
import com.example.android.strikingarts.domain.model.ImmutableSet
import com.example.android.strikingarts.domain.model.TechniqueCategory.DEFENSE
import com.example.android.strikingarts.domain.model.TechniqueCategory.OFFENSE
import com.example.android.strikingarts.ui.components.ColorPicker
import com.example.android.strikingarts.ui.components.CustomTextField
import com.example.android.strikingarts.ui.components.DetailsItemSwitch
import com.example.android.strikingarts.ui.components.DoubleButtonBottomSheetBox
import com.example.android.strikingarts.ui.components.FadingAnimatedContent
import com.example.android.strikingarts.ui.components.LocalSoundPickerDialog
import com.example.android.strikingarts.ui.components.NumTextField
import com.example.android.strikingarts.ui.components.PrimaryText
import com.example.android.strikingarts.ui.components.ProgressBar
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.example.android.strikingarts.ui.components.detailsitem.ClickableDetailsItem
import com.example.android.strikingarts.ui.components.detailsitem.DetailsItem
import com.example.android.strikingarts.ui.components.detailsitem.SelectableDetailsItem
import com.example.android.strikingarts.ui.components.util.SurviveProcessDeath
import com.example.android.strikingarts.ui.model.UriConditions
import com.example.android.strikingarts.ui.parentlayouts.DetailsLayout
import com.example.android.strikingarts.ui.techniquedetails.TechniqueDetailsViewModel.Companion.MAX_AUDIO_LENGTH_SEC
import com.example.android.strikingarts.ui.techniquedetails.TechniqueDetailsViewModel.Companion.MAX_FILE_SIZE_MB
import com.example.android.strikingarts.ui.techniquedetails.TechniqueDetailsViewModel.Companion.MIME_TYPE
import com.example.android.strikingarts.ui.techniquedetails.TechniqueDetailsViewModel.Companion.TRANSPARENT_COLOR_VALUE
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

const val TECHNIQUE_NAME_FIELD = 221
const val TECHNIQUE_NUM_FIELD = 222
const val TECHNIQUE_TECHNIQUE_TYPE = 224
const val TECHNIQUE_COLOR_PICKER = 225
const val TECHNIQUE_SOUND_PICKER = 226

@Composable
fun TechniqueDetailsScreen(
    model: TechniqueDetailsViewModel = hiltViewModel(), navigateUp: () -> Unit
) {
    val loadingScreen by model.loadingScreen.collectAsStateWithLifecycle()

    if (loadingScreen) ProgressBar() else {
        val name by model.name.collectAsStateWithLifecycle()
        val num by model.num.collectAsStateWithLifecycle()
        val techniqueType by model.techniqueType.collectAsStateWithLifecycle()
        val movementType by model.movementType.collectAsStateWithLifecycle()
        val color by model.color.collectAsStateWithLifecycle()
        val uriCondition by model.uriCondition.collectAsStateWithLifecycle()
        val audioAttributes by model.audioAttributes.collectAsStateWithLifecycle()
        val techniqueTypeList by model.techniqueTypeList.collectAsStateWithLifecycle()

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
            contract = ActivityResultContracts.OpenDocument(), onResult = model::handleSelectedUri
        )

        val (bottomSheetVisible, setBottomSheetVisibility) = rememberSaveable { mutableStateOf(false) }
        val (bottomSheetContent, setBottomSheetContent) = rememberSaveable {
            mutableIntStateOf(TECHNIQUE_NAME_FIELD)
        }

        val errorState by remember { derivedStateOf { name.length > TEXTFIELD_NAME_MAX_CHARS || num.isNotEmpty() && !num.isDigitsOnly() || name.isEmpty() || techniqueType.isEmpty() || movementType == DEFENSE && color == TRANSPARENT_COLOR_VALUE || uriCondition != UriConditions.VALID } }

        val (localSoundPickerDialogVisible, setLocalSoundPickerDialogVisibility) = rememberSaveable {
            mutableStateOf(false)
        }

        if (localSoundPickerDialogVisible) LocalSoundPickerDialog(
            onDismiss = setLocalSoundPickerDialogVisibility,
            setAudioFileName = model::setAssetAudioString,
            playTechnique = model::play
        )

        TechniqueDetailsScreen(
            name = name,
            onNameChange = model::onNameChange,
            num = num,
            onNumChange = model::onNumChange,
            techniqueType = techniqueType,
            onTechniqueTypeChange = model::onTechniqueTypeChange,
            movementType = movementType,
            onMovementTypeChange = model::onMovementTypeChange,
            techniqueTypeList = techniqueTypeList,
            color = color,
            onColorChange = model::onColorChange,
            launchSoundPicker = { soundPickerLauncher.launch(arrayOf(MIME_TYPE)) },
            setLocalSoundPickerDialogVisibility = setLocalSoundPickerDialogVisibility,
            uriCondition = uriCondition,
            soundMessage = soundMessage,
            soundMessageColor = soundMessageColor,
            resetUriString = model::resetUriString,
            colorPickerController = colorPickerController,
            saveButtonEnabled = !errorState,
            bottomSheetVisible = bottomSheetVisible,
            setBottomSheetVisibility = setBottomSheetVisibility,
            bottomSheetContent = bottomSheetContent,
            setBottomSheetContent = setBottomSheetContent,
            onSaveButtonClick = model::insertOrUpdateItem,
            navigateUp = navigateUp
        )
    }

    SurviveProcessDeath(onStop = model::surviveProcessDeath)
}

@Composable
private fun TechniqueDetailsScreen(
    name: String,
    onNameChange: (String) -> Unit,
    num: String,
    onNumChange: (String) -> Unit,
    techniqueType: String,
    onTechniqueTypeChange: (String) -> Unit,
    movementType: String,
    onMovementTypeChange: (String) -> Unit,
    techniqueTypeList: ImmutableSet<String>,
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
    navigateUp: () -> Unit
) = DetailsLayout(
    bottomSheetVisible = bottomSheetVisible,
    setBottomSheetVisibility = setBottomSheetVisibility,
    saveButtonEnabled = saveButtonEnabled,
    onSaveButtonClick = { onSaveButtonClick(); navigateUp() },
    onDiscardButtonClick = navigateUp,
    bottomSheetContent = {
        when (bottomSheetContent) {
            TECHNIQUE_NAME_FIELD -> TechniqueNameTextField(
                name, onNameChange, setBottomSheetVisibility
            )

            TECHNIQUE_TECHNIQUE_TYPE -> TechniqueType(
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
    movementType: String,
    onMovementTypeChange: (String) -> Unit,
    techniqueType: String,
    color: String,
    soundName: String,
    setBottomSheetContent: (Int) -> Unit,
    setBottomSheetVisibility: (Boolean) -> Unit
) {
    DetailsItemSwitch(
        initialValue = movementType,
        startingItemText = OFFENSE,
        endingItemText = DEFENSE,
        onSelectionChange = onMovementTypeChange
    )
    Divider()

    DetailsItem(
        startText = stringResource(R.string.technique_details_technique_category),
        endText = techniqueType
    ) { setBottomSheetContent(TECHNIQUE_TECHNIQUE_TYPE); setBottomSheetVisibility(true) }
    Divider()

    DetailsItem(
        startText = stringResource(R.string.technique_details_textfield_name_helper), endText = name
    ) { setBottomSheetContent(TECHNIQUE_NAME_FIELD); setBottomSheetVisibility(true) }
    Divider()

    FadingAnimatedContent(targetState = (movementType == DEFENSE), currentStateComponent = {
        Column {
            DetailsItem(
                startText = stringResource(R.string.technique_details_numfield_helper),
                endText = num
            ) { setBottomSheetContent(TECHNIQUE_NUM_FIELD); setBottomSheetVisibility(true) }
            Divider()

            DetailsItem(startText = "Sound", endText = soundName) {
                setBottomSheetContent(TECHNIQUE_SOUND_PICKER); setBottomSheetVisibility(true)
            }
        }
    }, targetStateComponent = {
        DetailsItem(
            startText = stringResource(R.string.technique_details_modify_technique_color),
            color = Color(color.toULong())
        ) { setBottomSheetContent(TECHNIQUE_COLOR_PICKER); setBottomSheetVisibility(true) }
    })
}

@Composable
private fun TechniqueType(
    techniqueType: String,
    techniqueTypeList: ImmutableSet<String>,
    onTechniqueTypeChange: (String) -> Unit,
    onDismissBottomSheet: (Boolean) -> Unit
) {
    var currentTechniqueType by rememberSaveable { mutableStateOf(techniqueType) }

    DoubleButtonBottomSheetBox(
        setBottomSheetVisibility = onDismissBottomSheet,
        saveButtonEnabled = true,
        onSaveButtonClick = { onTechniqueTypeChange(currentTechniqueType) }) {
        techniqueTypeList.forEach {
            SelectableDetailsItem(text = it,
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
            leadingIcon = { Icon(painterResource(R.drawable.ic_glove_filled_light), null) },
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
            leadingIcon = { Icon(painterResource(R.drawable.ic_label_filled_light), null) },
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
    onSaveButtonClick = { onColorChange(colorPickerController.selectedColor.value.value.toString()) }) {
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