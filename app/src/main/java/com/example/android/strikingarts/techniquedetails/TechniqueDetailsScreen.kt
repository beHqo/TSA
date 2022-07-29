package com.example.android.strikingarts.techniquedetails

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.R
import com.example.android.strikingarts.components.*

import com.example.android.strikingarts.database.entity.*
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

private const val TEXTFIELD_MAX_CHARS = 30

@Composable
fun TechniqueDetailsScreen(
    model: TechniqueDetailsViewModel = hiltViewModel(),
    scrollState: ScrollState = rememberScrollState(),
    onNavigationRequest: () -> Unit
) {
    val colorPickerController = rememberColorPickerController()

    val generalPV = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp)
    val movementButtonPV = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)

    if (model.state.alertDialogVisible)
        ConfirmDialog(
            titleId = R.string.discard,
            textId = R.string.technique_discard_text,
            confirmButtonTextId = R.string.discard,
            dismissButtonTextId = R.string.cancel,
            onConfirm = onNavigationRequest,
            onDismiss = model::hideAlertDialog
        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        StrikingTextField(value = model.state.name,
            onValueChange = model::onNameChange,
            maxChars = TEXTFIELD_MAX_CHARS,
            labelId = R.string.name,
            placeHolderId = R.string.ej_jab,
            leadingIcon = R.drawable.ic_glove_filled_light,
            valueLength = model.state.name.length,
            showTrailingIcon = model.state.name.isNotEmpty(),
            isError = model.state.name.length > TEXTFIELD_MAX_CHARS,
            imeAction = ImeAction.Next,
            modifier = Modifier
                .fillMaxWidth()
                .padding(generalPV)
        )

        StrikingNumField(value = model.state.num,
            onValueChange = model::onNumChange,
            labelId = R.string.number,
            placeHolderId = R.string.eg_1,
            leadingIcon = R.drawable.ic_label_filled_light,
            errorText = R.string.num_textfield_error,
            isError = !model.state.num.isDigitsOnly(),
            imeAction = ImeAction.Next,
            modifier = Modifier
                .fillMaxWidth()
                .padding(generalPV),
        )

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.technique_category),
                style = MaterialTheme.typography.subtitle2,
                fontSize = 16.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(movementButtonPV),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TechniqueDetailsRadioButton(
                selected = model.state.movementType == MovementType.Defense,
                onClick = model::onDefenseButtonClick,
                movementNameId = R.string.defense
            )

            TechniqueDetailsRadioButton(
                selected = model.state.movementType == MovementType.Offense,
                onClick = model::onOffenseButtonClick,
                movementNameId = R.string.offense
            )
        }

        TechniqueDetailsDropdown(
            techniqueName = model.state.techniqueType.techniqueName,
            onTechniqueNameChange = model::onTechniqueTypeChange,
            textFieldLabelId = R.string.technique_type,
            techniqueTypes = model.state.techniqueTypes,
            onDropdownItemClick = { model.onTechniqueTypeChange(it.techniqueName) },
            paddingValues = generalPV
        )

        if (model.state.movementType == MovementType.Defense) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .toggleable(
                        value = model.state.showColorPicker,
                        onValueChange = { model.showColorPicker() })
            ) {
                Text(
                    text = stringResource(R.string.color_modify),
                    style = MaterialTheme.typography.subtitle2,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                if (model.state.showColorPicker)
                    ColorPickerDialog(
                        controller = colorPickerController,
                        techniqueColor = model.state.color,
                        onDismiss = model::hideColorPicker,
                        onColorChange = model::onColorChange
                    )
                else
                    ColorSample(controller = null, techniqueColor = model.state.color)
            }
        }

        TwoButtonsRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(generalPV),
            leftButtonTextId = R.string.cancel,
            rightButtonTextId = R.string.save,
            onLeftButtonClick = model::showAlertDialog,
            onRightButtonClick = {
                model.onSaveButtonClick()
                onNavigationRequest()
            }
        )
    }
}


