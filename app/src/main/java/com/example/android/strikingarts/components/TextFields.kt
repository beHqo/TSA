package com.example.android.strikingarts.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.android.strikingarts.R
import com.example.android.strikingarts.components.DropdownIcon

@Composable
fun DropdownTextField(
    value: String,
    onValueChange: (String) -> Unit,
    expanded: Boolean,
    onClick: () -> Unit,
    label: @Composable (() -> Unit)) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { label() },
        trailingIcon = { DropdownIcon(expanded) },
        enabled = false,
        modifier = Modifier.clickable { onClick() },
        colors = TextFieldDefaults.textFieldColors(
            disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
            disabledTrailingIconColor = MaterialTheme.colors.onSurface
                .copy(alpha = TextFieldDefaults.IconOpacity)
        )
    )
}

@Composable
fun StrikingTextField(
    value: String,
    onValueChange: (String) -> Unit,
    maxChars: Int,
    @StringRes labelId: Int,
    @StringRes placeHolderId: Int,
    @DrawableRes leadingIcon: Int,
    valueLength: Int,
    showTrailingIcon: Boolean,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Default,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(stringResource(labelId)) },
        placeholder = { Text(stringResource(placeHolderId)) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType ,imeAction = imeAction),
        isError = isError,
        leadingIcon = { Icon(painter = painterResource(leadingIcon), contentDescription = null) },
        trailingIcon = {
            if (showTrailingIcon) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = stringResource(R.string.clear_text)
                    )
                }
            }
            Text("$valueLength/$maxChars", Modifier.offset(y = 40.dp))
        }
    )
}

@Composable
fun StrikingNumField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes labelId: Int,
    @StringRes placeHolderId: Int,
    @DrawableRes leadingIcon: Int,
    @StringRes errorText: Int,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Default,
    keyboardType: KeyboardType = KeyboardType.NumberPassword,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(stringResource(labelId)) },
        placeholder = { Text(stringResource(placeHolderId)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        isError = isError,
        leadingIcon = { Icon(painter = painterResource(leadingIcon), contentDescription = null) },
        trailingIcon = { if (isError)
            Text(stringResource(errorText), Modifier.offset(y = 40.dp))
        }
    )
}
