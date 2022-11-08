package com.example.android.strikingarts.ui.techniquedetails

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.components.*
import com.example.android.strikingarts.utils.TechniqueCategory.DEFENSE
import com.example.android.strikingarts.utils.TechniqueCategory.OFFENSE
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun TechniqueDetailsScreen(
    model: TechniqueDetailsViewModel = hiltViewModel(),
    scrollState: ScrollState = rememberScrollState(),
    navigateUp: () -> Unit
) {
    val state = model.uiState.collectAsState()
    val colorPickerController = rememberColorPickerController()

    if (state.value.alertDialogVisible) ConfirmDialog(
        titleId = stringResource(R.string.all_discard),
        textId = stringResource(R.string.techniquedetails_dialog_discard_changes),
        confirmButtonText = stringResource(R.string.all_discard),
        dismissButtonText = stringResource(R.string.all_cancel),
        onConfirm = navigateUp,
        onDismiss = model::hideAlertDialog
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        NameTextField(
            modifier = Modifier.padding(
                bottom = if (state.value.movementType == OFFENSE) 24.dp else 16.dp
            ),
            value = state.value.name,
            onValueChange = model::onNameChange,
            maxChars = TEXTFIELD_NAME_MAX_CHARS,
            isError = state.value.name.length > TEXTFIELD_NAME_MAX_CHARS,
            label = stringResource(R.string.techniquedetails_textfield_name_label),
            placeHolder = stringResource(R.string.techniquedetails_textfield_name_hint),
            leadingIcon = R.drawable.ic_glove_filled_light,
            helperText = stringResource(R.string.technique_details_textfield_name_helper),
            imeAction = ImeAction.Next
        )

        if (state.value.movementType == OFFENSE) NumTextField(
            modifier = Modifier.padding(bottom = 24.dp),
            value = state.value.num,
            onValueChange = model::onNumChange,
            label = stringResource(R.string.techniquedetails_numfield_label),
            placeHolder = stringResource(R.string.techniquedetails_numfield_hint),
            leadingIcon = R.drawable.ic_label_filled_light,
            helperText = stringResource(R.string.technique_details_numfield_helper),
            errorText = stringResource(R.string.all_numfield_error),
            imeAction = ImeAction.Next,
        )

        Text(
            text = stringResource(R.string.techniquedetails_technique_category),
            style = MaterialTheme.typography.subtitle2,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 4.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TechniqueDetailsRadioButton(
                selected = state.value.movementType == DEFENSE,
                onClick = model::onDefenseButtonClick,
                movementNameId = stringResource(R.string.techniquedetails_defense)
            )

            TechniqueDetailsRadioButton(
                selected = state.value.movementType == OFFENSE,
                onClick = model::onOffenseButtonClick,
                movementNameId = stringResource(R.string.techniquedetails_offense)
            )
        }
        TechniqueTypeDropdown(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            techniqueTypeName = state.value.techniqueType,
            textFieldLabel = stringResource(R.string.techniquedetails_technique_type),
            techniqueTypeList = state.value.techniqueTypes,
            onItemClick = model::onTechniqueTypeChange
        )

        if (state.value.movementType == DEFENSE) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(value = state.value.showColorPicker,
                        onValueChange = { model.showColorPicker() })
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.techniquedetails_modify_technique_color),
                    style = MaterialTheme.typography.subtitle2,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                if (state.value.showColorPicker) ColorPickerDialog(
                    controller = colorPickerController,
                    techniqueColor = state.value.color,
                    onDismiss = model::hideColorPicker,
                    onColorChange = model::onColorChange
                ) else ColorSample(controller = null, techniqueColor = state.value.color)
            }
        }
//        Need to remember this function in order to favor smart-recomposition... for now!
        val saveTechniqueAndNavigateUp = remember { { model.onSaveButtonClick(); navigateUp() } }

        DoubleButtonsRow(
            modifier = Modifier.fillMaxWidth(),
            leftButtonText = stringResource(R.string.all_cancel),
            rightButtonText = stringResource(R.string.all_save),
            onLeftButtonClick = model::showAlertDialog,
            onRightButtonClick = saveTechniqueAndNavigateUp
        )
    }
}