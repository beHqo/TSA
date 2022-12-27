package com.example.android.strikingarts.ui.technique

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.R
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.ui.components.DoubleLineItemWithImage
import com.example.android.strikingarts.ui.components.FilterChip
import com.example.android.strikingarts.ui.components.ListScreenLayout
import com.example.android.strikingarts.ui.components.SelectionModeBottomSheet
import com.example.android.strikingarts.utils.ImmutableList
import com.example.android.strikingarts.utils.ImmutableSet
import com.example.android.strikingarts.utils.TechniqueCategory.DEFENSE_ID
import com.example.android.strikingarts.utils.TechniqueCategory.OFFENSE_ID
import com.example.android.strikingarts.utils.TechniqueCategory.defenseTypes
import com.example.android.strikingarts.utils.TechniqueCategory.offenseTypes

@Composable
fun TechniqueScreen(
    model: TechniqueViewModel = hiltViewModel(),
    notifyBottomAppbarOnSelectionMode: (Boolean) -> Unit,
    onNavigateToTechniqueDetails: (Long) -> Unit,
    onNavigateToComboDetails: () -> Unit,
) {
    val state by model.uiState.collectAsState()

    ListScreenLayout(selectionMode = state.selectionMode,
        backHandler = {
            model.selectionActions.exitSelectionMode(); notifyBottomAppbarOnSelectionMode(false)
        },
        showDeleteDialog = state.showDeleteDialog,
        onHideDeleteDialog = model.deleteDialogActions::hideDeleteDialog,
        onDeleteItem = model::deleteItem,
        onDeleteMultipleItems = model::deleteSelectedItems,
        TopSlot = {
            TechniqueTabRow(selectedTabIndex = state.tabIndex, onTabClick = model::onTabClick)
            FilterChipRow(
                names = if (state.tabIndex == 0) ImmutableSet(offenseTypes.keys)
                else ImmutableSet(defenseTypes.keys),
                selectedIndex = state.chipIndex,
                onClick = model::onChipClick,
            )
        },
        lazyColumnContent = {
            techniqueList(
                visibleTechniques = ImmutableList(state.visibleTechniques),
                selectionMode = state.selectionMode,
                onSelectionModeChange = notifyBottomAppbarOnSelectionMode,
                onLongPress = model.selectionActions::onLongPress,
                selectedTechniques = ImmutableList(state.selectedItems),
                onSelectionChange = model.selectionActions::onItemSelectionChange,
                onClick = {}, // Should play the technique on in a dialog
                onNavigateToTechniqueDetails = onNavigateToTechniqueDetails,
                onShowDeleteDialog = model.deleteDialogActions::showDeleteDialogAndUpdateId,
            )
        },
        BottomSlot = {
            val buttonsEnabled by remember { derivedStateOf { state.selectedItems.isNotEmpty() } }

            SelectionModeBottomSheet(
                modifier = Modifier.align(Alignment.BottomEnd),
                visible = state.selectionMode,
                shrunkStateText = "${state.selectedItems.size} Selected",
                buttonsEnabled = buttonsEnabled,
                buttonText = stringResource(R.string.technique_create_combo),
                onButtonClick = { model.updateSelectedItemIds(); onNavigateToComboDetails() },
                onSelectAll = {
                    model.selectionActions.selectAllItems(
                        ImmutableList(state.visibleTechniques.map { it.techniqueId })
                    )
                },
                onDeselectAll = model.selectionActions::deselectAllItems,
                onDelete = model.deleteDialogActions::showDeleteDialog
            )
        })
}

@Composable
private fun TechniqueTabRow(selectedTabIndex: Int, onTabClick: (Int) -> Unit) {
    TabRow(selectedTabIndex) {
        val tabTitles = ImmutableList(listOf(OFFENSE_ID, DEFENSE_ID))

        tabTitles.forEachIndexed { index, tabTitle ->
            Tab(text = { Text(stringResource(tabTitle), style = MaterialTheme.typography.button) },
                selected = selectedTabIndex == index,
                onClick = { onTabClick(index) })
        }
    }
}

@Composable
private fun FilterChipRow(
    names: ImmutableSet<String>,
    selectedIndex: Int,
    onClick: (String, Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .background(color = MaterialTheme.colors.surface)
            .padding(top = 4.dp, bottom = 4.dp, start = 12.dp, end = 12.dp),
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
}

@Composable
private fun TechniqueItem(
    techniqueId: Long,
    techniqueName: String,
    techniqueType: String,
    @DrawableRes image: Int,
    selectionMode: Boolean,
    onModeChange: (Long, Boolean) -> Unit,
    selected: Boolean,
    onSelectionChange: (Long, Boolean) -> Unit,
    onClick: (id: Long) -> Unit,
    onEdit: (id: Long) -> Unit,
    onDelete: (id: Long) -> Unit
) {
    DoubleLineItemWithImage(
        itemId = techniqueId,
        primaryText = techniqueName,
        secondaryText = techniqueType,
        image = image,
        selectionMode = selectionMode,
        onModeChange = onModeChange,
        selected = selected,
        onSelectionChange = onSelectionChange,
        onClick = onClick,
        onEdit = onEdit,
        onDelete = onDelete
    )
}

private fun LazyListScope.techniqueList(
    visibleTechniques: ImmutableList<Technique>,
    selectionMode: Boolean,
    onSelectionModeChange: (Boolean) -> Unit,
    onLongPress: (Long) -> Unit,
    selectedTechniques: ImmutableList<Long>,
    onSelectionChange: (Long, Boolean) -> Unit,
    onClick: (Long) -> Unit,
    onNavigateToTechniqueDetails: (Long) -> Unit,
    onShowDeleteDialog: (Long) -> Unit,
) {
    items(items = visibleTechniques, key = { it.techniqueId }, contentType = { "techniqueItem" }) {
        TechniqueItem(
            techniqueId = it.techniqueId,
            techniqueName = it.name,
            techniqueType = it.techniqueType,
            image = offenseTypes[it.techniqueType]?.imageId
                ?: defenseTypes[it.techniqueType]?.imageId ?: R.drawable.none_color,
            selectionMode = selectionMode,
            onModeChange = { id, selectionMode ->
                onSelectionModeChange(selectionMode); onLongPress(id)
            },
            selected = selectedTechniques.contains(it.techniqueId),
            onSelectionChange = onSelectionChange,
            onClick = onClick,
            onEdit = onNavigateToTechniqueDetails,
            onDelete = onShowDeleteDialog
        )
    }
}
