package com.example.android.strikingarts.ui.technique

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.R
import com.example.android.strikingarts.database.entity.*
import com.example.android.strikingarts.ui.components.DoubleLineItemWithImage
import com.example.android.strikingarts.ui.components.FilterChip


@Composable
fun TechniqueListScreen(
    model: TechniqueViewModel = hiltViewModel(),
    onNavigationRequest: (Long) -> Unit,
) {
    val techniqueList = model.techniqueList
    val tabIndex = model.tabIndex
    val chipIndex = model.chipIndex

    Column {
        val tabTitles = MovementType.values().dropLast(1).map { it.name }

        TabRow(selectedTabIndex = tabIndex) {
            tabTitles.forEachIndexed { index, tabTitle ->
                Tab(
                    text = { Text(tabTitle, style = MaterialTheme.typography.button) },
                    selected = tabIndex == index,
                    onClick = { model.onTabClick(index) }
                )
            }
        }

        FilterChipRow(
            names = when (tabIndex) {
                0 -> getOffenseTypes().map { it.techniqueName }
                else -> getDefenseTypes().map { it.techniqueName }
            },
            selectedIndex = chipIndex,
            onClick = model::onChipClick,
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(techniqueList, key = { it.techniqueId }) { technique ->
                TechniqueItem(
                    technique
                ) { onNavigationRequest(technique.techniqueId) }
                Divider()
            }
        }
    }
}

@Composable
private fun FilterChipRow(
    names: List<String>,
    selectedIndex: Int?,
    onClick: (TechniqueType, Int?) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp, start = 12.dp, end = 12.dp)
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChip(
            title = stringResource(R.string.all_all),
            selected = selectedIndex == null,
            modifier = Modifier
                .height(32.dp)
                .padding(4.dp)
        ) { onClick(TechniqueType.NONE, null) }

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
fun TechniqueItem(technique: Technique, onItemClick: (id: Long) -> Unit) {
    DoubleLineItemWithImage(
        primaryText = technique.name,
        secondaryText = technique.techniqueType.techniqueName,
        image = technique.techniqueType.id,
        imageContentDescription = technique.techniqueType.techniqueName,
        onItemClick = { onItemClick(technique.techniqueId) },
        onMoreVertClick = { /*TODO*/ })
}
