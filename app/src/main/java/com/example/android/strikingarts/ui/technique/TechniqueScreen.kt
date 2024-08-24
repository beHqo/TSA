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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.strikingarts.R
import com.example.android.strikingarts.domain.model.ImmutableList
import com.example.android.strikingarts.domain.model.MovementType
import com.example.android.strikingarts.domain.model.Technique
import com.example.android.strikingarts.domain.model.TechniqueType
import com.example.android.strikingarts.ui.components.FilterChip
import com.example.android.strikingarts.ui.components.MoreVertDropdownMenu
import com.example.android.strikingarts.ui.components.NumberPicker
import com.example.android.strikingarts.ui.components.PlayButton
import com.example.android.strikingarts.ui.components.PrimaryText
import com.example.android.strikingarts.ui.components.SecondaryText
import com.example.android.strikingarts.ui.components.SelectionModeBottomSheet
import com.example.android.strikingarts.ui.components.listitem.SelectionButton
import com.example.android.strikingarts.ui.components.util.SurviveProcessDeath
import com.example.android.strikingarts.ui.parentlayouts.ListScreenLayout
import com.example.android.strikingarts.ui.technique.TechniqueViewModel.Companion.CHIP_INDEX_ALL
import com.example.android.strikingarts.ui.technique.TechniqueViewModel.Companion.OFFENSE_TAB_INDEX
import com.example.android.strikingarts.ui.theme.designsystemmanager.ColorManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.PaddingManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.ShapeManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.SizeManager
import com.example.android.strikingarts.ui.theme.designsystemmanager.SizeManager.TechniqueFilterChipHeight
import com.example.android.strikingarts.ui.util.getLocalizedName
import com.example.android.strikingarts.ui.util.toComposeColor

@Composable
fun TechniqueScreen(
    model: TechniqueViewModel = hiltViewModel(),
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    navigateToTechniqueDetails: (id: Long?) -> Unit,
    navigateToComboDetails: () -> Unit
) {
    val visibleTechniques by model.visibleTechniques.collectAsStateWithLifecycle()
    val tabIndex by model.tabIndex.collectAsStateWithLifecycle()
    val chipIndex by model.chipIndex.collectAsStateWithLifecycle()
    val deleteDialogVisible by model.deleteDialogVisible.collectAsStateWithLifecycle()
    val selectionMode by model.selectionMode.collectAsStateWithLifecycle()
    val selectedItemsIdList by model.selectedItemsIdList.collectAsStateWithLifecycle()
    val selectedItemsNames by model.selectedItemsNames.collectAsStateWithLifecycle()

    val productionMode = model.productionMode

    val selectionButtonsEnabled by remember { derivedStateOf { selectedItemsIdList.isNotEmpty() } }

    TechniqueScreen(
        navigateToTechniqueDetails = navigateToTechniqueDetails,
        navigateToComboDetails = navigateToComboDetails,
        setSelectionModeValueGlobally = setSelectionModeValueGlobally,
        selectedItemsIdList = selectedItemsIdList,
        selectedItemsNames = selectedItemsNames,
        playTechniqueAudio = model::play,
        productionMode = productionMode,
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
        onFabClick = { navigateToTechniqueDetails(null) },
        selectionButtonsEnabled = selectionButtonsEnabled,
        visibleTechniques = visibleTechniques,
        tabIndex = tabIndex,
        onTabClick = model::onTabClick,
        chipIndex = chipIndex,
        onChipClick = model::onChipClick
    )

    SurviveProcessDeath(onStop = model::surviveProcessDeath)
}

@Composable
private fun TechniqueScreen(
    navigateToTechniqueDetails: (Long) -> Unit,
    navigateToComboDetails: () -> Unit,
    setSelectionModeValueGlobally: (Boolean) -> Unit,
    selectedItemsIdList: ImmutableList<Long>,
    selectedItemsNames: String,
    playTechniqueAudio: (String) -> Unit,
    productionMode: Boolean,
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
    onFabClick: () -> Unit,
    selectionButtonsEnabled: Boolean,
    visibleTechniques: ImmutableList<Technique>,
    tabIndex: Int,
    onTabClick: (Int) -> Unit,
    chipIndex: Int,
    onChipClick: (TechniqueType, Int) -> Unit,
) = ListScreenLayout(
    productionMode = productionMode,
    selectionMode = selectionMode,
    exitSelectionMode = { exitSelectionMode(); setSelectionModeValueGlobally(false) },
    deleteDialogVisible = deleteDialogVisible,
    dismissDeleteDialog = setDeleteDialogVisibility,
    onDeleteItem = deleteItem,
    onDeleteMultipleItems = deleteSelectedItems,
    onFabClick = onFabClick,
    topSlot = {
        TechniqueTabRow(selectedIndex = tabIndex, onClick = onTabClick)
        FilterChipRow(
            techniqueTypeList = if (tabIndex == OFFENSE_TAB_INDEX) TechniqueType.offenseTypes
            else TechniqueType.defenseTypes,
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
            buttonsEnabled = selectionButtonsEnabled,
            previewText = selectedItemsNames,
            itemsSelectedText = stringResource(
                R.string.all_bottom_selection_bar_selected, selectedItemsIdList.size
            ),
            buttonText = stringResource(R.string.technique_create_combo),
            onButtonClick = { exitSelectionMode(); navigateToComboDetails() },
            onSelectAll = selectAllItems,
            onDeselectAll = deselectAllItems,
            onDelete = { setDeleteDialogVisibility(true) })
    })

