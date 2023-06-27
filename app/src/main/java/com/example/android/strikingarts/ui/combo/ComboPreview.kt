package com.example.android.strikingarts.ui.combo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.android.strikingarts.ui.components.PlayButton
import com.example.android.strikingarts.ui.components.PrimaryText
import com.example.android.strikingarts.ui.model.ComboPlaylist
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Stable
class ComboPreviewState(private val defaultDispatchers: CoroutineDispatcher = Dispatchers.Default) {
    var comboPlaylist by mutableStateOf(ComboPlaylist())

    var visible by mutableStateOf(false)
    var comboName by mutableStateOf("")
    var techniqueNames by mutableStateOf("")
    var playAudio: () -> Unit by mutableStateOf({ })
    var pauseAudio: () -> Unit by mutableStateOf({ })

    internal var currentColor by mutableStateOf(Color.Transparent)

    suspend fun onPlay() = withContext(defaultDispatchers) {
        playAudio()

        for (technique in comboPlaylist.playableTechniqueList) {
            currentColor = Color(technique.color.toULong())
            delay(technique.audioAttributes.durationMilli)
        }
    }
}

@Composable
fun rememberComboPreviewState() = remember { ComboPreviewState() }

@Composable
fun ComboPreviewDialog(
    modifier: Modifier = Modifier,
    comboPreviewState: ComboPreviewState = rememberComboPreviewState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onDismiss: (Boolean) -> Unit = { comboPreviewState.visible = false },
) {
    if (comboPreviewState.visible) Dialog(onDismissRequest = { onDismiss(false) }) {
        ComboPreview(modifier, comboPreviewState, coroutineScope)

        LaunchedEffect(Unit) { coroutineScope.launch { comboPreviewState.onPlay() } }

        DisposableEffect(Unit) { onDispose { comboPreviewState.pauseAudio() } }
    }
}

@Composable
fun ComboPreview(
    modifier: Modifier = Modifier,
    comboPreviewState: ComboPreviewState = rememberComboPreviewState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight(0.90F)
        .background(MaterialTheme.colors.surface)
        .padding(16.dp)
) {
    Text(
        comboPreviewState.comboName,
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(16.dp)
    )
    Box(
        Modifier
            .fillMaxSize()
            .weight(1F)
            .background(comboPreviewState.currentColor)
            .padding(16.dp)
    )
    PrimaryText(comboPreviewState.techniqueNames, modifier = Modifier.padding(16.dp))
    PlayButton(itemName = comboPreviewState.comboName) {
        coroutineScope.launch { comboPreviewState.onPlay() }
    }
}
//
//@Preview
//@Composable
//fun PreviewComboPreviewLOL() { //TODO: Remove
//    val comboPlaylist = ComboPlaylist(
//        delay = 300, playableTechniqueList = ImmutableList(
//            listOf(
//                PlayableTechnique(
//                    Color.Green.value.toString(),
//                    AssetAudioAttributes("Backfist", "audio/techniques/Backfist.wav", 300L)
//                ),
//                PlayableTechnique(
//                    Color.Blue.value.toString(),
//                    AssetAudioAttributes("Shoombool", "audio/techniques/shoombool.mp3", 600)
//                ),
//                PlayableTechnique(
//                    Color.Green.value.toString(),
//                    AssetAudioAttributes("Shoombool", "audio/techniques/Backfist.wav", 600)
//                ),
//                PlayableTechnique(
//                    Color.Black.value.toString(),
//                    AssetAudioAttributes("Silence", "audio/techniques/Silence.mp3", 5000)
//                ),
//                PlayableTechnique(
//                    Color.Red.value.toString(),
//                    AssetAudioAttributes("Shoombool", "audio/techniques/yummy_pie.mp3", 600)
//                ),
//            )
//        )
//    )
//
//    val coroutineScope = rememberCoroutineScope()
//
//    val comboPlayer = ComboPlayer(context = LocalContext.current, coroutineScope = coroutineScope)
//
//    val state = rememberComboPreviewState().apply {
//        this.comboName = "Double jab cross"
//        this.techniqueNames = "1, 1, 2"
//        this.comboPlaylist = comboPlaylist
//        this.playAudio = {
//            coroutineScope.launch {
//                comboPlayer.play(ImmutableList(comboPlaylist.playableTechniqueList.map {
//                    it.audioAttributes.audioString
//                }))
//            }
//        }
//        this.pauseAudio = { comboPlayer.pause() }
//    }
//
//    StrikingArtsTheme {
//        Surface(Modifier.fillMaxSize()) {
//            Column { Button(onClick = { state.visible = true }) { Text(text = "Show Dialog") } }
//            ComboPreviewDialog(coroutineScope = coroutineScope, comboPreviewState = state)
//        }
//    }
//}