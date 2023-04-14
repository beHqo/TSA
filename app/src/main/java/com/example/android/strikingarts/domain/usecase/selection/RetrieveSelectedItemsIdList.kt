package com.example.android.strikingarts.domain.usecase.selection

import javax.inject.Inject

class RetrieveSelectedItemsIdList @Inject constructor(private val selectionUseCase: SelectionUseCase) {
    operator fun invoke() = selectionUseCase.selectedItemsIdList
}