package com.example.android.strikingarts.ui.techniquedetails

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.R
import com.example.android.strikingarts.database.entity.MovementType
import com.example.android.strikingarts.ui.components.*
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun TechniqueDetailsScreen(
    model: TechniqueDetailsViewModel = hiltViewModel(),
    scrollState: ScrollState = rememberScrollState(),
    onNavigationRequest: () -> Unit
) {
    val colorPickerController = rememberColorPickerController()

    if (model.alertDialogVisible)
        ConfirmDialog(
            titleId = stringResource(R.string.all_discard),
            textId = stringResource(R.string.techniquedetails_dialog_discard_changes),
            confirmButtonText = stringResource(R.string.all_discard),
            dismissButtonText = stringResource(R.string.all_cancel),
            onConfirm = onNavigationRequest,
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
                bottom = if (model.movementType == MovementType.Offense) 24.dp else 16.dp
            ),
            value = model.name,
            onValueChange = model::onNameChange,
            maxChars = TEXTFIELD_NAME_MAX_CHARS,
            isError = model.name.length > TEXTFIELD_NAME_MAX_CHARS,
            label = stringResource(R.string.techniquedetails_textfield_name_label),
            placeHolder = stringResource(R.string.techniquedetails_textfield_name_hint),
            leadingIcon = R.drawable.ic_glove_filled_light,
            helperText = stringResource(R.string.technique_details_textfield_name_helper),
            imeAction = ImeAction.Next
        )

        if (model.movementType == MovementType.Offense) NumTextField(
            modifier = Modifier.padding(bottom = 24.dp),
            value = model.num,
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
                selected = model.movementType == MovementType.Defense,
                onClick = model::onDefenseButtonClick,
                movementNameId = stringResource(R.string.techniquedetails_defense)
            )

            TechniqueDetailsRadioButton(
                selected = model.movementType == MovementType.Offense,
                onClick = model::onOffenseButtonClick,
                movementNameId = stringResource(R.string.techniquedetails_offense)
            )
        }

        TechniqueDetailsDropdown(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            techniqueName = model.techniqueType.techniqueName,
            onTechniqueNameChange = model::onTechniqueTypeChange,
            textFieldLabel = stringResource(R.string.techniquedetails_technique_type),
            techniqueTypes = model.techniqueTypes,
            onDropdownItemClick = { model.onTechniqueTypeChange(it.techniqueName) }
        )

        if (model.movementType == MovementType.Defense) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = model.showColorPicker,
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
                if (model.showColorPicker)
                    ColorPickerDialog(
                        controller = colorPickerController,
                        techniqueColor = model.color,
                        onDismiss = model::hideColorPicker,
                        onColorChange = model::onColorChange
                    )
                else
                    ColorSample(controller = null, techniqueColor = model.color)
            }
        }

        TwoButtonsRow(
            modifier = Modifier
                .fillMaxWidth(),
            leftButtonText = stringResource(R.string.all_cancel),
            rightButtonText = stringResource(R.string.all_save),
            onLeftButtonClick = model::showAlertDialog,
            onRightButtonClick = {
                model.onSaveButtonClick()
                onNavigationRequest()
            }
        )
    }
}


