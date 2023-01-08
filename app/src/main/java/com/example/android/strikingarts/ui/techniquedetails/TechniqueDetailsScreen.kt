package com.example.android.strikingarts.ui.techniquedetails

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.components.ColorPickerDialog
import com.example.android.strikingarts.ui.components.ColorSample
import com.example.android.strikingarts.ui.components.ConfirmDialog
import com.example.android.strikingarts.ui.components.DoubleButtonsRow
import com.example.android.strikingarts.ui.components.NameTextField
import com.example.android.strikingarts.ui.components.NumTextField
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.example.android.strikingarts.utils.TechniqueCategory.DEFENSE
import com.example.android.strikingarts.utils.TechniqueCategory.OFFENSE
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun TechniqueDetailsScreen(
    model: TechniqueDetailsViewModel = hiltViewModel(),
    scrollState: ScrollState = rememberScrollState(),
    navigateUp: () -> Unit
) {
    val state by model.uiState.collectAsState()
    val colorPickerController = rememberColorPickerController()
    val errorState by remember {
        derivedStateOf {
            state.name.length > TEXTFIELD_NAME_MAX_CHARS ||
                    !state.num.isDigitsOnly() || state.name.isEmpty() ||
                    state.num.isEmpty() || state.techniqueType.isEmpty()
        }
    }

    if (state.alertDialogVisible) ConfirmDialog(
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
                bottom = if (state.movementType == OFFENSE) 24.dp else 16.dp
            ),
            value = state.name,
            onValueChange = model::onNameChange,
            maxChars = TEXTFIELD_NAME_MAX_CHARS,
            label = stringResource(R.string.techniquedetails_textfield_name_label),
            placeHolder = stringResource(R.string.techniquedetails_textfield_name_hint),
            leadingIcon = R.drawable.ic_glove_filled_light,
            helperText = stringResource(R.string.technique_details_textfield_name_helper),
            imeAction = ImeAction.Next
        )

        if (state.movementType == OFFENSE) NumTextField(
            modifier = Modifier.padding(bottom = 24.dp),
            value = state.num,
            onValueChange = model::onNumChange,
            label = stringResource(R.string.techniquedetails_numfield_label),
            placeHolder = stringResource(R.string.techniquedetails_numfield_hint),
            leadingIcon = R.drawable.ic_label_filled_light,
            helperText = stringResource(R.string.technique_details_numfield_helper),
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
                selected = state.movementType == DEFENSE,
                onClick = model::onDefenseButtonClick,
                movementNameId = stringResource(R.string.techniquedetails_defense)
            )

            TechniqueDetailsRadioButton(
                selected = state.movementType == OFFENSE,
                onClick = model::onOffenseButtonClick,
                movementNameId = stringResource(R.string.techniquedetails_offense)
            )
        }
        TechniqueTypeDropdown(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            techniqueTypeName = state.techniqueType,
            textFieldLabel = stringResource(R.string.techniquedetails_technique_type),
            techniqueTypeList = state.techniqueTypes,
            onItemClick = model::onTechniqueTypeChange
        )

        if (state.movementType == DEFENSE) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(value = state.showColorPicker,
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
                if (state.showColorPicker) ColorPickerDialog(
                    controller = colorPickerController,
                    techniqueColor = state.color,
                    onDismiss = model::hideColorPicker,
                    onColorChange = model::onColorChange
                ) else ColorSample(controller = null, techniqueColor = state.color)
            }
        }

        DoubleButtonsRow(modifier = Modifier.fillMaxWidth(),
            leftButtonText = stringResource(R.string.all_cancel),
            rightButtonText = stringResource(R.string.all_save),
            leftButtonEnabled = true,
            rightButtonEnabled = !errorState,
            onLeftButtonClick = model::showAlertDialog,
            onRightButtonClick = { model.onSaveButtonClick(); navigateUp() })
    }
}