package com.example.android.strikingarts.ui.technique

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.R
import com.example.android.strikingarts.domain.common.ImmutableList
import com.example.android.strikingarts.domain.common.ImmutableSet
import com.example.android.strikingarts.domain.model.TechniqueCategory.OFFENSE
import com.example.android.strikingarts.domain.model.TechniqueCategory.defenseStrId
import com.example.android.strikingarts.domain.model.TechniqueCategory.defenseTypes
import com.example.android.strikingarts.domain.model.TechniqueCategory.offenseStrId
import com.example.android.strikingarts.domain.model.TechniqueCategory.offenseTypes
import com.example.android.strikingarts.domain.model.TechniqueListItem
import com.example.android.strikingarts.mediaplayer.TechniquePlayer
import com.example.android.strikingarts.ui.components.FilterChip
import com.example.android.strikingarts.ui.components.MoreVertDropdownMenu
import com.example.android.strikingarts.ui.components.NumberPicker
import com.example.android.strikingarts.ui.components.PlayButton
import com.example.android.strikingarts.ui.components.PrimaryText
import com.example.android.strikingarts.ui.components.SecondaryText
import com.example.android.strikingarts.ui.components.SelectionModeBottomSheet
import com.example.android.strikingarts.ui.components.listitem.SelectionButton
import com.example.android.strikingarts.ui.parentlayouts.ListScreenLayout
import com.example.android.strikingarts.ui.shapes.HexagonShape
import com.example.android.strikingarts.ui.technique.TechniqueViewModel.Companion.CHIP_INDEX_ALL
import com.example.android.strikingarts.ui.technique.TechniqueViewModel.Companion.OFFENSE_TAB_INDEX
import kotlinx.coroutines.launch

@Composable
fun TechniqueScreen(
    model: TechniqueViewModel = hiltViewModel(),
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    navigateToTechniqueDetails: (Long) -> Unit,
    navigateToComboDetails: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val visibleTechniques by model.visibleTechniques.collectAsStateWithLifecycle()
    val tabIndex by model.tabIndex.collectAsStateWithLifecycle()
    val chipIndex by model.chipIndex.collectAsStateWithLifecycle()
    val deleteDialogVisible by model.deleteDialogVisible.collectAsStateWithLifecycle()
    val selectionMode by model.selectionMode.collectAsStateWithLifecycle()
    val selectedItemsIdList by model.selectedItemsIdList.collectAsStateWithLifecycle()

    val selectionButtonsEnabled by remember { derivedStateOf { selectedItemsIdList.size > 1 } }

    val techniquePlayer = TechniquePlayer(LocalContext.current)

    TechniqueScreen(
        navigateToTechniqueDetails = navigateToTechniqueDetails,
        navigateToComboDetails = navigateToComboDetails,
        setSelectionModeValueGlobally = setSelectionModeValueGlobally,
        selectedItemsIdList = selectedItemsIdList,
        playTechniqueAudio = { coroutineScope.launch { techniquePlayer.play(it) } },
        selectionMode = selectionMode,
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
        deleteItem = model::deleteItem,
        deleteSelectedItems = model::deleteSelectedItems,
        selectionButtonsEnabled = selectionButtonsEnabled,
        visibleTechniques = visibleTechniques,
        tabIndex = tabIndex,
        onTabClick = model::onTabClick,
        chipIndex = chipIndex,
        onChipClick = model::onChipClick,
    )

    DisposableEffect(Unit) { onDispose { techniquePlayer.releaseResources() } }
}