@Composable
private fun TechniqueTabRow(selectedIndex: Int, onClick: (Int) -> Unit) = TabRow(selectedIndex) {
    val tabTitles =
        listOf(stringResource(R.string.all_offense), stringResource(R.string.all_defense))

    tabTitles.forEachIndexed { index, tabTitle ->
        Tab(
            text = { Text(tabTitle) },
            selected = selectedIndex == index,
            onClick = { onClick(index) })
    }
}

@Composable
private fun FilterChipRow(
    techniqueTypeList: ImmutableList<TechniqueType>,
    selectedIndex: Int,
    onClick: (TechniqueType, Int) -> Unit,
) = Row(
    modifier = Modifier
        .height(IntrinsicSize.Min)
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .background(color = ColorManager.surface)
        .padding(horizontal = PaddingManager.Large, vertical = PaddingManager.Small),
    verticalAlignment = Alignment.CenterVertically
) {
    FilterChip(
        title = stringResource(R.string.all_all),
        selected = selectedIndex == CHIP_INDEX_ALL,
        modifier = Modifier
            .height(TechniqueFilterChipHeight)
            .padding(PaddingManager.Small)
    ) { onClick(TechniqueType.UNSPECIFIED, CHIP_INDEX_ALL) }

    VerticalDivider(
        modifier = Modifier
            .padding(horizontal = PaddingManager.Small)
            .fillMaxHeight(0.77F)
    )

    techniqueTypeList.forEachIndexed { index, techniqueType ->
        FilterChip(
            title = techniqueType.getLocalizedName(),
            selected = selectedIndex == index,
            modifier = Modifier
                .height(TechniqueFilterChipHeight)
                .padding(PaddingManager.Small)
        ) { onClick(techniqueType, index) }
    }
}

private fun LazyListScope.techniqueList(
    visibleTechniques: ImmutableList<Technique>,
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
        secondaryText = technique.techniqueType.getLocalizedName(),
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
        secondaryText = technique.techniqueType.getLocalizedName(),
        color = technique.color.toComposeColor(),
        onModeChange = { id, selectionMode ->
            setSelectionModeValueGlobally(selectionMode); onLongPress(id)
        },
        onClick = { if (technique.movementType == MovementType.OFFENSE) onOffenseClick(technique.audioAttributes.audioString) },
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
        .heightIn(min = SizeManager.ListItemMinHeight)
        .combinedClickable(onClick = { onClick(itemId) },
            onLongClick = { onModeChange(itemId, true) })
        .padding(vertical = PaddingManager.Medium, horizontal = PaddingManager.Large)
) {
    if (color == Color.Transparent) PlayButton(primaryText) { onClick(itemId) }
    else Box(
        modifier = Modifier
            .size(SizeManager.ListItemImageSize)
            .background(color, ShapeManager.ExtraSmall)
    )
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .weight(1F)
            .padding(start = PaddingManager.Large)
    ) { PrimaryText(primaryText); SecondaryText(secondaryText) }
    MoreVertDropdownMenu(
        onDelete = { onDelete(itemId) },
        onEdit = { onEdit(itemId) },
        modifier = Modifier.padding(end = PaddingManager.Medium)
    )
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
        .heightIn(min = SizeManager.ListItemMinHeight)
        .combinedClickable(onClick = {
            onSelectionChange(itemId, !selected)
        }, onLongClick = { onModeChange(itemId, false) })
        .padding(vertical = PaddingManager.Medium, horizontal = PaddingManager.Large)
) {
    SelectionButton(selected, onDeselectItem, itemId, onSelectionChange)
    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1F)) {
        PrimaryText(primaryText); SecondaryText(secondaryText)
    }
    NumberPicker(quantity = selectedQuantity, setQuantity = { setSelectedQuantity(itemId, it) })
}