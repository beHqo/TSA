package com.example.android.strikingarts.ui.techniquedetails

import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.components.ColorPicker
import com.example.android.strikingarts.ui.components.DetailsItemSwitch
import com.example.android.strikingarts.ui.components.FadingAnimatedContent
import com.example.android.strikingarts.ui.components.NameTextField
import com.example.android.strikingarts.ui.components.NumTextField
import com.example.android.strikingarts.ui.components.ProgressBar
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.example.android.strikingarts.ui.components.detailsitem.DetailsItem
import com.example.android.strikingarts.ui.components.detailsitem.SelectableDetailsItem
import com.example.android.strikingarts.ui.parentlayouts.BottomSheetBox
import com.example.android.strikingarts.ui.parentlayouts.DetailsLayout
import com.example.android.strikingarts.utils.ImmutableSet
import com.example.android.strikingarts.utils.TechniqueCategory.DEFENSE
import com.example.android.strikingarts.utils.TechniqueCategory.OFFENSE
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

const val TECHNIQUE_NAME_FIELD = 221
const val TECHNIQUE_NUM_FIELD = 222
const val TECHNIQUE_TECHNIQUE_TYPE = 224
const val TECHNIQUE_COLOR_PICKER = 225

@Composable
fun TechniqueDetailsScreen(
    model: TechniqueDetailsViewModel = hiltViewModel(), navigateUp: () -> Unit
) {
    val loadingScreen by model.loadingScreen.collectAsState()

    if (loadingScreen) ProgressBar() else {
        val name by model.name.collectAsState()
        val num by model.num.collectAsState()
        val techniqueType by model.techniqueType.collectAsState()
        val movementType by model.movementType.collectAsState()
        val color by model.color.collectAsState()
        val techniqueTypeList by model.techniqueTypeList.collectAsState()
        val colorPickerController = rememberColorPickerController()

        val (bottomSheetVisible, setBottomSheetVisibility) = rememberSaveable {
            mutableStateOf(false)
        }
        val (bottomSheetContent, setBottomSheetContent) = rememberSaveable {
            mutableStateOf(TECHNIQUE_NAME_FIELD)
        }

        val errorState by remember {
            derivedStateOf {
                name.length > TEXTFIELD_NAME_MAX_CHARS || num.isNotEmpty() && !num.isDigitsOnly() || name.isEmpty() || techniqueType.isEmpty()
            }
        }

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
    colorPickerController: ColorPickerController,
    saveButtonEnabled: Boolean,
    bottomSheetVisible: Boolean,
    setBottomSheetVisibility: (Boolean) -> Unit,
    bottomSheetContent: Int,
    setBottomSheetContent: (Int) -> Unit,
    onSaveButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    DetailsLayout(bottomSheetVisible = bottomSheetVisible,
        onDismissBottomSheet = setBottomSheetVisibility,
        saveButtonEnabled = saveButtonEnabled,
        onSaveButtonClick = { onSaveButtonClick(); navigateUp() },
        onDiscardButtonClick = navigateUp,
        bottomSheetContent = {
            when (bottomSheetContent) {
                TECHNIQUE_NAME_FIELD -> TechniqueNameTextField(
                    name, onNameChange, setBottomSheetVisibility
                )

                TECHNIQUE_TECHNIQUE_TYPE -> TechniqueType(
                    techniqueType,
                    techniqueTypeList,
                    onTechniqueTypeChange,
                    setBottomSheetVisibility
                )

                TECHNIQUE_NUM_FIELD -> TechniqueNumField(
                    num, onNumChange, setBottomSheetVisibility
                )

                TECHNIQUE_COLOR_PICKER -> TechniqueColorPicker(
                    colorPickerController, onColorChange, setBottomSheetVisibility
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
            setBottomSheetContent = setBottomSheetContent,
            setBottomSheetVisibility = setBottomSheetVisibility
        )
    }
}

@Composable
fun TechniqueDetailsColumnContent(
    name: String,
    num: String,
    movementType: String,
    onMovementTypeChange: (String) -> Unit,
    techniqueType: String,
    color: String,
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
        startText = stringResource(R.string.techniquedetails_technique_category),
        endText = techniqueType
    ) { setBottomSheetContent(TECHNIQUE_TECHNIQUE_TYPE); setBottomSheetVisibility(true) }
    Divider()

    DetailsItem(
        startText = stringResource(R.string.techniquedetails_textfield_name_helper), endText = name
    ) { setBottomSheetContent(TECHNIQUE_NAME_FIELD); setBottomSheetVisibility(true) }
    Divider()

    FadingAnimatedContent(targetState = (movementType == DEFENSE), currentStateComponent = {
        DetailsItem(
            startText = stringResource(R.string.techniquedetails_numfield_helper), endText = num
        ) { setBottomSheetContent(TECHNIQUE_NUM_FIELD); setBottomSheetVisibility(true) }
    }, targetStateComponent = {
        DetailsItem(
            startText = stringResource(R.string.techniquedetails_modify_technique_color),
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

    BottomSheetBox(onDismissBottomSheet = onDismissBottomSheet,
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

    BottomSheetBox(onDismissBottomSheet = onDismissBottomSheet,
        saveButtonEnabled = !errorState,
        onSaveButtonClick = { onNameChange(currentName) }) {
        NameTextField(
            value = currentName,
            onValueChange = { if (it.length <= TEXTFIELD_NAME_MAX_CHARS) currentName = it },
            maxChars = TEXTFIELD_NAME_MAX_CHARS,
            label = stringResource(R.string.techniquedetails_textfield_name_label),
            placeHolder = stringResource(R.string.techniquedetails_textfield_name_hint),
            leadingIcon = R.drawable.ic_glove_filled_light,
            helperText = stringResource(R.string.techniquedetails_textfield_name_helper),
            imeAction = ImeAction.Next
        )
    }
}

@Composable
private fun TechniqueNumField(
    num: String, onNumChange: (String) -> Unit, onDismissBottomSheet: (Boolean) -> Unit
) {
    var currentNum by rememberSaveable { mutableStateOf(num) }
    val errorState by remember { derivedStateOf { !currentNum.isDigitsOnly() || currentNum.isEmpty() } }

    BottomSheetBox(onDismissBottomSheet = onDismissBottomSheet,
        saveButtonEnabled = !errorState,
        onSaveButtonClick = { onNumChange(currentNum) }) {
        NumTextField(
            value = currentNum,
            onValueChange = { if (it.isDigitsOnly()) currentNum = it },
            label = stringResource(R.string.techniquedetails_numfield_label),
            placeHolder = stringResource(R.string.techniquedetails_numfield_hint),
            leadingIcon = R.drawable.ic_label_filled_light,
            helperText = stringResource(R.string.techniquedetails_numfield_helper),
            imeAction = ImeAction.Next,
        )
    }
}

@Composable
private fun TechniqueColorPicker(
    colorPickerController: ColorPickerController,
    onColorChange: (String) -> Unit,
    onDismissBottomSheet: (Boolean) -> Unit,
) {
    BottomSheetBox(onDismissBottomSheet = onDismissBottomSheet,
        saveButtonEnabled = true,
        onSaveButtonClick = { onColorChange(colorPickerController.selectedColor.value.value.toString()) }) {
        ColorPicker(colorPickerController)
    }
}
