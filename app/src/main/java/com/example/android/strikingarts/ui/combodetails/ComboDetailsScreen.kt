package com.example.android.strikingarts.ui.combodetails

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
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
import com.example.android.strikingarts.ui.components.DelaySlider
import com.example.android.strikingarts.ui.components.NameTextField
import com.example.android.strikingarts.ui.components.TEXTFIELD_DESC_MAX_CHARS
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import com.example.android.strikingarts.utils.quantityStringResource
import kotlin.math.roundToInt


@Composable
fun ComboDetailsScreen(
    model: ComboDetailsViewModel = hiltViewModel(),
    scrollState: ScrollState = rememberScrollState(),
    onNavigateToTechniqueScreen: () -> Unit,
    onNavigateToComoScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NameTextField(
            modifier = Modifier.padding(bottom = 16.dp),
            value = model.name,
            onValueChange = model::onNameChange,
            maxChars = TEXTFIELD_NAME_MAX_CHARS,
            isError = model.name.length > TEXTFIELD_NAME_MAX_CHARS,
            label = stringResource(R.string.combo_details_textfield_name_label),
            placeHolder = stringResource(R.string.combo_details_textfield_name_placeholder),
            helperText = stringResource(R.string.combo_details_textfield_helper),
            leadingIcon = R.drawable.ic_glove_filled_light,
            imeAction = ImeAction.Next
        )

        NameTextField(
            modifier = Modifier.padding(bottom = 32.dp),
            value = model.desc,
            onValueChange = model::onDescChange,
            maxChars = TEXTFIELD_DESC_MAX_CHARS,
            isError = model.desc.length > TEXTFIELD_DESC_MAX_CHARS,
            label = stringResource(R.string.combo_details_textfield_desc_label),
            placeHolder = stringResource(R.string.combo_details_textfield_desc_placeholder),
            helperText = stringResource(R.string.combo_details_textfield_desc_helper),
            leadingIcon = R.drawable.ic_comment_filled_light,
            imeAction = ImeAction.Next
        )

        Text(
            text = stringResource(R.string.combo_details_recovery),
            style = MaterialTheme.typography.subtitle2,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        DelaySlider(
            value = model.delay,
            onValueChange = model::onDelayChange,
            valueRange = 1F..15F,
            startingNumber = stringResource(R.string.all_one),
            finishingNumber = stringResource(R.string.all_fifteen)
        )
        Text(
            text = quantityStringResource(
                R.plurals.all_second,
                model.delay.roundToInt(),
                model.delay.roundToInt()
            ),
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .offset(y = (0).dp)
                .padding(bottom = 24.dp)
        )

        Button(onClick = onNavigateToTechniqueScreen) {
            Text(text = stringResource(R.string.combo_details_button_add_technique))
        }
    }

}