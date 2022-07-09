package com.example.android.strikingarts.techniquedetails

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import com.example.android.strikingarts.R
import com.example.android.strikingarts.components.DropdownTextField
import com.example.android.strikingarts.database.entity.TechniqueType

@Composable
fun TechniqueDetailsRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    @StringRes movementNameId: Int
) {
    Column(Modifier.selectableGroup()) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = stringResource(movementNameId),
            style = MaterialTheme.typography.body2
        )
    }
}

@Composable
fun TechniqueDetailsDropdown(
    techniqueName: String,
    onTechniqueNameChange: (String) -> Unit,
    @StringRes textFieldLabelId: Int,
    techniqueTypes: List<TechniqueType>,
    onDropdownItemClick: (TechniqueType) -> Unit,
    paddingValues: PaddingValues
    ) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
    ) {
        var expanded by remember { mutableStateOf(false) }

        DropdownTextField(
            value = techniqueName,
            onValueChange = onTechniqueNameChange,
            expanded = expanded,
            onClick = { expanded = true },
            label = { Text(stringResource(textFieldLabelId)) }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            techniqueTypes.forEach {
                DropdownMenuItem(onClick = {
                    onDropdownItemClick(it)
                    onTechniqueNameChange(it.techniqueName)
                    expanded = false
                }) { Text(it.techniqueName) }
            }
        }
    }
}
