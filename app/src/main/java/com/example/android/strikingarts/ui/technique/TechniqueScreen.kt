package com.example.android.strikingarts.ui.technique

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.R
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.ui.components.ConfirmDialog
import com.example.android.strikingarts.ui.components.DoubleLineItemWithImage
import com.example.android.strikingarts.ui.components.FilterChip
import com.example.android.strikingarts.ui.components.SelectionModeBottomSheet
import com.example.android.strikingarts.utils.ImmutableList
import com.example.android.strikingarts.utils.ImmutableSet
import com.example.android.strikingarts.utils.TechniqueCategory.DEFENSE_ID
import com.example.android.strikingarts.utils.TechniqueCategory.OFFENSE_ID
import com.example.android.strikingarts.utils.TechniqueCategory.defenseTypes
import com.example.android.strikingarts.utils.TechniqueCategory.offenseTypes

@Composable
fun TechniqueListScreen(
    model: TechniqueViewModel = hiltViewModel(),
    onNavigateToTechniqueDetails: (Long) -> Unit,
    onSelectionModeChange: (Boolean) -> Unit,
    onNavigateBackToComboDetails: () -> Unit,
) {
    val state = model.uiState.collectAsState()

    BackHandler(enabled = state.value.selectionMode) {
        model.exitSelectionMode(); onSelectionModeChange(false)
    }

    if (state.value.showDeleteDialog) ConfirmDialog(
        titleId = stringResource(R.string.all_delete),
        textId = if (state.value.selectionMode) stringResource(R.string.technique_delete_multiple)
        else stringResource(R.string.technique_dialog_delete),
        confirmButtonText = stringResource(R.string.all_delete),
        dismissButtonText = stringResource(R.string.all_cancel),
        onConfirm = {
            if (state.value.selectionMode) model.deleteSelectedItems()
            else model.deleteItem()
        },
        onDismiss = model::hideDeleteDialog
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            TechniqueTabRow(selectedTabIndex = state.value.tabIndex, onTabClick = model::onTabClick)

            FilterChipRow(
                names = if (state.value.tabIndex == 0) ImmutableSet(offenseTypes.keys)
                else ImmutableSet(defenseTypes.keys),
                selectedIndex = state.value.chipIndex,
                onClick = model::onChipClick,
            )

            TechniqueList(
                visibleTechniques = ImmutableList(state.value.visibleTechniques),
                selectionMode = state.value.selectionMode,
                onSelectionModeChange = onSelectionModeChange,
                selectedTechniques = ImmutableList(state.value.selectedItems),
                onSelectionChange = model::onItemSelectionChange,
                onLongPress = model::onLongPress,
                onClick = {}, // Should play the technique on in a dialog
                onNavigateToTechniqueDetails = onNavigateToTechniqueDetails,
                onShowDeleteDialog = model::showDeleteDialogAndUpdateId
            )
        }

        SelectionModeBottomSheet(
            modifier = Modifier.align(Alignment.BottomEnd),
            visible = state.value.selectionMode,
            shrunkStateText = "${state.value.numberOfSelectedItems} Selected",
            buttonsEnabled = state.value.selectedItems.isNotEmpty(),
            buttonText = stringResource(R.string.technique_create_combo),
            onButtonClick = { model.updateSelectedItemIds(); onNavigateBackToComboDetails() },
            onSelectAll = model::selectAllItems,
            onDeselectAll = model::deselectAllItems,
            onDelete = model::showDeleteDialog
        )
    }
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
            .padding(top = 4.dp, bottom = 4.dp, start = 12.dp, end = 12.dp)
            .horizontalScroll(rememberScrollState()), verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChip(
            title = stringResource(R.string.all_all),
            selected = selectedIndex == Int.MAX_VALUE,
            modifier = Modifier
                .height(32.dp)
                .padding(4.dp)
        ) { onClick("", Int.MAX_VALUE) }

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

@Composable
private fun TechniqueList(
    modifier: Modifier = Modifier,
    visibleTechniques: ImmutableList<Technique>,
    selectionMode: Boolean,
    onSelectionModeChange: (Boolean) -> Unit,
    onLongPress: (Long, Boolean) -> Unit,
    selectedTechniques: ImmutableList<Long>,
    onSelectionChange: (Long, Boolean) -> Unit,
    onClick: (Long) -> Unit,
    onNavigateToTechniqueDetails: (Long) -> Unit,
    onShowDeleteDialog: (Long) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(visibleTechniques, key = { it.techniqueId }) {
            TechniqueItem(
                techniqueId = it.techniqueId,
                techniqueName = it.name,
                techniqueType = it.techniqueType,
                image = offenseTypes[it.techniqueType]?.imageId
                    ?: defenseTypes[it.techniqueType]?.imageId ?: R.drawable.none_color,
                selectionMode = selectionMode,
                onModeChange = { id, selectionMode ->
                    onSelectionModeChange(selectionMode); onLongPress(id, selectionMode)
                },
                selected = selectedTechniques.contains(it.techniqueId),
                onSelectionChange = onSelectionChange,
                onClick = onClick,
                onEdit = onNavigateToTechniqueDetails,
                onDelete = onShowDeleteDialog
            )
        }
    }
}
