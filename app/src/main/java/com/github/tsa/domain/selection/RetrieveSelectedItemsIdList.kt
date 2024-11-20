package com.github.tsa.domain.selection

import javax.inject.Inject

class RetrieveSelectedItemsIdList @Inject constructor(private val selectionUseCase: SelectionUseCase) {
    operator fun invoke() = selectionUseCase.selectedItemsIdList
}