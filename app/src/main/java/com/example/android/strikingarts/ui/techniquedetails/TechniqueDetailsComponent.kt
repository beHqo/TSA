package com.example.android.strikingarts.ui.techniquedetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.android.strikingarts.ui.components.DropdownIcon
import com.example.android.strikingarts.utils.ImmutableSet

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

@OptIn(ExperimentalMaterialApi::class) // ExposedDropdown is experimental
@Composable
fun TechniqueTypeDropdown(
    techniqueTypeName: String,
    textFieldLabel: String,
    techniqueTypeList: ImmutableSet<String>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            TextField(
                modifier = Modifier.clickable { expanded = !expanded },
                value = techniqueTypeName,
                onValueChange = { },
                label = { Text(textFieldLabel) },
                trailingIcon = { DropdownIcon(expanded = expanded) },
                readOnly = true,
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                techniqueTypeList.forEach {
                    DropdownMenuItem(onClick = {
                        onItemClick(it)
                        expanded = false
                    }) { Text(it) }
                }
            }
        }
    }
}
