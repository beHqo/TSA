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
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.R
import com.example.android.strikingarts.database.entity.MovementType
import com.example.android.strikingarts.ui.components.*
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

private const val TEXTFIELD_MAX_CHARS = 30

@Composable
fun TechniqueDetailsScreen(
    model: TechniqueDetailsViewModel = hiltViewModel(),
    scrollState: ScrollState = rememberScrollState(),
    onNavigationRequest: () -> Unit
) {
    val state = model.state
    val colorPickerController = rememberColorPickerController()

    val pv = PaddingValues(top = 16.dp, bottom = 32.dp)

    if (state.alertDialogVisible)
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
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        StrikingTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pv),
            value = state.name,
            onValueChange = model::onNameChange,
            maxChars = TEXTFIELD_MAX_CHARS,
            label = stringResource(R.string.techniquedetails_textfield_name_label),
            placeHolder = stringResource(R.string.techniquedetails_textfield_name_hint),
            leadingIcon = R.drawable.ic_glove_filled_light,
            valueLength = state.name.length,
            showTrailingIcon = state.name.isNotEmpty(),
            isError = state.name.length > TEXTFIELD_MAX_CHARS,
            imeAction = ImeAction.Next
        )

        StrikingNumField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pv),
            value = state.num,
            onValueChange = model::onNumChange,
            labelId = R.string.techniquedetails_numfield_label,
            placeHolderId = R.string.techniquedetails_numfield_hint,
            leadingIcon = R.drawable.ic_label_filled_light,
            errorText = R.string.techniquedetails_numfield_error,
            isError = !state.num.isDigitsOnly(),
            imeAction = ImeAction.Next,
        )

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.techniquedetails_technique_category),
                style = MaterialTheme.typography.subtitle2,
                fontSize = 16.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TechniqueDetailsRadioButton(
                selected = state.movementType == MovementType.Defense,
                onClick = model::onDefenseButtonClick,
                movementNameId = stringResource(R.string.techniquedetails_defense)
            )

            TechniqueDetailsRadioButton(
                selected = state.movementType == MovementType.Offense,
                onClick = model::onOffenseButtonClick,
                movementNameId = stringResource(R.string.techniquedetails_offense)
            )
        }

        TechniqueDetailsDropdown(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pv),
            techniqueName = state.techniqueType.techniqueName,
            onTechniqueNameChange = model::onTechniqueTypeChange,
            textFieldLabel = stringResource(R.string.techniquedetails_technique_type),
            techniqueTypes = state.techniqueTypes,
            onDropdownItemClick = { model.onTechniqueTypeChange(it.techniqueName) }
        )

        if (state.movementType == MovementType.Defense) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = state.showColorPicker,
                        onValueChange = { model.showColorPicker() }),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.techniquedetails_modify_technique_color),
                    style = MaterialTheme.typography.subtitle2,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                if (state.showColorPicker)
                    ColorPickerDialog(
                        controller = colorPickerController,
                        techniqueColor = state.color,
                        onDismiss = model::hideColorPicker,
                        onColorChange = model::onColorChange
                    )
                else
                    ColorSample(controller = null, techniqueColor = state.color)
            }
        }

        TwoButtonsRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
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