@Composable
private fun TechniqueScreen(
    navigateToTechniqueDetails: (Long) -> Unit,
    navigateToComboDetails: () -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    selectedItemsIdList: ImmutableList<Long>,
    playTechniqueAudio: (String) -> Unit,
    selectionMode: Boolean,
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
    deleteItem: () -> Unit,
    deleteSelectedItems: () -> Unit,
    selectionButtonsEnabled: Boolean,
    visibleTechniques: ImmutableList<TechniqueListItem>,
    tabIndex: Int,
    onTabClick: (Int) -> Unit,
    chipIndex: Int,
    onChipClick: (String, Int) -> Unit,
) = ListScreenLayout(selectionMode = selectionMode,
    exitSelectionMode = { exitSelectionMode(); setSelectionModeValueGlobally(false) },
    deleteDialogVisible = deleteDialogVisible,
    dismissDeleteDialog = setDeleteDialogVisibility,
    onDeleteItem = deleteItem,
    onDeleteMultipleItems = deleteSelectedItems,
    topSlot = {
        TechniqueTabRow(selectedIndex = tabIndex, onClick = onTabClick)
        FilterChipRow(
            names = if (tabIndex == OFFENSE_TAB_INDEX) ImmutableSet(offenseTypes.keys)
            else ImmutableSet(defenseTypes.keys),
            selectedIndex = chipIndex,
            onClick = onChipClick,
        )
    },
    lazyColumnContent = {
        techniqueList(
            visibleTechniques = visibleTechniques,
            selectionMode = selectionMode,
            setSelectionModeValueGlobally = setSelectionModeValueGlobally,
            onLongPress = onLongPress,
            selectedItemsIdList = selectedItemsIdList,
            onSelectionChange = onItemSelectionChange,
            onDeselectItem = onDeselectItem,
            setSelectedQuantity = setSelectedQuantity,
            onOffenseClick = playTechniqueAudio,
            onNavigateToTechniqueDetails = navigateToTechniqueDetails,
            onShowDeleteDialog = showDeleteDialogAndUpdateId,
        )
    },
    bottomSlot = {
        SelectionModeBottomSheet(visible = selectionMode,
            previewText = stringResource(
                R.string.all_bottom_selection_bar_selected, selectedItemsIdList.size
            ),
            buttonsEnabled = selectionButtonsEnabled,
            buttonText = stringResource(R.string.technique_create_combo),
            onButtonClick = { exitSelectionMode(); navigateToComboDetails() },
            onSelectAll = selectAllItems,
            onDeselectAll = deselectAllItems,
            onDelete = { setDeleteDialogVisibility(true) })
    })

@Composable
private fun TechniqueTabRow(selectedIndex: Int, onClick: (Int) -> Unit) = TabRow(selectedIndex) {
    val tabTitles = ImmutableList(listOf(offenseStrId, defenseStrId))

    tabTitles.forEachIndexed { index, tabTitle ->
        Tab(text = { Text(stringResource(tabTitle), style = MaterialTheme.typography.button) },
            selected = selectedIndex == index,
            onClick = { onClick(index) })
    }
}

