package com.example.android.strikingarts.ui.combo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android.strikingarts.ui.components.ExpandableListItem
import com.example.android.strikingarts.database.entity.ComboWithTechniques
import com.example.android.strikingarts.utils.getTechniqueNumberFromCombo

@Composable
fun ComboList(model: ComboViewModel = viewModel()) {
    val comboList = model.comboList.collectAsState(mutableListOf())

    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize()
    ) {
        items(comboList.value) { combo ->
            ComboItem(combo)
            Divider()
        }
    }
}

@Composable
private fun ComboItem(comboWithTechniques: ComboWithTechniques) {
//    ExpandableListItem(
//        primaryText = combo.comboName,
//        expandedText1 = combo.comboDescription,
//        expandedText2 = getTechniqueNumberFromCombo(combo).joinToString(separator = ", ")
//    )

    val combo = comboWithTechniques.combo

    ExpandableListItem(
        combo.name,
        combo.description,
        getTechniqueNumberFromCombo(comboWithTechniques)
    )
}
