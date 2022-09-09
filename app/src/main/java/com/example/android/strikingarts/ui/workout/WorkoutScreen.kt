package com.example.android.strikingarts.ui.workout

import androidx.compose.foundation.layout.Column
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.ui.theme.StrikingArtsTheme

@Composable
fun Test(model: WorkoutViewModel = hiltViewModel()) {
    val state = model.uiState.collectAsState()
    Column {
        OutlinedTextField(value = state.value.textFieldValue1, onValueChange = model::onTextField1ValueChange)
        TextField(value = state.value.textFieldValue2, onValueChange = model::onTextField2ValueChange)
    }
}

@Preview
@Composable
fun PreviewThis() {
    StrikingArtsTheme {
        Surface {
            Test()
        }
    }
}