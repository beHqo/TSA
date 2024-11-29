package com.thestrikingarts.domain.combo

import com.thestrikingarts.domain.model.Combo
import javax.inject.Inject

class UpsertComboUseCase @Inject constructor(private val repository: ComboCacheRepository) {
    suspend operator fun invoke(combo: Combo, techniqueIdList: List<Long>) {
        if (combo.id == 0L) repository.insert(combo, techniqueIdList)
        else repository.update(combo, techniqueIdList)
    }
}