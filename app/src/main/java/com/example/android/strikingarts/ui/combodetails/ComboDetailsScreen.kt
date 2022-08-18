package com.example.android.strikingarts.ui.combodetails

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.components.NameTextField
import com.example.android.strikingarts.ui.components.TEXTFIELD_DESC_MAX_CHARS
import com.example.android.strikingarts.ui.components.TEXTFIELD_NAME_MAX_CHARS


@Composable
fun ComboDetailsScreen(
    model: ComboDetailsViewModel = hiltViewModel(),
    scrollState: ScrollState = rememberScrollState()
) {
    val pv = PaddingValues(top = 4.dp, bottom = 4.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        NameTextField(
            modifier = Modifier.padding(pv),
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
            modifier = Modifier.padding(pv),
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
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(R.string.all_one), modifier = Modifier.offset(y = -(4).dp))
            Slider(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp, end = 4.dp),
                value = model.delay,
                onValueChange = model::onDelayChange,
                valueRange = 1F..15F,
                steps = 13,
                colors = SliderDefaults.colors()
            )
            Text(text = stringResource(R.string.all_fifteen), modifier = Modifier.offset(y = -(4).dp))
        }
        Text(
            text = "${model.delay.toInt()} Second",
            style = MaterialTheme.typography.caption,
            modifier = Modifier.align(Alignment.CenterHorizontally).offset(y = (-10).dp)
        )
    }

}

@Preview
@Composable
fun PreviewComboDetailsScreen() {
    ComboDetailsScreen()
}