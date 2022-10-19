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
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.ui.components.ConfirmDialog
import com.example.android.strikingarts.ui.components.TripleLineItem
import com.example.android.strikingarts.utils.ImmutableList
import com.example.android.strikingarts.utils.getTechniqueNumberFromCombo

@Composable
fun ComboScreen(
    model: ComboViewModel = hiltViewModel(), navigateToComboDetailsScreen: (id: Long) -> Unit
) {
    val state = model.uiState.collectAsState()

    if (state.value.showDeleteDialog) {
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
        items(state.value.visibleCombos, key = { it.combo.comboId }) {
            ComboItem(
                comboId = it.combo.comboId,
                comboName = it.combo.name,
                comboDesc = it.combo.description,
                techniqueList = ImmutableList(it.techniques),
                onItemClick = {},
                onEdit = navigateToComboDetailsScreen,
                onDelete = model::showDeleteDialog
            )
        }
    }
}

@Composable
private fun ComboItem(
    comboId: Long, // Need to pass this in order to avoid using unstable lambdas for our onClick(s)
    comboName: String,
    comboDesc: String,
    techniqueList: ImmutableList<Technique>,
    onItemClick: (id: Long) -> Unit,
    onEdit: (id: Long) -> Unit,
    onDelete: (id: Long) -> Unit
) {
    TripleLineItem(
        itemId = comboId,
        primaryText = comboName,
        secondaryText = comboDesc,
        tertiaryText = getTechniqueNumberFromCombo(techniqueList),
        onItemClick = onItemClick,
        onEdit = onEdit,
        onDelete = onDelete
    )
}