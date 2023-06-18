package com.example.android.strikingarts.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.ui.components.PlayButton
import com.example.android.strikingarts.ui.components.PrimaryText
import com.example.android.strikingarts.ui.theme.StrikingArtsTheme
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Stable
class ComboPreviewState(private val defaultDispatchers: CoroutineDispatcher = Dispatchers.Default) {
    private val colorList = mutableStateListOf<Color>()
    internal var currentColor by mutableStateOf(Color.Transparent)

    var visible by mutableStateOf(false)
    var comboName by mutableStateOf("")
    var techniqueNames by mutableStateOf("")
    var playAudio: () -> Unit by mutableStateOf({})

    fun setTechniqueColors(colorList: ImmutableList<Color>) = this.colorList.apply {
        clear()
        addAll(colorList)
        add(Color.Transparent)
    }

    suspend fun onPlay() = withContext(defaultDispatchers) {
        playAudio()

        for (color in colorList) {
            delay(200)
            currentColor = color
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
    PrimaryText(comboPreviewState.comboName, modifier = Modifier.padding(16.dp))
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

@Preview
@Composable
fun PreviewComboPreviewLOL() { //TODO: Remove
    val colorList = ImmutableList(
        listOf(
            Color.Blue, Color.Red, Color.Green, Color.Cyan, Color.LightGray, Color.Transparent
        )
    )

    val coroutineScope = rememberCoroutineScope()

    val state = rememberComboPreviewState().apply {
        setTechniqueColors(colorList)
        comboName = "Double jab cross"
        techniqueNames = "1, 1, 2"
    }

    StrikingArtsTheme {
        Surface(Modifier.fillMaxSize()) {
            Column {
                Button(onClick = { state.visible = true }) { Text(text = "Show Dialog") }
                Text(
                    text = "Show Dialog"
                )
            }
            ComboPreviewDialog(coroutineScope = coroutineScope,
                comboPreviewState = state,
                onDismiss = { state.visible = false })
        }
    }
}