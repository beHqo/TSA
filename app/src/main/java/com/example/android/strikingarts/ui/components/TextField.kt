package com.example.android.strikingarts.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
    modifier: Modifier = Modifier,
    onImeActionClick: (KeyboardActionScope.() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    val isError by remember(value) { derivedStateOf { value.length > maxChars } }
    val imeAction by remember(isError) { derivedStateOf { if (isError) ImeAction.None else ImeAction.Done } }

    Column(modifier = modifier) {
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { Text(placeHolder) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            keyboardActions = KeyboardActions(onDone = onImeActionClick),
            isError = isError,
            leadingIcon = { Icon(painterResource(leadingIcon), null) },
            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            imageVector = Icons.Rounded.Clear, contentDescription = stringResource(
                                R.string.all_clear_text_field
                            )
                        )
                    }
                }
                Text("${value.length}/$maxChars", Modifier.offset(y = 40.dp))
            })
        HintText(helperText, stringResource(R.string.all_textfield_error), isError)
    }
}

@Composable
fun NumTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeHolder: String,
    @DrawableRes leadingIcon: Int,
    helperText: String,
    modifier: Modifier = Modifier,
    @DrawableRes trailingIcon: Int? = null,
    onImeActionClick: (KeyboardActionScope.() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.NumberPassword,
) {
    val isError by remember(value) { derivedStateOf { !value.isDigitsOnly() } }
    val imeAction by remember(isError) { derivedStateOf { if (isError) ImeAction.None else ImeAction.Done } }

    Column(modifier = modifier) {
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { Text(placeHolder) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            keyboardActions = KeyboardActions(onDone = onImeActionClick),
            isError = isError,
            leadingIcon = { Icon(painterResource(leadingIcon), null) },
            trailingIcon = trailingIcon?.let {
                { Icon(painterResource(trailingIcon), null) }
            })
        HintText(helperText, stringResource(R.string.all_numfield_error), isError)
    }
}

@Composable
private fun HintText(helperText: String, errorText: String, isError: Boolean) {
    Text(
        text = if (isError) errorText else helperText,
        style = MaterialTheme.typography.caption,
        color = if (isError) MaterialTheme.colors.error else MaterialTheme.colors.onSurface.copy(
            alpha = ContentAlpha.medium
        ),
        modifier = Modifier.padding(start = 16.dp)
    )
}
//
//@OptIn(ExperimentalMaterialApi::class) // ExposedDropdown is experimental
//@Composable
//fun TextFieldItemDropdown(
//    textFieldValue: String,
//    textFieldLabel: String,
//    techniqueTypeList: ImmutableSet<String>,
//    onItemClick: (String) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    var expanded by rememberSaveable { mutableStateOf(false) }
//
//    Box(modifier = modifier, contentAlignment = Alignment.Center) {
//        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
//            TextField(
//                modifier = Modifier.clickable { expanded = !expanded },
//                value = textFieldValue,
//                onValueChange = { },
//                label = { Text(textFieldLabel) },
//                trailingIcon = { DropdownIcon(expanded = expanded) },
//                readOnly = true,
//                colors = ExposedDropdownMenuDefaults.textFieldColors()
//            )
//
//            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//                techniqueTypeList.forEach {
//                    DropdownMenuItem(onClick = {
//                        onItemClick(it)
//                        expanded = false
//                    }) { Text(it) }
//                }
//            }
//        }
//    }
//}