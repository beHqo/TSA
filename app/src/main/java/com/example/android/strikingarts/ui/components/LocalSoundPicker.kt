package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.example.android.strikingarts.R
import com.example.android.strikingarts.ui.audioplayers.PlayerConstants.ASSET_TECHNIQUE_PATH_PREFIX
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.SizeManager.LocalSoundPickerListItemHeight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LocalSoundPickerDialog(
    modifier: Modifier = Modifier,
    onDismiss: (Boolean) -> Unit,
    setAudioFileName: (String) -> Unit,
    playTechnique: (String) -> Unit
) = Dialog(onDismissRequest = { onDismiss(false) }) {
    LocalSoundPicker(setAudioFileName, { onDismiss(false) }, playTechnique, modifier)
}

@Composable
fun LocalSoundPicker(
    setAudioFileName: (String) -> Unit,
    onDismiss: () -> Unit,
    playTechnique: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope { Dispatchers.Default }

    val audioList =
        context.assets.list(ASSET_TECHNIQUE_PATH_PREFIX)?.toList().orEmpty()

    val (selectedIndex, setSelectedIndex) = rememberSaveable { mutableIntStateOf(-1) }
    val errorState by remember(selectedIndex) { derivedStateOf { selectedIndex == -1 } }

    Column(
        modifier
            .fillMaxWidth()
            .background(ColorManager.surface)
    ) {
        AudioList(modifier = Modifier.weight(1F), audioList, selectedIndex, setSelectedIndex) {
            coroutineScope.launch { playTechnique(it) }
        }

        DoubleTextButtonRow(modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingManager.Large),
            leftButtonText = stringResource(R.string.all_discard),
            rightButtonText = stringResource(R.string.all_save),
            leftButtonEnabled = true,
            rightButtonEnabled = !errorState,
            onLeftButtonClick = { setSelectedIndex(-1); onDismiss() },
            onRightButtonClick = {
                setAudioFileName("$ASSET_TECHNIQUE_PATH_PREFIX${audioList[selectedIndex]}")
                onDismiss()
            })
    }
}

@Composable
private fun AudioList(
    modifier: Modifier = Modifier,
    audioList: List<String>,
    selectedIndex: Int,
    setSelectedIndex: (Int) -> Unit,
    onClick: (String) -> Unit
) = LazyColumn(modifier = modifier) {
    itemsIndexed(items = audioList,
        key = { index, _ -> index },
        contentType = { _, _ -> "AssetAudioFile" }) { index, fileName ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = LocalSoundPickerListItemHeight)
                .selectable(
                    selected = index == selectedIndex,
                    onClick = { setSelectedIndex(index) })
                .background(color = if (index == selectedIndex) ColorManager.primary else ColorManager.background)
                .padding(vertical = PaddingManager.Medium, horizontal = PaddingManager.Large)
        ) {
            PlayButton(
                itemName = fileName,
                onClick = { onClick("$ASSET_TECHNIQUE_PATH_PREFIX$fileName") })
            PrimaryText(
                fileName,
                modifier = Modifier.padding(start = PaddingManager.Large),
                color = if (index == selectedIndex) ColorManager.onPrimary else ColorManager.onBackground
            )
        }
    }
}