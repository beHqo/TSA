@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.android.strikingarts.ui.combodetails

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.components.DelaySlider
import com.example.android.strikingarts.ui.components.NameTextField
import com.example.android.strikingarts.ui.components.TEXTFIELD_DESC_MAX_CHARS
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS
import kotlin.math.roundToInt


@Composable
fun ComboDetailsScreen(
    model: ComboDetailsViewModel = hiltViewModel(),
    scrollState: ScrollState = rememberScrollState()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
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
            modifier = Modifier.padding(bottom = 24.dp),
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
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 4.dp)
        )
        DelaySlider(
            value = model.delay,
            onValueChange = model::onDelayChange,
            valueRange = 1F..15F,
            startingNumber = stringResource(R.string.all_one),
            finishingNumber = stringResource(R.string.all_fifteen)
        )
        Text(
            text = pluralStringResource(
                R.plurals.all_second,
                model.delay.roundToInt(),
                model.delay.roundToInt()
            ),
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .offset(y = (0).dp)
                .padding(bottom = 24.dp)
        )
    }

}

@Preview
@Composable
fun PreviewComboDetailsScreen() {
    ComboDetailsScreen()
}