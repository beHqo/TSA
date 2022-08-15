package com.example.android.strikingarts.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
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
import com.example.android.strikingarts.R

@Composable
fun NameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    maxChars: Int,
    label: String,
    placeHolder: String,
    @DrawableRes leadingIcon: Int,
    valueLength: Int,
    showTrailingIcon: Boolean,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Default,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(label) },
        placeholder = { Text(placeHolder) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        isError = isError,
        leadingIcon = { Icon(painter = painterResource(leadingIcon), contentDescription = null) },
        trailingIcon = {
            if (showTrailingIcon) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = stringResource(R.string.all_clear_text)
                    )
                }
            }
            Text("$valueLength/$maxChars", Modifier.offset(y = 40.dp))
        }
    )
}

@Composable
fun NumTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeHolder: String,
    @DrawableRes leadingIcon: Int,
    errorText: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Default,
    keyboardType: KeyboardType = KeyboardType.NumberPassword,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(label) },
        placeholder = { Text(placeHolder) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        isError = isError,
        leadingIcon = { Icon(painter = painterResource(leadingIcon), contentDescription = null) },
        trailingIcon = {
            if (isError)
                Text(errorText, Modifier.offset(y = 40.dp))
        }
    )
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