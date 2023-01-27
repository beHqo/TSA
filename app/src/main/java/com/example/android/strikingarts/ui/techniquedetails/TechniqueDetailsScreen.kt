package com.example.android.strikingarts.ui.techniquedetails

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberSwipeableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.ui.parentlayouts.BottomSheetBox
import com.example.android.strikingarts.ui.parentlayouts.DetailsLayout
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.components.ColorPicker
import com.example.android.strikingarts.ui.components.DetailsItem
import com.example.android.strikingarts.ui.components.DetailsItemSwitch
import com.example.android.strikingarts.ui.components.NameTextField
import com.example.android.strikingarts.ui.components.NumTextField
import com.example.android.strikingarts.ui.components.RadioButtonWithName
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.example.android.strikingarts.ui.components.TextFieldItemDropdown
import com.example.android.strikingarts.utils.ImmutableSet
import com.example.android.strikingarts.utils.TechniqueCategory.DEFENSE
import com.example.android.strikingarts.utils.TechniqueCategory.OFFENSE
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import kotlinx.coroutines.launch

const val TECHNIQUE_NAME_FIELD = 221
const val TECHNIQUE_NUM_FIELD = 222
const val TECHNIQUE_MOVEMENT_TYPE = 223
const val TECHNIQUE_TECHNIQUE_TYPE = 224
const val TECHNIQUE_COLOR_PICKER = 225

