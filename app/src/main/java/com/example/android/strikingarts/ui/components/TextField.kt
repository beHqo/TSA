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
import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.ui.model.TextFieldError

internal const val TEXTFIELD_NAME_MAX_CHARS = 30
internal const val TEXTFIELD_DESC_MAX_CHARS = 40

val numfieldError = ImmutableList(
    listOf(TextFieldError(R.string.all_numfield_error) { !it.isDigitsOnly() })
)

val nameFieldError = ImmutableList(
    listOf(TextFieldError(R.string.all_textfield_error_character_limit) { it.length > TEXTFIELD_NAME_MAX_CHARS })
)

val descFieldError = ImmutableList(
    listOf(TextFieldError(R.string.all_textfield_error_character_limit) { it.length > TEXTFIELD_DESC_MAX_CHARS })
)

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    maxChars: Int,
    label: String,
    placeHolder: String,
    helperText: String,
    leadingIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    errorList: ImmutableList<TextFieldError> = nameFieldError,
    onDoneImeAction: (KeyboardActionScope.() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    val error by remember(value) { derivedStateOf { textFieldErrorComputation(value, errorList) } }
    val isError by remember(value) { derivedStateOf { error != null } }
    val imeAction by remember(value) { derivedStateOf { if (isError || value.isEmpty()) ImeAction.None else ImeAction.Done } }

    Column(modifier = modifier) {
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { Text(placeHolder) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            keyboardActions = KeyboardActions(onDone = onDoneImeAction),
            isError = isError,
            leadingIcon = leadingIcon,
            trailingIcon = { NameTextfieldTrailingIcon(value, onValueChange, maxChars) })

        HintText(
            helperText,
            stringResource(error?.messageId ?: R.string.all_textfield_error_incorrect_input),
            isError
        )
    }
}

@Composable
private fun NameTextfieldTrailingIcon(
    value: String, onValueChange: (String) -> Unit, maxChars: Int
) {
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
}

@Composable
fun NumTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeHolder: String,
    leadingIcon: @Composable () -> Unit,
    helperText: String,
    modifier: Modifier = Modifier,
    @DrawableRes trailingIconId: Int? = null,
    errorList: ImmutableList<TextFieldError> = numfieldError,
    onDoneImeAction: (KeyboardActionScope.() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.NumberPassword,
) {
    val error by remember(value) { derivedStateOf { textFieldErrorComputation(value, errorList) } }
    val isError by remember(value) { derivedStateOf { error != null } }
    val imeAction by remember(value) { derivedStateOf { if (isError || value.isEmpty()) ImeAction.None else ImeAction.Done } }

    Column(modifier = modifier) {
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { Text(placeHolder) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            keyboardActions = KeyboardActions(onDone = onDoneImeAction),
            isError = isError,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIconId?.let {
                { Icon(painterResource(trailingIconId), null) }
            })

        HintText(
            helperText,
            stringResource(error?.messageId ?: R.string.all_textfield_error_incorrect_input),
            isError
        )
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

private fun textFieldErrorComputation(
    value: String, errorList: ImmutableList<TextFieldError>
): TextFieldError? {
    errorList.forEach { error -> if (error.predicate(value)) return error }

    return null
}

fun String.removePrefixZeros(): String {
    if (this.length <= 1) return this

    return this.dropWhile { char -> char == '0' }
}

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