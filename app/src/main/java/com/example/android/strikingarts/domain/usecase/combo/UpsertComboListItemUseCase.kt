package com.example.android.strikingarts.domain.usecase.combo

import com.example.android.strikingarts.domain.interfaces.ComboCacheRepository
import com.example.android.strikingarts.domain.model.Combo
import javax.inject.Inject

class UpsertComboListItemUseCase @Inject constructor(private val repository: ComboCacheRepository) {
    suspend operator fun invoke(comboListItem: Combo, techniqueIdList: List<Long>) {
        if (comboListItem.id == 0L) repository.insert(comboListItem, techniqueIdList)
        else repository.update(comboListItem, techniqueIdList)
    }
}