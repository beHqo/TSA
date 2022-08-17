package com.example.android.strikingarts.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.android.strikingarts.R

internal const val TEXTFIELD_NAME_MAX_CHARS = 30
internal const val TEXTFIELD_DESC_MAX_CHARS = 40

@Composable
fun NameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    maxChars: Int,
    label: String,
    placeHolder: String,
    helperText: String,
    @DrawableRes leadingIcon: Int,
    isError: Boolean,
    modifier: Modifier = Modifier,
    errorText: String = stringResource(R.string.all_textfield_error),
    imeAction: ImeAction = ImeAction.Default,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { Text(placeHolder) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            isError = isError,
            leadingIcon = {
                Icon(
                    painter = painterResource(leadingIcon),
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(imageVector = Icons.Rounded.Clear, contentDescription = null)
                    }
                }
                Text("${value.length}/$maxChars", Modifier.offset(y = 40.dp))
            }
        )
        hintText(helperText, errorText, isError)
    }
}

@Composable
fun NumTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeHolder: String,
    @DrawableRes leadingIcon: Int,
    @DrawableRes trailingIcon: Int? = null,
    helperText: String,
    errorText: String,
    modifier: Modifier = Modifier,
    isError: Boolean = !value.isDigitsOnly(),
    imeAction: ImeAction = ImeAction.Default,
    keyboardType: KeyboardType = KeyboardType.NumberPassword,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { Text(placeHolder) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            isError = isError,
            leadingIcon = { Icon(painter = painterResource(leadingIcon), null) },
            trailingIcon = trailingIcon?.let {
                { Icon(painterResource(trailingIcon), null) }
            }
        )
        hintText(helperText, errorText, isError)
    }
}

@Composable
fun DropdownTextField(
    value: String,
    onValueChange: (String) -> Unit,
    expanded: Boolean,
    onClick: () -> Unit,
    label: @Composable (() -> Unit),
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier.clickable { onClick() },
        value = value,
        onValueChange = onValueChange,
        label = { label() },
        trailingIcon = { DropdownIcon(expanded) },
        enabled = false,
        colors = TextFieldDefaults.textFieldColors(
            disabledTextColor = MaterialTheme.colors.onSurface.copy(alpha = 0.87F),
            disabledTrailingIconColor = MaterialTheme.colors.onSurface.copy(alpha = 0.87F),
            disabledLabelColor = MaterialTheme.colors.onSurface.copy(alpha = 0.60F),
            disabledPlaceholderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.60F),
            disabledIndicatorColor = MaterialTheme.colors.onSurface.copy(alpha = 0.87F),
            backgroundColor = MaterialTheme.colors.surface
        )
    )
}

@Composable
private fun hintText(helperText: String, errorText: String, isError: Boolean) {
    Text(
        text = if (isError) errorText else helperText,
        style = MaterialTheme.typography.caption,
        color = if (isError) MaterialTheme.colors.error else MaterialTheme.colors.onSurface.copy(
            alpha = 0.6F
        ),
        modifier = Modifier
            .padding(start = 16.dp)
    )
}