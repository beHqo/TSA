package com.example.android.strikingarts.technique

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.components.FilterChip
import com.example.android.strikingarts.components.MoreVertIconButton
import com.example.android.strikingarts.database.entity.*


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

        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize()
        ) {
            items(techniqueList, key = { technique -> technique.techniqueId }) { technique ->
                TechniqueItem(
                    technique,
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

    Row(modifier = Modifier
        .padding(top = 8.dp, bottom = 4.dp, start = 4.dp, end = 4.dp)
        .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        FilterChip(title = "All", selected = selectedIndex == null) {
            onClick(TechniqueType.NONE, null)
        }

        Divider(modifier = Modifier.padding(horizontal = 4.dp))

        for (index in names.indices)
            FilterChip(names[index], selectedIndex == index) {
                onClick(getTechniqueType(names[index]), index)
            }
    }
}

@Composable
private fun TechniqueItem(technique: Technique, onItemClick: (id: Long) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onItemClick(technique.techniqueId) }
    ) {
        Image(
            painter = painterResource(technique.techniqueType.id),
            contentDescription = technique.techniqueType.techniqueName,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(end = 16.dp)
                .height(56.dp)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = technique.name,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1
            )
            Text(
                text = technique.techniqueType.techniqueName,
                style = MaterialTheme.typography.caption,
                color = Color.Black.copy(alpha = 0.5f)
            )
        }
        MoreVertIconButton { /*TODO*/ }
    }
}
