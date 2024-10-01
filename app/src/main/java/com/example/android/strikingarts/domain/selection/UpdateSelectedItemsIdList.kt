package com.example.android.strikingarts.domain.selection

import javax.inject.Inject

class UpdateSelectedItemsIdList @Inject constructor(private val selectionUseCase: SelectionUseCase) {
    operator fun invoke(idList: List<Long>) {
        selectionUseCase.updateSelectedItems(idList)
    }
}