@Composable
fun TechniqueDetailsScreen(
    model: TechniqueDetailsViewModel = hiltViewModel(), navigateUp: () -> Unit
) {
    val state by model.uiState.collectAsState()
    val colorPickerController = rememberColorPickerController()

    val (bottomSheetVisible, setBottomSheetVisibility) = rememberSaveable { mutableStateOf(false) }

    val (bottomSheetContent, setBottomSheetContent) = rememberSaveable { mutableStateOf(0) }

    val errorState by remember {
        derivedStateOf {
            state.name.length > TEXTFIELD_NAME_MAX_CHARS || !state.num.isDigitsOnly() || state.name.isEmpty() || state.techniqueType.isEmpty()
        }
    }

    DetailsLayout(bottomSheetVisible = bottomSheetVisible,
        onDismissBottomSheet = setBottomSheetVisibility,
        saveButtonEnabled = !errorState,
        onSaveButtonClick = { model.onSaveButtonClick(); navigateUp() },
        onDiscardButtonClick = navigateUp,
        bottomSheetContent = {
            when (bottomSheetContent) {
                TECHNIQUE_NAME_FIELD -> TechniqueNameTextField(
                    model::onNameChange, setBottomSheetVisibility
                )

                TECHNIQUE_NUM_FIELD -> TechniqueNumField(
                    model::onNumChange, setBottomSheetVisibility
                )

//                TECHNIQUE_MOVEMENT_TYPE -> MovementType(
//                    model::onMovementButtonClick, setBottomSheetVisibility
//                )

                TECHNIQUE_TECHNIQUE_TYPE -> TechniqueType(
                    state.techniqueTypes, model::onTechniqueTypeChange, setBottomSheetVisibility
                )

                TECHNIQUE_COLOR_PICKER -> TechniqueColorPicker(
                    colorPickerController, model::onColorChange, setBottomSheetVisibility
                )
            }

        }) {
        TechniqueDetailsColumnContent(
            name = state.name,
            num = state.num,
            movementType = state.movementType,
            onMovementTypeChange = model::onMovementButtonClick,
            techniqueType = state.techniqueType,
            color = state.color,
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
    MovementTypeSwitch(onSwitchValueChange = onMovementTypeChange)

    DetailsItem(
        startText = stringResource(R.string.techniquedetails_technique_category),
        endText = movementType
    ) { setBottomSheetContent(TECHNIQUE_MOVEMENT_TYPE); setBottomSheetVisibility(true) }
    Divider()

    DetailsItem(
        startText = stringResource(R.string.techniquedetails_textfield_name_helper), endText = name
    ) { setBottomSheetContent(TECHNIQUE_NAME_FIELD); setBottomSheetVisibility(true) }
    Divider()

    if (movementType == OFFENSE) {
        DetailsItem(
            startText = stringResource(R.string.techniquedetails_numfield_helper), endText = num
        ) { setBottomSheetContent(TECHNIQUE_NUM_FIELD); setBottomSheetVisibility(true) }
        Divider()
    }

    DetailsItem(
        startText = stringResource(R.string.techniquedetails_technique_type),
        endText = techniqueType
    ) { setBottomSheetContent(TECHNIQUE_TECHNIQUE_TYPE); setBottomSheetVisibility(true) }
    Divider()

    if (movementType == DEFENSE) DetailsItem(
        startText = stringResource(R.string.techniquedetails_modify_technique_color),
        color = Color(color.toULong())
    ) { setBottomSheetContent(TECHNIQUE_COLOR_PICKER); setBottomSheetVisibility(true) }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MovementTypeSwitch(onSwitchValueChange: (String) -> Unit) {
    val swipeableState = rememberSwipeableState(initialValue = DEFENSE, animationSpec = tween(150))
    val coroutineScope = rememberCoroutineScope()
    val onSelectionChange = { newValue: String ->
        coroutineScope.launch {
            swipeableState.animateTo(newValue)
        }
        onSwitchValueChange(newValue)
    }

    DetailsItemSwitch(
        startingItemText = DEFENSE,
        endingItemText = OFFENSE,
        swipeableState = swipeableState,
        onSelectionChange = onSelectionChange
    )
}

//@Composable
//private fun MovementType(
//    onMovementButtonClick: (String) -> Unit, onDismissBottomSheet: (Boolean) -> Unit
//) {
//    var movementType by rememberSaveable { mutableStateOf("") }
//    val saveButtonEnabled by remember { derivedStateOf { movementType == OFFENSE || movementType == DEFENSE } }
//
//    BottomSheetBox(onDismissBottomSheet = onDismissBottomSheet,
//        saveButtonEnabled = saveButtonEnabled,
//        onSaveButtonClick = { onMovementButtonClick(movementType) }) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            RadioButtonWithName(
//                selected = movementType == DEFENSE,
//                onClick = { movementType = DEFENSE },
//                name = stringResource(R.string.techniquedetails_defense)
//            )
//
//            RadioButtonWithName(
//                selected = movementType == OFFENSE,
//                onClick = { movementType = OFFENSE },
//                name = stringResource(R.string.techniquedetails_offense)
//            )
//        }
//    }
//}

@Composable
private fun TechniqueNameTextField(
    onNameChange: (String) -> Unit, onDismissBottomSheet: (Boolean) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    val errorState by remember {
        derivedStateOf { name.length > TEXTFIELD_NAME_MAX_CHARS || name.isEmpty() }
    }

    BottomSheetBox(onDismissBottomSheet = onDismissBottomSheet,
        saveButtonEnabled = !errorState,
        onSaveButtonClick = { onNameChange(name) }) {
        NameTextField(
            value = name,
            onValueChange = { if (it.length <= TEXTFIELD_NAME_MAX_CHARS) name = it },
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
    onNumChange: (String) -> Unit, onDismissBottomSheet: (Boolean) -> Unit
) {
    var num by rememberSaveable { mutableStateOf("") }
    val errorState by remember { derivedStateOf { !num.isDigitsOnly() || num.isEmpty() } }

    BottomSheetBox(onDismissBottomSheet = onDismissBottomSheet,
        saveButtonEnabled = !errorState,
        onSaveButtonClick = { onNumChange(num) }) {
        NumTextField(
            value = num,
            onValueChange = { if (it.isDigitsOnly()) num = it },
            label = stringResource(R.string.techniquedetails_numfield_label),
            placeHolder = stringResource(R.string.techniquedetails_numfield_hint),
            leadingIcon = R.drawable.ic_label_filled_light,
            helperText = stringResource(R.string.techniquedetails_numfield_helper),
            imeAction = ImeAction.Next,
        )
    }
}

@Composable
private fun TechniqueType(
    techniqueTypeList: ImmutableSet<String>,
    onTechniqueTypeChange: (String) -> Unit,
    onDismissBottomSheet: (Boolean) -> Unit
) {
    var techniqueType by rememberSaveable { mutableStateOf("") }

    BottomSheetBox(onDismissBottomSheet = onDismissBottomSheet,
        saveButtonEnabled = true,
        onSaveButtonClick = { onTechniqueTypeChange(techniqueType) }) {
        TextFieldItemDropdown(modifier = Modifier.fillMaxWidth(),
            textFieldValue = techniqueType,
            textFieldLabel = stringResource(R.string.techniquedetails_technique_type),
            techniqueTypeList = techniqueTypeList,
            onItemClick = { techniqueType = it })
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
