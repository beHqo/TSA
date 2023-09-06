package com.example.android.strikingarts.ui.losersscreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.audioplayers.PlayerConstants.ASSET_SESSION_EVENT_PATH_PREFIX
import com.example.android.strikingarts.ui.audioplayers.soundpool.SoundPoolWrapper
import com.example.android.strikingarts.ui.components.PrimaryText
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.TypographyManager
import kotlinx.coroutines.Dispatchers

@Composable
fun LosersScreen(navigateToHomeScreen: () -> Unit) = Column(
    Modifier
        .fillMaxSize()
        .padding(PaddingManager.Large),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
) {
    val soundPoolWrapper =
        SoundPoolWrapper(LocalContext.current, Dispatchers.Default, Dispatchers.IO)

    LaunchedEffect(Unit) {
        soundPoolWrapper.play(ASSET_SESSION_EVENT_PATH_PREFIX + "quit.wav")
    }

    BackHandler(onBack = navigateToHomeScreen)

    Text(
        text = stringResource(R.string.loser_give_up),
        textAlign = TextAlign.Center,
        style = TypographyManager.titleMedium,
        modifier = Modifier.padding(top = PaddingManager.Large)
    )

    PrimaryText(text = stringResource(R.string.loser_habit))

    Button(
        onClick = navigateToHomeScreen,
        modifier = Modifier.padding(top = PaddingManager.XXLarge, bottom = PaddingManager.Large)
    ) { Text(text = stringResource(R.string.loser_button), textAlign = TextAlign.Center) }

    DisposableEffect(Unit) { onDispose { soundPoolWrapper.release() } }
}