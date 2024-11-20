package com.github.tsa.ui.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import com.github.tsa.R
import com.github.tsa.ui.theme.designsystemmanager.PaddingManager
import com.github.tsa.ui.theme.designsystemmanager.TypographyManager
import java.util.Locale

@Composable
fun AboutScreen(navigateUp: () -> Unit) = Column(
    Modifier
        .verticalScroll(rememberScrollState())
        .fillMaxSize()
) {
    val libraries = mapOf(
        "Coroutines" to "https://github.com/Kotlin/kotlinx.coroutines",
        "SqlDelight" to "https://github.com/sqldelight/sqldelight",
        "Kotest" to "https://github.com/kotest/kotest",
        "ColorPicker" to "https://github.com/skydoves/colorpicker-compose"
    )

    val fonts = listOf(
        Triple(
            "Eczar", "https://github.com/rosettatype/eczar", stringResource(R.string.about_app_font)
        ), Triple(
            "Lumanosimo",
            "https://github.com/docrepair-fonts/lumanosimo-fonts",
            stringResource(R.string.about_logo_font)
        )
    )

    val license =
        "See the GNU General Public License for more details." to "https://www.gnu.org/licenses"

    val titleSmall = TypographyManager.titleSmall.toSpanStyle()
    val titleMedium = TypographyManager.titleMedium.toSpanStyle()
    val titleLarge = TypographyManager.titleLarge.toSpanStyle()
    val bodyMedium = TypographyManager.bodyMedium.toSpanStyle()
    val labelSmall = MaterialTheme.typography.labelMedium.toSpanStyle()
        .copy(color = Color.Blue, textDecoration = TextDecoration.Underline)
    val hyperLinkStyle = TextLinkStyles(style = labelSmall)

    AboutScreenTopAppBar(navigateUp)

    val aboutAnnotatedText = buildAnnotatedString {

        withStyle(titleMedium) { append("TSA ") }
        withStyle(bodyMedium) {
            appendLine(stringResource(R.string.about_intro))
        }

        withStyle(titleLarge) { appendLine(stringResource(R.string.about_features_title)) }
        withStyle(bodyMedium) {
            appendLine(stringResource(R.string.about_features))
        }

        withStyle(titleLarge) { appendLine(stringResource(R.string.about_credits_title)) }
        withStyle(titleSmall) { appendLine(stringResource(R.string.about_libraries_title)) }
        for ((name, link) in libraries) withStyle(bodyMedium) {
            withLink(link = LinkAnnotation.Url(url = link, styles = hyperLinkStyle)) {
                appendLine(name)
            }
        }
        withStyle(bodyMedium) { appendLine(stringResource(R.string.about_credits_ui)) }
        withStyle(titleSmall) { appendLine(stringResource(R.string.about_credits_fonts)) }
        for ((name, link, desc) in fonts) {
            withLink(link = LinkAnnotation.Url(url = link, styles = hyperLinkStyle)) {
                append(name)
            }
            withStyle(bodyMedium) { appendLine(desc) }
        }
    }

    val licenseAnnotatedText = buildAnnotatedString {
        withStyle(titleLarge) { appendLine(stringResource(R.string.about_license_title)) }
        withStyle(bodyMedium) { appendLine("The Striking Arts\nCopyright (C) 2024 behqo\n") }
        withStyle(bodyMedium) { append("This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. ") }
        withLink(link = LinkAnnotation.Url(url = license.second, styles = hyperLinkStyle)) {
            append(license.first)
        }
    }

    Text(text = aboutAnnotatedText, modifier = Modifier.padding(PaddingManager.Large))

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingManager.Large)
        ) { Text(text = licenseAnnotatedText, modifier = Modifier.padding(PaddingManager.Large)) }
    }

    Spacer(Modifier.weight(1F))

    TextButton(
        onClick = navigateUp,
        modifier = Modifier
            .align(Alignment.End)
            .padding(bottom = PaddingManager.Large, end = PaddingManager.Large)
    ) { Text(stringResource(R.string.about_text_button_navigate_back).uppercase(Locale.getDefault())) }
}

@OptIn(ExperimentalMaterial3Api::class) //TopAppBar
@Composable
private fun AboutScreenTopAppBar(navigateUp: () -> Unit) {
    TopAppBar(
        title = { Text(stringResource(R.string.about_screen_name)) },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.about_navigate_up_content_desc)
                )
            }
        },
    )
}