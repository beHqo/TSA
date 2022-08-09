package com.example.android.strikingarts.ui.combo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android.strikingarts.database.entity.Combo
import com.example.android.strikingarts.database.entity.ComboWithTechniques
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.database.entity.TechniqueType
import com.example.android.strikingarts.ui.components.TripleLineItem
import com.example.android.strikingarts.utils.getTechniqueNumberFromCombo

@Composable
fun ComboList(model: ComboViewModel = viewModel()) {
    val comboList = model.comboList.collectAsState(mutableListOf())

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(comboList.value, key = { it.combo.comboId }) { combo ->
            ComboItem(combo)
            Divider()
        }
    }
}

@Composable
private fun ComboItem(comboWithTechniques: ComboWithTechniques) {
    TripleLineItem(
        primaryText = comboWithTechniques.combo.name,
        secondaryText = comboWithTechniques.combo.description,
        tertiaryText = getTechniqueNumberFromCombo(comboWithTechniques.techniques),
        onItemClick = { /*TODO*/ },
        onMoreVertClick = { /*TODO*/ })
}

@Preview
@Composable
fun PreviewComboItem() {
    Column {
        ComboItem(
            comboWithTechniques = ComboWithTechniques(
                Combo(
                    name = "The Mike Tyson Combo",
                    description = "Done in his fight against RJJ"
                ),
                techniques = listOf(
                    Technique(name = "Jab", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Cross", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Duck", techniqueType = TechniqueType.HEAD_MOVEMENT),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH)
                )
            )
        )
        Divider()
        ComboItem(
            comboWithTechniques = ComboWithTechniques(
                Combo(
                    name = "The Mike Tyson Combo",
                    description = "Done in his fight against RJJ"
                ),
                techniques = listOf(
                    Technique(name = "Jab", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Cross", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Duck", techniqueType = TechniqueType.HEAD_MOVEMENT),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH)
                )
            )
        )
        Divider()
        ComboItem(
            comboWithTechniques = ComboWithTechniques(
                Combo(
                    name = "The Mike Tyson Combo",
                    description = "Done in his fight against RJJ"
                ),
                techniques = listOf(
                    Technique(name = "Jab", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Cross", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Duck", techniqueType = TechniqueType.HEAD_MOVEMENT),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH)
                )
            )
        )
        Divider()
        ComboItem(
            comboWithTechniques = ComboWithTechniques(
                Combo(
                    name = "The Mike Tyson Combo",
                    description = "Done in his fight against RJJ"
                ),
                techniques = listOf(
                    Technique(name = "Jab", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Cross", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Duck", techniqueType = TechniqueType.HEAD_MOVEMENT),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH)
                )
            )
        )
        Divider()
        ComboItem(
            comboWithTechniques = ComboWithTechniques(
                Combo(
                    name = "The Mike Tyson Combo",
                    description = "Done in his fight against RJJ"
                ),
                techniques = listOf(
                    Technique(name = "Jab", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Cross", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Duck", techniqueType = TechniqueType.HEAD_MOVEMENT),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH)
                )
            )
        )
        Divider()
        ComboItem(
            comboWithTechniques = ComboWithTechniques(
                Combo(
                    name = "The Mike Tyson Combo",
                    description = "Done in his fight against RJJ"
                ),
                techniques = listOf(
                    Technique(name = "Jab", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Cross", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Duck", techniqueType = TechniqueType.HEAD_MOVEMENT),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH),
                    Technique(name = "Lead Hook", techniqueType = TechniqueType.PUNCH)
                )
            )
        )
        Divider()
    }
}