@Composable
private fun FilterChipRow(
    names: ImmutableSet<String>,
    selectedIndex: Int,
    onClick: (String, Int) -> Unit,
) = Row(
    modifier = Modifier
        .height(IntrinsicSize.Min)
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .background(color = MaterialTheme.colors.surface)
        .padding(horizontal = 12.dp, vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    FilterChip(
        title = stringResource(R.string.all_all),
        selected = selectedIndex == CHIP_INDEX_ALL,
        modifier = Modifier
            .height(32.dp)
            .padding(4.dp)
    ) { onClick("", CHIP_INDEX_ALL) }

    Divider(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .fillMaxHeight(0.77F)
            .width(1.dp)
    )

    names.forEachIndexed { index, name ->
        FilterChip(
            title = name,
            selected = selectedIndex == index,
            modifier = Modifier
                .height(32.dp)
                .padding(4.dp)
        ) { onClick(name, index) }
    }
}

private fun LazyListScope.techniqueList(
    visibleTechniques: ImmutableList<TechniqueListItem>,
    selectionMode: Boolean,
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    onLongPress: (Long) -> Unit,
    selectedItemsIdList: ImmutableList<Long>,
    onSelectionChange: (Long, Boolean) -> Unit,
    onDeselectItem: (Long) -> Unit,
    setSelectedQuantity: (Long, Int) -> Unit,
    onOffenseClick: (String) -> Unit,
    onNavigateToTechniqueDetails: (Long) -> Unit,
    onShowDeleteDialog: (Long) -> Unit,
) = if (selectionMode) items(items = visibleTechniques,
    key = { it.id },
    contentType = { "SelectionModeTechniqueItem" }) { technique ->
    TechniqueItemSelectionMode(
        itemId = technique.id,
        primaryText = technique.name,
        secondaryText = technique.techniqueType,
        onModeChange = { id, selectionMode ->
            setSelectionModeValueGlobally(selectionMode); onLongPress(id)
        },
        selected = selectedItemsIdList.contains(technique.id),
        onSelectionChange = onSelectionChange,
        onDeselectItem = onDeselectItem,
        selectedQuantity = selectedItemsIdList.count { id -> id == technique.id },
        setSelectedQuantity = setSelectedQuantity,
    )
} else items(items = visibleTechniques,
    key = { it.id },
    contentType = { "ViewingModeTechniqueItem" }) { technique ->
    TechniqueItemViewingMode(
        itemId = technique.id,
        primaryText = technique.name,
        secondaryText = technique.techniqueType,
        color = Color(technique.color.toULong()),
        onModeChange = { id, selectionMode ->
            setSelectionModeValueGlobally(selectionMode); onLongPress(id)
        },
        onClick = { if (technique.movementType == OFFENSE) onOffenseClick(technique.audioUriString.ifEmpty { technique.audioAssetFileName }) },
        onEdit = onNavigateToTechniqueDetails,
        onDelete = onShowDeleteDialog
    )
}

@OptIn(ExperimentalFoundationApi::class) //combinedClickable is experimental
@Composable
fun TechniqueItemViewingMode(
    itemId: Long,
    primaryText: String,
    secondaryText: String,
    color: Color,
    onModeChange: (itemId: Long, selectionMode: Boolean) -> Unit,
    onClick: (Long) -> Unit,
    onEdit: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    modifier: Modifier = Modifier
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
        .heightIn(min = 72.dp)
        .combinedClickable(onClick = { onClick(itemId) },
            onLongClick = { onModeChange(itemId, true) })
        .padding(vertical = 8.dp, horizontal = 16.dp)
) {
    if (color == Color.Transparent) PlayButton(primaryText) { onClick(itemId) }
    else Box(
        modifier = Modifier
            .size(32.dp)
            .background(color, HexagonShape)
    )
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .weight(1F)
            .padding(start = 16.dp)
    ) { PrimaryText(primaryText); SecondaryText(secondaryText) }
    MoreVertDropdownMenu({ onDelete(itemId) }, { onEdit(itemId) }, Modifier.padding(end = 8.dp))
}

@OptIn(ExperimentalFoundationApi::class) //combinedClickable is experimental
@Composable
fun TechniqueItemSelectionMode(
    itemId: Long,
    primaryText: String,
    secondaryText: String,
    onModeChange: (Long, Boolean) -> Unit,
    selected: Boolean,
    onSelectionChange: (Long, Boolean) -> Unit,
    onDeselectItem: (Long) -> Unit,
    selectedQuantity: Int,
    setSelectedQuantity: (Long, Int) -> Unit,
    modifier: Modifier = Modifier
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
        .heightIn(min = 72.dp)
        .combinedClickable(onClick = {
            onSelectionChange(itemId, !selected)
        }, onLongClick = { onModeChange(itemId, false) })
        .padding(vertical = 8.dp, horizontal = 16.dp)
) {
    SelectionButton(selected, onDeselectItem, itemId, onSelectionChange)
    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1F)) {
        PrimaryText(primaryText); SecondaryText(secondaryText)
    }
    NumberPicker(quantity = selectedQuantity, setQuantity = { setSelectedQuantity(itemId, it) })
}