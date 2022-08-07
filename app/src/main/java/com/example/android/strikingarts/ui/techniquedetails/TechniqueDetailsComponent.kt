package com.example.android.strikingarts.ui.techniquedetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.android.strikingarts.database.entity.TechniqueType
import com.example.android.strikingarts.ui.components.DropdownTextField

@Composable
fun TechniqueDetailsRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    movementNameId: String
) {
    Column(Modifier.selectableGroup()) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = movementNameId,
            style = MaterialTheme.typography.body2
        )
    }
}

@Composable
fun TechniqueDetailsDropdown(
    techniqueName: String,
    onTechniqueNameChange: (String) -> Unit,
    textFieldLabel: String,
    techniqueTypes: List<TechniqueType>,
    onDropdownItemClick: (TechniqueType) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        var expanded by remember { mutableStateOf(false) }

        DropdownTextField(
            value = techniqueName,
            onValueChange = onTechniqueNameChange,
            expanded = expanded,
            onClick = { expanded = true },
            label = { Text(textFieldLabel) }
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
