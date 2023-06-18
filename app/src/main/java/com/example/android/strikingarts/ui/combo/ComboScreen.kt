package com.example.android.strikingarts.ui.combo

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.R
import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.domain.model.ComboListItem
import com.example.android.strikingarts.domain.model.TechniqueListItem
import com.example.android.strikingarts.mediaplayer.ComboPlayer
import com.example.android.strikingarts.ui.ComboPreviewDialog
import com.example.android.strikingarts.ui.ComboPreviewState
import com.example.android.strikingarts.ui.components.SelectionModeBottomSheet
import com.example.android.strikingarts.ui.components.listitem.TripleLineItemSelectionMode
import com.example.android.strikingarts.ui.components.listitem.TripleLineItemViewingMode
import com.example.android.strikingarts.ui.mapper.toComboPlaylist
import com.example.android.strikingarts.ui.parentlayouts.ListScreenLayout
import com.example.android.strikingarts.ui.rememberComboPreviewState
import kotlinx.coroutines.cancelChildren

@Composable
fun ComboScreen(
    model: ComboViewModel = hiltViewModel(),
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    navigateToComboDetails: (id: Long) -> Unit,
    navigateToWorkoutDetails: () -> Unit
) {
    val deleteDialogVisible by model.deleteDialogVisible.collectAsStateWithLifecycle()
    val visibleItems by model.comboList.collectAsStateWithLifecycle()
    val selectedItemsIdList by model.selectedItemsIdList.collectAsStateWithLifecycle()
    val selectionMode by model.selectionMode.collectAsStateWithLifecycle()

    val selectionButtonsEnabled by remember { derivedStateOf { selectedItemsIdList.isNotEmpty() } }

    val coroutineScope = rememberCoroutineScope()

    val comboPlayer = ComboPlayer(LocalContext.current, coroutineScope)

    val comboPreviewState = rememberComboPreviewState()

    ComboPreviewDialog(comboPreviewState = comboPreviewState, coroutineScope = coroutineScope)

    ComboScreen(
        setSelectionModeValueGlobally = setSelectionModeValueGlobally,
        navigateToComboDetails = navigateToComboDetails,
        navigateToWorkoutDetails = navigateToWorkoutDetails,
        playComboAudio = comboPlayer::play,
        comboPreviewState = comboPreviewState,
        selectionMode = selectionMode,
        selectedItemsIdList = selectedItemsIdList,
        exitSelectionMode = model::exitSelectionMode,
        onLongPress = model::onLongPress,
        onItemSelectionChange = model::onItemSelectionChange,
        onDeselectItem = model::deselectItem,
        selectAllItems = model::selectAllItems,
        deselectAllItems = model::deselectAllItems,
        setSelectedQuantity = model::setSelectedQuantity,
        deleteDialogVisible = deleteDialogVisible,
        showDeleteDialogAndUpdateId = model::showDeleteDialogAndUpdateId,
        setDeleteDialogVisibility = model::setDeleteDialogVisibility,
        selectionButtonsEnabled = selectionButtonsEnabled,
        visibleCombos = visibleItems,
        deleteItem = model::deleteItem,
        deleteSelectedItems = model::deleteSelectedItems
    )

    DisposableEffect(Unit) {
        onDispose {
            coroutineScope.coroutineContext.cancelChildren()
            comboPlayer.releaseAllResources()
        }
    }
}

