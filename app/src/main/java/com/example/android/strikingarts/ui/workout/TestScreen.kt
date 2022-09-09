package com.example.android.strikingarts.ui.workout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun Test(model: TestViewModel = hiltViewModel()) {
    val state = model.uiState.collectAsState()
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = state.value.textFieldValue1,
            onValueChange = model::onTextField1ValueChange
        )

        TextField(
            value = state.value.textFieldValue2,
            onValueChange = model::onTextField2ValueChange
        )

        Checkbox(
            checked = state.value.checkBoxValue,
            onCheckedChange = model::onCheckBoxValueChange
        )

        TextFieldDropdown(
            name = state.value.optionName,
//            optionList = state.value.visibleOptions.list.toMutableStateList(),
            optionList = model.visibleOptions.list,
            onOptionChange = {}
        )
    }
}

@Composable
private fun TextFieldIcon(expanded: Boolean, modifier: Modifier = Modifier) {
    Icon(
        imageVector = if (expanded)
            Icons.Sharp.KeyboardArrowUp else Icons.Sharp.KeyboardArrowDown,
        contentDescription = null,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TextFieldDropdown(
    name: String,
    optionList: List<String>,
    onOptionChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            TextField(
                modifier = Modifier.clickable { expanded = !expanded },
                readOnly = true,
                value = name,
                onValueChange = { },
                label = { Text("Label") },
                trailingIcon = { TextFieldIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                optionList.forEach {
                    DropdownMenuItem(onClick = {
                        onOptionChange(it)
                        expanded = false
                    }) { Text(it) }
                }
//                DropdownMenuItem(onClick = {
//                    onOptionChange("Option 1")
//                    expanded = false
//                }) { Text("Option 1") }
//                DropdownMenuItem(onClick = {
//                    onOptionChange("Option 3")
//                    expanded = false
//                }) { Text("Option 3") }
            }
        }
    }
}

@Preview
@Composable
fun PreviewThis() {
    Surface {
        Test()
    }
}