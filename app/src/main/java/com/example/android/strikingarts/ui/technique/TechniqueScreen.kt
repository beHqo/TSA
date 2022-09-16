package com.example.android.strikingarts.ui.technique

import androidx.annotation.DrawableRes
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.R
import com.example.android.strikingarts.database.entity.*
import com.example.android.strikingarts.ui.components.ConfirmDialog
import com.example.android.strikingarts.ui.components.DoubleLineItemWithImage
import com.example.android.strikingarts.ui.components.FilterChip
import com.example.android.strikingarts.utils.ImmutableList


@Composable
fun TechniqueListScreen(
    model: TechniqueViewModel = hiltViewModel(),
    onNavigateToTechniqueDetails: (Long) -> Unit,
) {
    val state = model.uiState.collectAsState()

    if (state.value.showDeleteDialog) {
        ConfirmDialog(
            titleId = stringResource(R.string.all_delete),
            textId = stringResource(R.string.technique_dialog_delete),
            confirmButtonText = stringResource(R.string.all_delete),
            dismissButtonText = stringResource(R.string.all_cancel),
            onConfirm = model::deleteTechnique,
            onDismiss = model::hideDeleteDialog
        )
    }

    Column {
        val tabTitles = MovementType.values().dropLast(1).map { it.name }

        TabRow(selectedTabIndex = state.value.tabIndex) {
            tabTitles.forEachIndexed { index, tabTitle ->
                Tab(text = { Text(tabTitle, style = MaterialTheme.typography.button) },
                    selected = state.value.tabIndex == index,
                    onClick = { model.onTabClick(index) })
            }
        }

        FilterChipRow(
            names = when (state.value.tabIndex) {
                0 -> ImmutableList(getOffenseTypes().map { it.techniqueName })
                else -> ImmutableList(getDefenseTypes().map { it.techniqueName })
            },
            selectedIndex = state.value.chipIndex,
            onClick = model::onChipClick,
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.value.visibleTechniques, key = { it.techniqueId }) {
                TechniqueItem(
                    techniqueName = it.name,
                    techniqueType = it.techniqueType.techniqueName,
                    image = it.techniqueType.id,
                    selectionMode = state.value.selectionMode,
                    onModeChange = { model.onLongPress(it.techniqueId) },
                    selected = state.value.selectedTechniques[it.techniqueId]!!,
                    onSelectionChange = { selected ->
                        model.onItemSelectionChange(it.techniqueId, selected)
                    },
                    onClick = {},
                    onEdit = { onNavigateToTechniqueDetails(it.techniqueId) },
                    onDelete = { model.showDeleteDialog(it.techniqueId) }
                )
            }
        }
    }
}

@Composable
private fun FilterChipRow(
    names: ImmutableList<String>,
    selectedIndex: Int,
    onClick: (TechniqueType, Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp, start = 12.dp, end = 12.dp)
            .horizontalScroll(rememberScrollState()), verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChip(
            title = stringResource(R.string.all_all),
            selected = selectedIndex == Int.MAX_VALUE,
            modifier = Modifier
                .height(32.dp)
                .padding(4.dp)
        ) { onClick(TechniqueType.NONE, Int.MAX_VALUE) }

        Divider(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .height(24.dp)
                .width(1.dp)
        )

        for (index in names.indices) {
            FilterChip(
                title = names[index],
                selected = selectedIndex == index,
                modifier = Modifier
                    .height(32.dp)
                    .padding(4.dp)
            ) { onClick(getTechniqueType(names[index]), index) }
        }
    }
}

@Composable
fun TechniqueItem(
    techniqueName: String,
    techniqueType: String,
    @DrawableRes image: Int,
    selectionMode: Boolean,
    onModeChange: () -> Unit,
    selected: Boolean,
    onSelectionChange: (selected: Boolean) -> Unit,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    DoubleLineItemWithImage(
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
