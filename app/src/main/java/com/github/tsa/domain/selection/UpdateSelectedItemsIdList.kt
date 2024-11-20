package com.github.tsa.domain.selection

import javax.inject.Inject

class UpdateSelectedItemsIdList @Inject constructor(private val selectionUseCase: SelectionUseCase) {
    operator fun invoke(idList: List<Long>) {
        selectionUseCase.updateSelectedItems(idList)
    }
}