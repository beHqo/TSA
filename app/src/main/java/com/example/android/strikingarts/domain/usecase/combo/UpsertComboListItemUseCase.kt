package com.example.android.strikingarts.domain.usecase.combo

import com.example.android.strikingarts.domain.model.ComboListItem
import com.example.android.strikingarts.domain.repository.ComboCacheRepository
import javax.inject.Inject

class UpsertComboListItemUseCase @Inject constructor(private val repository: ComboCacheRepository) {
    suspend operator fun invoke(comboListItem: ComboListItem, techniqueIdList: List<Long>) {
        if (comboListItem.id == 0L) repository.insertComboTechniqueCrossRef(
            comboListItem, techniqueIdList
        ) else repository.updateComboTechniqueCrossRef(comboListItem, techniqueIdList)
    }
}