@Composable
private fun ComboScreen(
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    navigateToComboDetails: (Long) -> Unit,
    navigateToWorkoutDetails: () -> Unit,
    playComboAudio: (audioStringList: ImmutableList<String>) -> Unit,
    comboPreviewState: ComboPreviewState,
    selectionMode: Boolean,
    selectedItemsIdList: ImmutableList<Long>,
    exitSelectionMode: () -> Unit,
    onLongPress: (Long) -> Unit,
    onItemSelectionChange: (Long, Boolean) -> Unit,
    onDeselectItem: (Long) -> Unit,
    selectAllItems: () -> Unit,
    deselectAllItems: () -> Unit,
    setSelectedQuantity: (Long, Int) -> Unit,
    deleteDialogVisible: Boolean,
    showDeleteDialogAndUpdateId: (Long) -> Unit,
    setDeleteDialogVisibility: (Boolean) -> Unit,
    selectionButtonsEnabled: Boolean,
    visibleCombos: ImmutableList<ComboListItem>,
    deleteItem: () -> Unit,
    deleteSelectedItems: () -> Unit,
) = ListScreenLayout(selectionMode = selectionMode,
    exitSelectionMode = { setSelectionModeValueGlobally(false); exitSelectionMode() },
    deleteDialogVisible = deleteDialogVisible,
    dismissDeleteDialog = setDeleteDialogVisibility,
    onDeleteItem = deleteItem,
    onDeleteMultipleItems = deleteSelectedItems,
    lazyColumnContent = {
        comboList(
            visibleCombos = visibleCombos,
            selectionMode = selectionMode,
            onSelectionModeChange = setSelectionModeValueGlobally,
            onLongPress = onLongPress,
            selectedItemsIdList = selectedItemsIdList,
            onSelectionChange = onItemSelectionChange,
            onDeselectItem = onDeselectItem,
            setSelectedQuantity = setSelectedQuantity,
            playComboAudio = playComboAudio,
            comboPreviewState = comboPreviewState,
            onEdit = navigateToComboDetails,
            onDelete = showDeleteDialogAndUpdateId
        )
    },
    bottomSlot = {
        SelectionModeBottomSheet(
            visible = selectionMode,
            buttonsEnabled = selectionButtonsEnabled,
            previewText = stringResource(
                R.string.all_bottom_selection_bar_selected, selectedItemsIdList.size
            ),
            buttonText = stringResource(R.string.combo_details_add_to_workout),
            onButtonClick = { exitSelectionMode(); navigateToWorkoutDetails() },
            onSelectAll = selectAllItems,
            onDeselectAll = deselectAllItems,
            onDelete = { setDeleteDialogVisibility(true) },
        )
    })

private fun LazyListScope.comboList(
    visibleCombos: ImmutableList<ComboListItem>,
    selectionMode: Boolean,
    onSelectionModeChange: (Boolean) -> Unit,
    onLongPress: (Long) -> Unit,
    selectedItemsIdList: ImmutableList<Long>,
    onSelectionChange: (Long, Boolean) -> Unit,
    onDeselectItem: (Long) -> Unit,
    setSelectedQuantity: (Long, Int) -> Unit,
    playComboAudio: (audioStringList: ImmutableList<String>) -> Unit,
    comboPreviewState: ComboPreviewState,
    onEdit: (id: Long) -> Unit,
    onDelete: (id: Long) -> Unit
) = if (selectionMode) items(items = visibleCombos,
    key = { it.id },
    contentType = { "SelectionModeComboItem" }) {
    TripleLineItemSelectionMode(
        itemId = it.id,
        primaryText = it.name,
        secondaryText = it.desc,
        tertiaryText = getTechniqueNumberFromCombo(it.techniqueList), //TODO: Get technique name OR number based on user preferences
        onModeChange = { id, selectionMode ->
            onSelectionModeChange(selectionMode); onLongPress(id)
        },
        selected = selectedItemsIdList.contains(it.id),
        onSelectionChange = onSelectionChange,
        onDeselectItem = onDeselectItem,
        selectedQuantity = selectedItemsIdList.count { id -> id == it.id },
        setSelectedQuantity = setSelectedQuantity
    )
} else items(items = visibleCombos,
    key = { it.id },
    contentType = { "ViewingModeComboItem" }) { combo ->
    TripleLineItemViewingMode(
        itemId = combo.id,
        primaryText = combo.name,
        secondaryText = combo.desc,
        tertiaryText = getTechniqueNumberFromCombo(combo.techniqueList),
        onModeChange = { id, selectionMode ->
            onSelectionModeChange(selectionMode); onLongPress(id)
        },
        onClick = {
            comboPreviewState.apply {
                playAudio = { playComboAudio(combo.toComboPlaylist().audioStringList) }
                comboName = combo.name
                techniqueNames = combo.techniqueList.joinToString { it.name }
                setTechniqueColors(ImmutableList(combo.techniqueList.map { Color(it.color.toULong()) }))
                visible = true
            }
        },
        onEdit = onEdit,
        onDelete = onDelete
    )
}

private fun getTechniqueNumberFromCombo(techniqueList: ImmutableList<TechniqueListItem>): String =
    techniqueList.joinToString { it.num.ifEmpty { it.name } }

private fun getTechniqueNamesFromCombo(techniqueList: List<TechniqueListItem>): String =
    techniqueList.joinToString { it.name }