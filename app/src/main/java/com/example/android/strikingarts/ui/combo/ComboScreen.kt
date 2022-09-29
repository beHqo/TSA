package com.example.android.strikingarts.ui.combo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.strikingarts.R
import com.example.android.strikingarts.database.entity.ComboWithTechniques
import com.example.android.strikingarts.ui.components.ConfirmDialog
import com.example.android.strikingarts.ui.components.TripleLineItem
import com.example.android.strikingarts.utils.getTechniqueNumberFromCombo

@Composable
fun ComboScreen(model: ComboViewModel = hiltViewModel(), onNavigationRequest: (id: Long) -> Unit) {
    val comboList = model.comboList.collectAsState(mutableListOf())

    if (model.showDeleteDialog) {
        ConfirmDialog(
            titleId = stringResource(R.string.all_delete),
            textId = stringResource(R.string.combo_dialog_delete),
            confirmButtonText = stringResource(R.string.all_delete),
            dismissButtonText = stringResource(R.string.all_cancel),
            onConfirm = model::deleteCombo,
            onDismiss = model::hideDeleteDialog
        )
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(comboList.value, key = { it.combo.comboId }) {
            ComboItem(
                comboWithTechniques = it,
                onItemClick = onNavigationRequest,
                onEdit = onNavigationRequest,
                onDelete = model::showDeleteDialog
            )
        }
    }
}

@Composable
private fun ComboItem(
    comboWithTechniques: ComboWithTechniques,
    onItemClick: (id: Long) -> Unit,
    onEdit: (id: Long) -> Unit,
    onDelete: (id: Long) -> Unit
) {
    val comboId = comboWithTechniques.combo.comboId

    TripleLineItem(
        primaryText = comboWithTechniques.combo.name,
        secondaryText = comboWithTechniques.combo.description,
        tertiaryText = getTechniqueNumberFromCombo(comboWithTechniques.techniques),
        onItemClick = { onItemClick(comboId) },
        onEdit = { onEdit(comboId) },
        onDelete = { onDelete(comboId) }
    )
}