package com.example.android.strikingarts.ui.help

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.theme.StrikingArtsTheme
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.TypographyManager
import java.util.Locale

@Composable
fun HelpScreen(navigateUp: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        HelpTopAppBar(navigateUp)

        Text(
            text = stringResource(R.string.help_text),
            style = TypographyManager.bodyLarge,
            modifier = Modifier.padding(PaddingManager.Large)
        )

        Spacer(Modifier.weight(1F))

        TextButton(
            onClick = navigateUp,
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = PaddingManager.Large, end = PaddingManager.Large)
        ) { Text(stringResource(R.string.help_text_button_navigate_up).uppercase(Locale.getDefault())) }
    }
}

@OptIn(ExperimentalMaterial3Api::class) //TopAppBar
@Composable
private fun HelpTopAppBar(navigateUp: () -> Unit) =
    TopAppBar(title = { Text(stringResource(R.string.help_title)) }, navigationIcon = {
        IconButton(onClick = navigateUp) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(R.string.help_icon_desc_navigate_up)
            )
        }
    })

@Preview
@Composable
private fun PreviewHelpScreen() = StrikingArtsTheme { Surface { HelpScreen {} } }
